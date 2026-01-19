package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.adapter.external.BaseServiceClient
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeRegisterCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import com.idrsys.ailis.sales.application.dto.response.inner.CustChargeInnerResponse
import com.idrsys.ailis.sales.application.required.repository.charge.ChargeCustomRepository
import com.idrsys.ailis.sales.application.required.repository.charge.ChargeRepository
import com.idrsys.ailis.sales.application.usecase.charge.ChargeUseCase
import com.idrsys.ailis.sales.shared.constant.ChargeErrorCode
import com.idrsys.ailis.sales.shared.mapper.ChargeMapper
import com.idrsys.ailis.sales.shared.util.ChargeValidator
import com.idrsys.ailis.sales.shared.util.PeriodClassifier
import com.idrsys.ailis.sales.shared.util.PeriodType
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Service
class ChargeService(
    private val chargeCustomRepository: ChargeCustomRepository,
    private val chargeMapper: ChargeMapper,
    private val baseServiceClient: BaseServiceClient,
    private val chargeRepository: ChargeRepository,
    private val chargeValidator: ChargeValidator
) : ChargeUseCase {

    override suspend fun getChargePage(
        searchParam: ChargeSearchParam,
        pageable: Pageable
    ): Page<ChargeResponse> {

        var finalSearchParam = searchParam

        val users = baseServiceClient.getUsers() ?: emptyList()
        val userMap = users.associateBy { it.userId }

        if (!searchParam.empUserIdNm.isNullOrBlank()) {
            val keyword = searchParam.empUserIdNm
            val userIds = userMap.values.filter { it.userNm.contains(keyword, ignoreCase = true) || it.userId.contains(keyword, ignoreCase = true) }
                .map { it.userId }
                .toList()
            finalSearchParam = searchParam.copy(empUserIds = userIds)
        }

        val departments = baseServiceClient.getDepartments() ?: emptyList()
        val deptNameByCd = departments.associate { it.deptCd to it.deptNm }

        val total = chargeCustomRepository.countCharge(finalSearchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val charges: List<ChargeResponse> = chargeCustomRepository.findCharges(finalSearchParam, pageable)
            .map(chargeMapper::toResponse)
            .toList()

        val chargeResponses = charges.map { charge ->
            val updatedSalesPics = charge.salesPics?.map { pic ->
                pic.copy(empUserNm = userMap[pic.empUserId]?.userNm)
            }
            charge.copy(
                bzoffiNm = deptNameByCd[charge.bzoffiCd],
                salesPics = updatedSalesPics
            )
        }

        return PageImpl(chargeResponses, pageable, total)
    }
    
    override suspend fun getCharges(searchParam: ChargeSearchParam): List<ChargeResponse> {
        var finalSearchParam = searchParam

        val users = baseServiceClient.getUsers() ?: emptyList()
        val userMap = users.associateBy { it.userId }

        if (!searchParam.empUserIdNm.isNullOrBlank()) {
            val keyword = searchParam.empUserIdNm
            val userIds = userMap.values.filter { it.userNm.contains(keyword, ignoreCase = true) || it.userId.contains(keyword, ignoreCase = true) }
                .map { it.userId }
                .toList()
            finalSearchParam = searchParam.copy(empUserIds = userIds)
        }

        val departments = baseServiceClient.getDepartments() ?: emptyList()
        val deptNameByCd = departments.associate { it.deptCd to it.deptNm }

        return chargeCustomRepository.findCharges(finalSearchParam, Pageable.unpaged())
            .map(chargeMapper::toResponse)
            .map { charge ->
                val updatedSalesPics = charge.salesPics?.map { pic ->
                    pic.copy(empUserNm = userMap[pic.empUserId]?.userNm)
                }
                charge.copy(bzoffiNm = deptNameByCd[charge.bzoffiCd], salesPics = updatedSalesPics)
            }.toList()
    }
    

    @Transactional
    override suspend fun registerCharge(command: ChargeRegisterCommand, creator: String): ChargeResponse {
        // 검증 로직 추가
        chargeValidator.validateForCreate(
            custMstId = command.custMstId,
            applyStartDt = command.applyStartDt,
            applyEndDt = command.applyEndDt ?: LocalDate.of(9999, 12, 31),
            tstCd = command.tstCd
        )

        val now = LocalDateTime.now()
        val newChargeId = UUID.randomUUID().toString()
        val newCharge = chargeMapper.toDomain(command, newChargeId, creator, now).apply { setAsNew() }
        val savedCharge = chargeRepository.save(newCharge)
        return chargeMapper.toResponse(savedCharge)
    }

    @Transactional
    override suspend fun updateCharge(custChargeId: String, command: ChargeUpdateCommand, updater: String): ChargeResponse {
        val charge = chargeRepository.findById(custChargeId)
            ?: throw UserDefinedException(
                ChargeErrorCode.NOT_FOUND_CODE,
                ChargeErrorCode.NOT_FOUND_MESSAGE
            )

        val today = LocalDate.now()
        val periodType = PeriodClassifier.classifyPeriod(charge.applyStartDt, charge.applyEndDt, today)

        return when (periodType) {
            PeriodType.PAST -> {
                // 과거 구간: 수정 불가
                throw UserDefinedException(
                    ChargeErrorCode.UPDATE_NOT_ALLOWED_CODE,
                    ChargeErrorCode.UPDATE_NOT_ALLOWED_MESSAGE
                )
            }
            PeriodType.CURRENT -> {
                // 현재 구간: 이력 끊기 (사용자 입력 시작일 기준)
                val newStartDt = command.applyStartDt
                val previousEndDt = newStartDt.minusDays(1)

                chargeValidator.validateForUpdate(
                    custChargeId = custChargeId,
                    custMstId = charge.custMstId ?: throw IllegalStateException("custMstId is null"),
                    applyStartDt = newStartDt,
                    applyEndDt = command.applyEndDt,
                    tstCd = charge.tstCd
                )

                // 기존 레코드 종료일 = (새 시작일 - 1일)
                charge.update(
                    ChargeUpdateCommand(
                        applyStartDt = charge.applyStartDt,
                        applyEndDt = previousEndDt,
                        crcyCd = charge.crcyCd,
                        specialCharge = charge.specialCharge,
                        supval = charge.supval,
                        addtax = charge.addtax,
                        remark = charge.remark
                    ),
                    updater
                )
                chargeRepository.save(charge)

                // 신규 레코드 생성 (사용자 입력 시작일부터)
                val newChargeId = UUID.randomUUID().toString()
                val now = LocalDateTime.now()
                val newCharge = chargeMapper.toDomain(
                    ChargeRegisterCommand(
                        custMstId = charge.custMstId ?: throw IllegalStateException("custMstId is null"),
                        custCd = charge.custCd,
                        applyStartDt = newStartDt,
                        applyEndDt = command.applyEndDt,
                        tstCd = charge.tstCd,
                        crcyCd = command.crcyCd,
                        stndPrice = charge.stndPrice,
                        specialCharge = command.specialCharge,
                        supval = command.supval,
                        addtax = command.addtax,
                        remark = command.remark,
                        lastApprStatCd = charge.lastApprStatCd
                    ),
                    newChargeId,
                    updater,
                    now
                ).apply { setAsNew() }

                val savedNewCharge = chargeRepository.save(newCharge)
                chargeMapper.toResponse(savedNewCharge)
            }
            PeriodType.FUTURE -> {
                // 미래 구간: 단순 UPDATE (시작일 유지, 종료일/금액만 변경)
                chargeValidator.validateForUpdate(
                    custChargeId = custChargeId,
                    custMstId = charge.custMstId ?: throw IllegalStateException("custMstId is null"),
                    applyStartDt = charge.applyStartDt,
                    applyEndDt = command.applyEndDt,
                    tstCd = charge.tstCd
                )

                charge.update(
                    ChargeUpdateCommand(
                        applyStartDt = charge.applyStartDt,
                        applyEndDt = command.applyEndDt,
                        crcyCd = command.crcyCd,
                        specialCharge = command.specialCharge,
                        supval = command.supval,
                        addtax = command.addtax,
                        remark = command.remark
                    ),
                    updater
                )
                val updatedCharge = chargeRepository.save(charge)
                chargeMapper.toResponse(updatedCharge)
            }
        }
    }

    @Transactional
    override suspend fun deleteCharge(custChargeId: String) {
        val charge = chargeRepository.findById(custChargeId)
            ?: throw UserDefinedException(
                ChargeErrorCode.NOT_FOUND_CODE,
                ChargeErrorCode.NOT_FOUND_MESSAGE
            )

        val today = LocalDate.now()
        val periodType = PeriodClassifier.classifyPeriod(charge.applyStartDt, charge.applyEndDt, today)

        when (periodType) {
            PeriodType.PAST -> {
                // 과거 구간: 삭제 불가
                throw UserDefinedException(
                    ChargeErrorCode.DELETION_NOT_ALLOWED_CODE,
                    ChargeErrorCode.DELETION_NOT_ALLOWED_MESSAGE
                )
            }
            PeriodType.CURRENT -> {
                // 현재 구간: 조기 종료 (종료일을 어제로 설정)
                val yesterday = today.minusDays(1)
                charge.update(
                    ChargeUpdateCommand(
                        applyEndDt = yesterday,
                        crcyCd = charge.crcyCd,
                        specialCharge = charge.specialCharge,
                        supval = charge.supval,
                        addtax = charge.addtax,
                        remark = charge.remark,
                        applyStartDt = charge.applyStartDt
                    ),
                    "SYSTEM"
                )
                chargeRepository.save(charge)
            }
            PeriodType.FUTURE -> {
                // 미래 구간: 물리적 삭제
                chargeRepository.deleteById(custChargeId)
            }
        }
    }

    override suspend fun getCharge(custChargeId: String): ChargeResponse {
        val chargeWithDetails = chargeCustomRepository.findChargeWithDetailsById(custChargeId)
            ?: throw UserDefinedException(
                ChargeErrorCode.NOT_FOUND_CODE,
                ChargeErrorCode.NOT_FOUND_MESSAGE
            )

        return chargeMapper.toResponse(chargeWithDetails)
    }

    override suspend fun getCustChargesForBilling(
        custCds: List<String>,
        tstCds: List<String>,
        startDt: LocalDate,
        endDt: LocalDate
    ): List<CustChargeInnerResponse> {
        return chargeCustomRepository.findCustChargesByConditions(custCds, tstCds, startDt, endDt)
    }
}
