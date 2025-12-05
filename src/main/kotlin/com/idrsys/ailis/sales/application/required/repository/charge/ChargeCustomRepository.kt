package com.idrsys.ailis.sales.application.required.repository.charge

import com.idrsys.ailis.sales.application.dto.query.ChargeWithDetails
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.domain.model.Charge
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface ChargeCustomRepository {
    fun findCharges(searchParam: ChargeSearchParam, pageable: Pageable): Flow<ChargeWithDetails>
    suspend fun countCharge(searchParam: ChargeSearchParam): Long
    suspend fun findChargeWithDetailsById(custChargeId: String): ChargeWithDetails?

    // 신규: UK 중복 검증
    suspend fun existsByUniqueKey(
        custMstId: String,
        applyStartDt: LocalDate,
        tstCd: String,
        excludeId: String? = null
    ): Boolean

    // 신규: 기간 겹침 검증
    suspend fun findOverlappingPeriods(
        custMstId: String,
        tstCd: String,
        startDt: LocalDate,
        endDt: LocalDate,
        excludeId: String? = null
    ): List<Charge>
}