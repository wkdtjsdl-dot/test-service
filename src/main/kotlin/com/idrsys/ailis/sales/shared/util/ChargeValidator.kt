package com.idrsys.ailis.sales.shared.util

import com.idrsys.ailis.sales.application.required.repository.charge.ChargeCustomRepository
import com.idrsys.ailis.sales.shared.constant.ChargeErrorCode
import com.idrsys.web.exception.UserDefinedException
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ChargeValidator(
    private val chargeCustomRepository: ChargeCustomRepository
) {

    /**
     * 날짜 범위 검증
     * @throws UserDefinedException 시작일 > 종료일인 경우
     */
    fun validateDateRange(startDt: LocalDate, endDt: LocalDate) {
        if (startDt > endDt) {
            throw UserDefinedException(
                ChargeErrorCode.INVALID_DATE_RANGE_CODE,
                ChargeErrorCode.INVALID_DATE_RANGE_MESSAGE
            )
        }
    }

    /**
     * UK 중복 검증
     * @throws UserDefinedException UK 중복 시
     */
    suspend fun validateUniqueKey(
        custMstId: String,
        applyStartDt: LocalDate,
        tstCd: String,
        excludeId: String? = null
    ) {
        val exists = chargeCustomRepository.existsByUniqueKey(
            custMstId = custMstId,
            applyStartDt = applyStartDt,
            tstCd = tstCd,
            excludeId = excludeId
        )

        if (exists) {
            throw UserDefinedException(
                ChargeErrorCode.UK_DUPLICATE_CODE,
                ChargeErrorCode.UK_DUPLICATE_MESSAGE
            )
        }
    }

    /**
     * 기간 겹침 검증
     * @throws UserDefinedException 기간이 겹치는 경우cc
     */
    suspend fun validatePeriodOverlap(
        custMstId: String,
        tstCd: String,
        startDt: LocalDate,
        endDt: LocalDate,
        excludeId: String? = null
    ) {
        val overlapping = chargeCustomRepository.findOverlappingPeriods(
            custMstId = custMstId,
            tstCd = tstCd,
            startDt = startDt,
            endDt = endDt,
            excludeId = excludeId
        )

        if (overlapping.isNotEmpty()) {
            // 첫 번째 겹치는 기간 정보 추출
            val first = overlapping.first()
            val message = "입력한 기간(${startDt} ~ ${endDt})이 기존 수가 기간(${first.applyStartDt} ~ ${first.applyEndDt})과 겹칩니다"

            throw UserDefinedException(
                ChargeErrorCode.PERIOD_OVERLAP_CODE,
                message
            )
        }
    }

    /**
     * 신규 등록 시 전체 검증
     */
    suspend fun validateForCreate(
        custMstId: String,
        applyStartDt: LocalDate,
        applyEndDt: LocalDate,
        tstCd: String
    ) {
        validateDateRange(applyStartDt, applyEndDt)
        validateUniqueKey(custMstId, applyStartDt, tstCd)
        validatePeriodOverlap(custMstId, tstCd, applyStartDt, applyEndDt)
    }

    /**
     * 수정 시 전체 검증 (미래 구간만 수정 가능하므로 기간 겹침 검증 필요)
     */
    suspend fun validateForUpdate(
        custChargeId: String,
        custMstId: String,
        applyStartDt: LocalDate,
        applyEndDt: LocalDate,
        tstCd: String
    ) {
        validateDateRange(applyStartDt, applyEndDt)

        // UK 중복 검증 (현재 구간 이력 끊기 시 today로 시작하는 새 레코드의 UK 검증)
        validateUniqueKey(custMstId, applyStartDt, tstCd, excludeId = custChargeId)

        // 기간 겹침 검증
        validatePeriodOverlap(custMstId, tstCd, applyStartDt, applyEndDt, excludeId = custChargeId)
    }
}
