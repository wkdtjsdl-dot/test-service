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
        // 1. Validate items
        if (command.items.isEmpty()) {
            throw UserDefinedException("INVALID_REQUEST", "최소 1개 이상의 항목이 필요합니다")
        }

        // 2. Generate document number
        val docNo = generateDocumentNumber(command.docType, command.regDt)

        // 3. Create estimate items first (총액 계산을 위해)
        val tempEstimateId = java.util.UUID.randomUUID().toString()

        val items = command.items.mapIndexed { index, itemCommand ->
            EstimateItem.create(
                estimateId = tempEstimateId,
                seq = index + 1,
                item = itemCommand.item,
                qnty = itemCommand.qnty,
                unitPrice = itemCommand.unitPrice,
                creator = adminId
            )
        }

        // 4. Calculate totals from items
        val totalSupval = items.fold(java.math.BigDecimal.ZERO) { acc, item -> acc.add(item.supval) }
        val totalAddtax = items.fold(java.math.BigDecimal.ZERO) { acc, item -> acc.add(item.addtax) }
        val totalAmt = items.fold(java.math.BigDecimal.ZERO) { acc, item -> acc.add(item.demandCharge) }

        // 5. Create estimate entity with calculated totals
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

        // 6. Save estimate items
        val savedItems = items.map { estimateItemRepository.save(it) }

        // 7. Return response
        return EstimateResponse.from(savedEstimate, savedItems)
    }

    /**
     * Update estimate with modified items
     *
     * Business Rules:
     * 1. Items are replaced entirely (not merged)
     * 2. Totals always reflect current items
     */
    override suspend fun updateEstimate(
        estimateId: String,
        command: UpdateEstimateCommand,
        adminId: String
    ): EstimateResponse {
        // 1. Find estimate
        val estimate = estimateRepository.findById(estimateId)
            ?: throw UserDefinedException("ESTIMATE_NOT_FOUND", "견적서를 찾을 수 없습니다: $estimateId")

        // 2. Validate items
        if (command.items.isEmpty()) {
            throw UserDefinedException("INVALID_REQUEST", "최소 1개 이상의 항목이 필요합니다")
        }

        // 3. Update estimate basic info
        // Note: Estimate entity doesn't have an update method,
        // we need to manually set fields or create a new instance

        // 4. Delete old items
        estimateItemRepository.deleteByEstimateId(estimateId)

        // 5. Create new items with auto-incremented seq
        val newItems = command.items.mapIndexed { index, itemCommand ->
            EstimateItem.create(
                estimateId = estimate.estimateId ?: throw UserDefinedException("INVALID_STATE", "Estimate ID is null"),
                seq = index + 1,  // 1-based sequence
                item = itemCommand.item,
                qnty = itemCommand.qnty,
                unitPrice = itemCommand.unitPrice,
                creator = adminId
            )
        }

        val savedItems = newItems.map { estimateItemRepository.save(it) }

        // 6. Recalculate totals
        estimate.recalculateTotals(savedItems)
        val updatedEstimate = estimateRepository.save(estimate)

        // 7. Return response
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
