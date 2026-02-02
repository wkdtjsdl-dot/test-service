package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.charge.ChargeRegisterCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ExcelChargeRegisterCommand
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import com.idrsys.ailis.sales.application.dto.response.ExcelRegisterValidationResponse
import com.idrsys.ailis.sales.application.dto.response.ValidationError
import com.idrsys.ailis.sales.application.dto.response.inner.CustChargeInnerResponse
import com.idrsys.ailis.sales.application.required.external.BaseServicePort
import com.idrsys.ailis.sales.application.required.external.TstServicePort
import com.idrsys.ailis.sales.application.required.repository.charge.ChargeCustomRepository
import com.idrsys.ailis.sales.application.required.repository.charge.ChargeRepository
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
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
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional(readOnly = false)
class ChargeService(
    private val chargeCustomRepository: ChargeCustomRepository,
    private val chargeMapper: ChargeMapper,
    private val baseServicePort: BaseServicePort,
    private val chargeRepository: ChargeRepository,
    private val chargeValidator: ChargeValidator,
    private val tstServicePort: TstServicePort,
    private val custCustomRepository: CustCustomRepository
) : ChargeUseCase {

    companion object {
        private val logger = LoggerFactory.getLogger(ChargeService::class.java)
    }

    @Transactional(readOnly = true)
    override suspend fun getChargePage(
        searchParam: ChargeSearchParam,
        pageable: Pageable
    ): Page<ChargeResponse> {

        var finalSearchParam = searchParam

        val users = baseServicePort.getUsers() ?: emptyList()
        val userMap = users.associateBy { it.userId }

        if (!searchParam.empUserIdNm.isNullOrBlank()) {
            val keyword = searchParam.empUserIdNm
            val userIds = userMap.values.filter { it.userNm.contains(keyword, ignoreCase = true) || it.userId.contains(keyword, ignoreCase = true) }
                .map { it.userId }
                .toList()
            finalSearchParam = searchParam.copy(empUserIds = userIds)
        }

        val departments = baseServicePort.getDepartments() ?: emptyList()
        val deptNameByCd = departments.associate { it.deptCd to it.deptNm }

        val systemCodeMaps = fetchSystemCodeMaps()

        val total = chargeCustomRepository.countCharge(finalSearchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val charges: List<ChargeResponse> = chargeCustomRepository.findCharges(finalSearchParam, pageable)
            .map(chargeMapper::toResponse)
            .toList()

        val tstCds = charges.map { it.tstCd }.distinct()
        val testItems = tstServicePort.findTestItemByTestCode(tstCds) ?: emptyList()
        val tstNameByCode = testItems.associate { it.tstCd to it.tstNm }

        val chargeResponses = charges.map { charge ->
            val updatedSalesPics = charge.salesPics?.map { pic ->
                pic.copy(empUserNm = userMap[pic.empUserId]?.userNm)
            }
            charge.copy(
                bzoffiNm = deptNameByCd[charge.bzoffiCd],
                tstNm = tstNameByCode[charge.tstCd],
                crcyCd = systemCodeMaps.crcyNameByCd[charge.crcyCd] ?: charge.crcyCd,
                lastApprStatCd = systemCodeMaps.lastApprStatNameByCd[charge.lastApprStatCd] ?: charge.lastApprStatCd,
                salesPics = updatedSalesPics
            )
        }

        return PageImpl(chargeResponses, pageable, total)
    }

    @Transactional(readOnly = true)
    override suspend fun getCharges(searchParam: ChargeSearchParam): List<ChargeResponse> {
        var finalSearchParam = searchParam

        val users = baseServicePort.getUsers() ?: emptyList()
        val userMap = users.associateBy { it.userId }

        if (!searchParam.empUserIdNm.isNullOrBlank()) {
            val keyword = searchParam.empUserIdNm
            val userIds = userMap.values.filter { it.userNm.contains(keyword, ignoreCase = true) || it.userId.contains(keyword, ignoreCase = true) }
                .map { it.userId }
                .toList()
            finalSearchParam = searchParam.copy(empUserIds = userIds)
        }

        val departments = baseServicePort.getDepartments() ?: emptyList()
        val deptNameByCd = departments.associate { it.deptCd to it.deptNm }

        val systemCodeMaps = fetchSystemCodeMaps()

        val charges = chargeCustomRepository.findCharges(finalSearchParam, Pageable.unpaged())
            .map(chargeMapper::toResponse)
            .toList()

        val tstCds = charges.map { it.tstCd }.distinct()
        val testItems = tstServicePort.findTestItemByTestCode(tstCds) ?: emptyList()
        val tstNameByCode = testItems.associate { it.tstCd to it.tstNm }

        return charges.map { charge ->
            val updatedSalesPics = charge.salesPics?.map { pic ->
                pic.copy(empUserNm = userMap[pic.empUserId]?.userNm)
            }
            charge.copy(
                bzoffiNm = deptNameByCd[charge.bzoffiCd],
                tstNm = tstNameByCode[charge.tstCd],
                crcyCd = systemCodeMaps.crcyNameByCd[charge.crcyCd] ?: charge.crcyCd,
                lastApprStatCd = systemCodeMaps.lastApprStatNameByCd[charge.lastApprStatCd] ?: charge.lastApprStatCd,
                salesPics = updatedSalesPics
            )
        }
    }

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
        val chargeResponse = chargeMapper.toResponse(savedCharge)
        val testItems = tstServicePort.findTestItemByTestCode(listOf(chargeResponse.tstCd))
        val tstNm = testItems?.firstOrNull()?.tstNm
        return chargeResponse.copy(tstNm = tstNm)
    }

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
                val chargeResponse = chargeMapper.toResponse(savedNewCharge)
                val testItems = tstServicePort.findTestItemByTestCode(listOf(chargeResponse.tstCd))
                val tstNm = testItems?.firstOrNull()?.tstNm
                chargeResponse.copy(tstNm = tstNm)
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
                val chargeResponse = chargeMapper.toResponse(updatedCharge)
                val testItems = tstServicePort.findTestItemByTestCode(listOf(chargeResponse.tstCd))
                val tstNm = testItems?.firstOrNull()?.tstNm
                chargeResponse.copy(tstNm = tstNm)
            }
        }
    }

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

    @Transactional(readOnly = true)
    override suspend fun getCharge(custChargeId: String): ChargeResponse {
        val chargeWithDetails = chargeCustomRepository.findChargeWithDetailsById(custChargeId)
            ?: throw UserDefinedException(
                ChargeErrorCode.NOT_FOUND_CODE,
                ChargeErrorCode.NOT_FOUND_MESSAGE
            )

        val chargeResponse = chargeMapper.toResponse(chargeWithDetails)
        val testItems = tstServicePort.findTestItemByTestCode(listOf(chargeResponse.tstCd))
        val tstNm = testItems?.firstOrNull()?.tstNm

        return chargeResponse.copy(tstNm = tstNm)
    }


    @Transactional(readOnly = true)
    override suspend fun validateExcelRegisterCharges(
        commands: List<ExcelChargeRegisterCommand>
    ): ExcelRegisterValidationResponse {
        val errors = mutableListOf<ValidationError>()

        // 1. custCd 들 모아서 custMstId 조회
        val custCds = commands.map { it.custCd }.distinct()
        val custMstIdMap = custCustomRepository.findCustMstIdsByCustCds(custCds)

        // 2. custMstId 값 합쳐서 commnad에 mapping
        val enrichedCommands = commands.mapIndexed { index, command ->
            val rowNumber = index + 2 // Excel row (헤더 제외, 1-based)
            val custMstId = custMstIdMap[command.custCd]

            if (custMstId == null) {
                errors.add(
                    ValidationError(
                        rowNumber = rowNumber,
                        custCd = command.custCd,
                        tstCd = command.tstCd,
                        fieldName = "custCd",
                        errorCode = ChargeErrorCode.CUSTOMER_NOT_FOUND_CODE,
                        errorMessage = "${ChargeErrorCode.CUSTOMER_NOT_FOUND_MESSAGE}: ${command.custCd}"
                    )
                )
                null
            } else {
                chargeMapper.toRegisterCommand(command, custMstId) to rowNumber
            }
        }

        // 3. validation
        enrichedCommands.filterNotNull().forEach { (enriched, rowNumber) ->
            // 3-1. 검사코드 validation
            val testItems = tstServicePort.findTestItemByTestCode(listOf(enriched.tstCd))
            if (testItems.isNullOrEmpty()) {
                errors.add(
                    ValidationError(
                        rowNumber = rowNumber,
                        custCd = enriched.custCd,
                        tstCd = enriched.tstCd,
                        fieldName = "tstCd",
                        errorCode = ChargeErrorCode.TEST_NOT_FOUND_CODE,
                        errorMessage = "${ChargeErrorCode.TEST_NOT_FOUND_MESSAGE}: ${enriched.tstCd}"
                    )
                )
            }

            // 3-2. 중복 validation (동일 고객, 기간, 검사코드)
            val duplicate = chargeCustomRepository.existsByUniqueKey(
                custMstId = enriched.custMstId,
                applyStartDt = enriched.applyStartDt,
                tstCd = enriched.tstCd,
                excludeId = null
            )
            if (duplicate) {
                errors.add(
                    ValidationError(
                        rowNumber = rowNumber,
                        custCd = enriched.custCd,
                        tstCd = enriched.tstCd,
                        fieldName = "duplicate",
                        errorCode = ChargeErrorCode.UK_DUPLICATE_CODE,
                        errorMessage = "${ChargeErrorCode.UK_DUPLICATE_MESSAGE} (고객: ${enriched.custCd}, 검사: ${enriched.tstCd}, 시작일: ${enriched.applyStartDt})"
                    )
                )
            }

            // 3-3. 비즈니스룰 validation (날짜, 가격, 기타.)
            if (enriched.applyEndDt != null && enriched.applyEndDt.isBefore(enriched.applyStartDt)) {
                errors.add(
                    ValidationError(
                        rowNumber = rowNumber,
                        custCd = enriched.custCd,
                        tstCd = enriched.tstCd,
                        fieldName = "applyEndDt",
                        errorCode = ChargeErrorCode.INVALID_DATE_RANGE_CODE,
                        errorMessage = ChargeErrorCode.INVALID_DATE_RANGE_MESSAGE
                    )
                )
            }
        }

        val invalidRowNumbers = errors.map { it.rowNumber }.distinct()

        return ExcelRegisterValidationResponse(
            isValid = errors.isEmpty(),
            totalCount = commands.size,
            validCount = commands.size - invalidRowNumbers.size,
            invalidCount = invalidRowNumbers.size,
            errors = errors
        )
    }

    override suspend fun excelRegisterCharges(
        commands: List<ExcelChargeRegisterCommand>,
        userId: String
    ): List<ChargeResponse> {
        // 1. Batch lookup all custMstIds in single query
        val custCds = commands.map { it.custCd }.distinct()
        val custMstIdMap = custCustomRepository.findCustMstIdsByCustCds(custCds)

        // 2. Enrich and register
        val registeredCharges = mutableListOf<ChargeResponse>()

        commands.forEach { command ->
            try {
                // Resolve custMstId (fail fast if not found)
                val custMstId = custMstIdMap[command.custCd]
                    ?: throw UserDefinedException(
                        ChargeErrorCode.CUSTOMER_NOT_FOUND_CODE,
                        "${ChargeErrorCode.CUSTOMER_NOT_FOUND_MESSAGE}: ${command.custCd}"
                    )

                // Convert to ChargeRegisterCommand with custMstId
                val registerCommand = chargeMapper.toRegisterCommand(command, custMstId)

                // Auto-adjust overlapping existing charges
                val overlapping = chargeCustomRepository.findOverlappingPeriods(
                    custMstId = custMstId,
                    tstCd = registerCommand.tstCd,
                    startDt = registerCommand.applyStartDt,
                    endDt = registerCommand.applyEndDt ?: LocalDate.of(9999, 12, 31),
                    excludeId = null
                )

                if (overlapping.isNotEmpty()) {
                    // Select only the most recent charge (latest applyStartDt)
                    val mostRecentCharge = overlapping.maxByOrNull { it.applyStartDt }

                    if (mostRecentCharge != null) {
                        // Cut off previous period (same pattern as updateCharge for CURRENT period)
                        val newEndDate = registerCommand.applyStartDt.minusDays(1)
                        mostRecentCharge.updateEndDate(newEndDate, userId)
                        chargeRepository.save(mostRecentCharge)
                    }
                }

                // 수가 등록
                val now = LocalDateTime.now()
                val newChargeId = UUID.randomUUID().toString()
                val newCharge = chargeMapper.toDomain(registerCommand, newChargeId, userId, now).apply {
                    setAsNew()
                }

                val saved = chargeRepository.save(newCharge)
                val response = chargeMapper.toResponse(saved)
                registeredCharges.add(response)

            } catch (e: Exception) {
                // Throw exception to rollback transaction if any registration fails
                throw UserDefinedException(
                    ChargeErrorCode.EXCEL_REGISTRATION_FAILED_CODE,
                    "${ChargeErrorCode.EXCEL_REGISTRATION_FAILED_MESSAGE}: ${e.message}"
                )
            }
        }

        return registeredCharges
    }

    @Transactional(readOnly = true)
    override suspend fun getCustChargesForBilling(
        custCds: List<String>,
        tstCds: List<String>,
        startDt: LocalDate,
        endDt: LocalDate
    ): List<CustChargeInnerResponse> {
        return chargeCustomRepository.findCustChargesByConditions(custCds, tstCds, startDt, endDt)
    }

    private suspend fun fetchSystemCodeMaps(): SystemCodeMaps {
        return try {
            val systemCodes = baseServicePort.getChildrenSystemCodes(listOf("CRCY", "LAST")) ?: emptyMap()

            val crcyNameByCd = systemCodes["CRCY"]?.associate { it.cd to it.cdNm } ?: emptyMap()
            val lastApprStatNameByCd = systemCodes["LAST"]?.associate { it.cd to it.cdNm } ?: emptyMap()

            SystemCodeMaps(crcyNameByCd, lastApprStatNameByCd)
        } catch (e: Exception) {
            logger.warn("Failed to fetch system codes from base-service, displaying raw codes instead", e)
            SystemCodeMaps(emptyMap(), emptyMap())
        }
    }

    data class SystemCodeMaps(
        val crcyNameByCd: Map<String, String>,
        val lastApprStatNameByCd: Map<String, String>
    )
}
