package com.idrsys.ailis.sales.application.required.repository.charge

import com.idrsys.ailis.sales.application.dto.query.ChargeWithDetails
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.dto.response.inner.CustChargeInnerResponse
import com.idrsys.ailis.sales.domain.model.Charge
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface ChargeCustomRepository {
    fun findCharges(searchParam: ChargeSearchParam, pageable: Pageable): Flow<ChargeWithDetails>
    suspend fun countCharge(searchParam: ChargeSearchParam): Long
    suspend fun findChargeWithDetailsById(custChargeId: String): ChargeWithDetails?

    // 신규: UK 중복 검증 (cust_cd, apply_start_dt, tst_cd)
    suspend fun existsByUniqueKey(
        custCd: String,
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

    // 기간 겹침 검증 (특정 상태 필터)
    suspend fun findOverlappingPeriodsWithStatus(
        custMstId: String,
        tstCd: String,
        startDt: LocalDate,
        endDt: LocalDate,
        lastApprStatCd: String,
        excludeId: String? = null
    ): List<Charge>

    // --- Approval CAS Methods ---
    suspend fun updateToInProgressWithCAS(
        custChargeId: String,
        apprInfoNo: Long,
        currApprSeq: Int,
        apprSubmsEmpNo: String,
        apprLvlCd: String,
        updater: String
    ): Int

    suspend fun incrementApprSeqWithCAS(
        custChargeId: String,
        currentSeq: Int,
        newSeq: Int,
        updater: String
    ): Int

    suspend fun completeApprovalWithCAS(
        custChargeId: String,
        currentSeq: Int,
        updater: String
    ): Int

    suspend fun deleteWithCAS(
        custChargeId: String,
        currentSeq: Int
    ): Int

    // 청구수가 재계산용 고객수가 조회
    suspend fun findCustChargesByConditions(
        custCds: List<String>,
        tstCds: List<String>,
        startDt: LocalDate,
        endDt: LocalDate
    ): List<CustChargeInnerResponse>
}
