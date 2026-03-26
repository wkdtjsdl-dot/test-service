package com.idrsys.ailis.sales.application.service.estimate

import com.idrsys.ailis.sales.application.dto.request.estimate.CreateEstimateCommand
import com.idrsys.ailis.sales.application.dto.request.estimate.UpdateEstimateCommand
import com.idrsys.ailis.sales.application.dto.response.DeleteEstimateResponse
import com.idrsys.ailis.sales.application.dto.response.EstimateResponse
import com.idrsys.ailis.sales.application.required.repository.estimate.EstimateItemRepository
import com.idrsys.ailis.sales.application.required.repository.estimate.EstimateRepository
import com.idrsys.ailis.sales.application.usecase.estimate.EstimateCommandUseCase
import com.idrsys.ailis.sales.domain.model.Estimate
import com.idrsys.ailis.sales.domain.model.EstimateItem
import com.idrsys.web.exception.UserDefinedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Estimate Command Service
 *
 * Implements write operations for estimate domain following TDD approach
 */
@Service
@Transactional
class EstimateCommandService(
    private val estimateRepository: EstimateRepository,
    private val estimateItemRepository: EstimateItemRepository,
) : EstimateCommandUseCase {

    /**
     * Create estimate with items
     *
     * Business Rules:
     * 1. Document number auto-generated: GCG-{YYYY}-{TYPE}-{SEQNO}
     * 2. Item amounts auto-calculated
     * 3. Totals auto-calculated from items
     */
//    override suspend fun createEstimate(command: CreateEstimateCommand, adminId: String): EstimateResponse {
//        // 1. Validate items
//        if (command.items.isEmpty()) {
//            throw UserDefinedException("INVALID_REQUEST", "최소 1개 이상의 항목이 필요합니다")
//        }
//
//        // 2. Generate document number
//        val docNo = generateDocumentNumber(command.docType, command.regDt)
//
//        // 3. Create estimate entity
//        val estimate = Estimate(
//            docType = command.docType,
//            docNo = docNo,
//            regDt = command.regDt,
//            title = command.title,
//            receiver = command.receiver,
//            reference = command.reference,
//            writerEmpNo = command.writerEmpNo,
//            deptCd = command.deptCd,
//            totalSupval = java.math.BigDecimal.ZERO,
//            totalAddtax = java.math.BigDecimal.ZERO,
//            totalAmt = java.math.BigDecimal.ZERO,
//            remark = command.remark,
//            note = command.note,
//            creator = adminId,
//            createDtime = LocalDateTime.now(),
//            updater = adminId,
//            updateDtime = LocalDateTime.now()
//        ).apply { setAsNew() }
//
//        val savedEstimate = estimateRepository.save(estimate)
//
//        // 4. Create estimate items with auto-incremented seq
//        val items = command.items.mapIndexed { index, itemCommand ->
//            EstimateItem.create(
//                estimateId = savedEstimate.estimateId ?: throw UserDefinedException("INVALID_STATE", "Estimate ID is null"),
//                seq = index + 1,  // 1-based sequence
//                item = itemCommand.item,
//                qnty = itemCommand.qnty,
//                unitPrice = itemCommand.unitPrice,
//                creator = adminId
//            )
//        }
//
//        val savedItems = items.map { estimateItemRepository.save(it) }
//
//        // 5. Recalculate totals
//        estimate.recalculateTotals(savedItems)
//        estimateRepository.save(estimate)
//
//        // 6. Return response
//        return EstimateResponse.from(estimate, savedItems)
//    }

    override suspend fun createEstimate(command: CreateEstimateCommand, adminId: String): EstimateResponse {
        // 초깃값 설정(items 없는 경우 대비)
        var totalSupval = java.math.BigDecimal.ZERO
        var totalAddtax = java.math.BigDecimal.ZERO
        var totalAmt = java.math.BigDecimal.ZERO

        // 문서 번호 생성
        val docNo = generateDocumentNumber(command.docType, command.regDt)

        // 임시 견적서 id 생성 (총액 계산을 위해)
        val tempEstimateId = java.util.UUID.randomUUID().toString()

        // 아이템이 있는 경우에만 저장
        val savedItems = if (!command.items.isNullOrEmpty()) {
            val items = command.items.mapIndexed { index, itemCommand ->
                EstimateItem.create(
                    estimateId = tempEstimateId,
                    seq = index + 1,
                    item = itemCommand.item,
                    qnty = itemCommand.qnty,
                    unitPrice = itemCommand.unitPrice,
                    supval = itemCommand.supval,
                    addtax = itemCommand.addtax,
                    creator = adminId
                )
            }

            totalSupval = items.sumOf { it.supval }
            totalAddtax = items.sumOf { it.addtax }
            totalAmt = items.sumOf { it.demandCharge }

            items.map { estimateItemRepository.save(it) }

        } else {
            emptyList()
        }

        // 견적서 생성
        val estimate = Estimate(
            estimateId = tempEstimateId,
            docType = command.docType,
            docNo = docNo,
            regDt = command.regDt,
            title = command.title,
            receiver = command.receiver,
            reference = command.reference,
            writerEmpNo = command.writerEmpNo,
            deptCd = command.deptCd,
            totalSupval = totalSupval,
            totalAddtax = totalAddtax,
            totalAmt = totalAmt,
            remark = command.remark,
            note = command.note,
            creator = adminId,
            createDtime = LocalDateTime.now(),
            updater = adminId,
            updateDtime = LocalDateTime.now()
        ).apply { setAsNew() }

        val savedEstimate = estimateRepository.save(estimate)

        return EstimateResponse.from(savedEstimate, savedItems)
    }

    /**
     * Update estimate with modified items
     *
     * Business Rules:
     * 1. Items are replaced entirely (not merged)
     * 2. Totals always reflect current items
     * 3. Basic info (title, receiver, etc.) is updated
     */
    override suspend fun updateEstimate(
        estimateId: String,
        command: UpdateEstimateCommand,
        adminId: String
    ): EstimateResponse {
        // ID 통해 견적서 조회
        val estimate = estimateRepository.findById(estimateId)
            ?: throw UserDefinedException("ESTIMATE_NOT_FOUND", "견적서를 찾을 수 없습니다: $estimateId")

        // 기존 견적서 항목 삭제
        estimateItemRepository.deleteByEstimateId(estimateId)

        // 아이템이 있는 경우에만 저장
        val savedItems = if (!command.items.isNullOrEmpty()) {
            val newItems = command.items.mapIndexed { index, itemCommand ->
                EstimateItem.create(
                    estimateId = estimate.estimateId ?: throw UserDefinedException("INVALID_STATE", "Estimate ID is null"),
                    seq = index + 1,  // 1-based sequence
                    item = itemCommand.item,
                    qnty = itemCommand.qnty,
                    unitPrice = itemCommand.unitPrice,
                    supval = itemCommand.supval,
                    addtax = itemCommand.addtax,
                    creator = adminId
                )
            }

            newItems.map { estimateItemRepository.save(it) }
        } else {
            emptyList()
        }

        // 견적서 업데이트
        estimate.update(
            title = command.title,
            receiver = command.receiver,
            reference = command.reference,
            writerEmpNo = command.writerEmpNo,
            deptCd = command.deptCd,
            remark = command.remark,
            note = command.note,
            items = savedItems,
            updater = adminId
        )
        val updatedEstimate = estimateRepository.save(estimate)

        return EstimateResponse.from(updatedEstimate, savedItems)
    }

    /**
     * Delete estimate
     *
     * Business Rules:
     * 1. Associated items are deleted (cascade)
     */
    override suspend fun deleteEstimate(estimateId: String, adminId: String): DeleteEstimateResponse {
        // 1. Find estimate
        val estimate = estimateRepository.findById(estimateId)
            ?: throw UserDefinedException("ESTIMATE_NOT_FOUND", "견적서를 찾을 수 없습니다: $estimateId")

        // 2. Delete associated items
        estimateItemRepository.deleteByEstimateId(estimateId)

        // 3. Delete estimate
        estimateRepository.delete(estimate)

        // 4. Return response
        return DeleteEstimateResponse(
            estimateId = estimateId,
            deleted = true
        )
    }

    /**
     * Generate document number
     *
     * Format: GCG-{YYYY}-{TYPE}-{SEQNO}
     * Example: GCG-2026-EST-000001
     */
    private suspend fun generateDocumentNumber(docType: String, regDt: LocalDate): String {
        val year = regDt.year
        val typeCode = docType // EST or TRN

        // Get next sequence number for this year and type
        // TODO: Implement proper sequence generation
        val timestamp = System.currentTimeMillis() % 1000000
        val seqNo = String.format("%06d", timestamp)

        return "GCG-$year-$typeCode-$seqNo"
    }
}
