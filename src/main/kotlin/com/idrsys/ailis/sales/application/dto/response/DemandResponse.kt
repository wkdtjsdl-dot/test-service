package com.idrsys.ailis.sales.application.dto.response

import com.idrsys.ailis.sales.application.dto.query.DemandWithCustInfo
import com.idrsys.ailis.sales.domain.model.Demand
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Demand Response DTO
 */
data class DemandResponse(
    val demandId: String?,
    val custCd: String,
    val custNm: String? = null,
    val branchNm: String? = null,
    val demandDt: LocalDate,
    val demandStartDt: LocalDate,
    val demandEndDt: LocalDate,
    val demandCreatorEmpNo: String? = null,
    val demandCreateDtime: LocalDateTime,
    val stndPrice: BigDecimal,
    val supval: BigDecimal,
    val addtax: BigDecimal,
    val demandCharge: BigDecimal,
    val dscntRate: BigDecimal?,
    val slstmtNo: String? = null,
    val slstmtSendDt: LocalDate? = null,
    val billPublYn: Boolean,
    val invcRecpEmailAddr: String? = null,
    val bzoffiCd: String? = null,
    val sapCustCd: String? = null,
    val demandMemo: String? = null,
    val creator: String,
    val createDtime: LocalDateTime,
    val colledgerId: String? = null,
    val createdRequestCount: Int? = null,
    val crcyCd: String? = null,
    val crcyCdNm: String? = null,
    val frgnCrcyAmt: BigDecimal? = null,
    val tstReqDivCd: String? = null,
    val demandType: String? = null,
) {
    companion object {
        fun from(demand: Demand, colledgerId: String? = null, createdRequestCount: Int? = null): DemandResponse {
            return DemandResponse(
                demandId = demand.demandId!!,
                custCd = demand.custCd,
                demandDt = demand.demandDt,
                demandStartDt = demand.demandStartDt,
                demandEndDt = demand.demandEndDt,
                demandCreatorEmpNo = demand.demandCreatorEmpNo,
                demandCreateDtime = demand.demandCreateDtime,
                stndPrice = demand.stndPrice,
                supval = demand.supval,
                addtax = demand.addtax,
                demandCharge = demand.demandCharge,
                dscntRate = demand.dscntRate,
                slstmtNo = demand.slstmtNo,
                slstmtSendDt = demand.slstmtSendDt,
                billPublYn = demand.billPublYn,
                invcRecpEmailAddr = demand.invcRecpEmailAddr,
                bzoffiCd = null,
                sapCustCd = null,
                demandMemo = demand.demandMemo,
                creator = demand.creator,
                createDtime = demand.createDtime,
                colledgerId = colledgerId,
                createdRequestCount = createdRequestCount,
                crcyCd = demand.crcyCd,
                frgnCrcyAmt = demand.frgnCrcyAmt,
                demandType = demand.demandType
            )
        }

        fun from(demandWithCust: DemandWithCustInfo): DemandResponse {
            // demandType '30' → RQDV_PR, other non-null → NON_PR (CASE WHEN 분기와 동일 로직)
            val tstReqDivCd = when (demandWithCust.demandType) {
                "30" -> "RQDV_PR"
                null -> null
                else -> "NON_PR"
            }
            return DemandResponse(
                demandId = demandWithCust.demandId,
                custCd = demandWithCust.custCd,
                custNm = demandWithCust.custNm,
                demandDt = demandWithCust.demandDt,
                demandStartDt = demandWithCust.demandStartDt,
                demandEndDt = demandWithCust.demandEndDt,
                demandCreatorEmpNo = demandWithCust.demandCreatorEmpNo,
                demandCreateDtime = demandWithCust.demandCreateDtime,
                stndPrice = demandWithCust.stndPrice,
                supval = demandWithCust.supval,
                addtax = demandWithCust.addtax,
                demandCharge = demandWithCust.demandCharge,
                dscntRate = demandWithCust.dscntRate,
                slstmtNo = demandWithCust.slstmtNo,
                slstmtSendDt = demandWithCust.slstmtSendDt,
                billPublYn = demandWithCust.billPublYn,
                invcRecpEmailAddr = demandWithCust.invcRecpEmailAddr,
                bzoffiCd = demandWithCust.bzoffiCd,
                sapCustCd = demandWithCust.sapCustCd,
                demandMemo = demandWithCust.demandMemo,
                creator = demandWithCust.creator,
                createDtime = demandWithCust.createDtime,
                colledgerId = demandWithCust.colledgerId,
                crcyCd = demandWithCust.crcyCd,
                frgnCrcyAmt = demandWithCust.frgnCrcyAmt,
                demandType = demandWithCust.demandType,
                tstReqDivCd = tstReqDivCd
            )
        }
    }
}

/**
 * Create Demand Response DTO
 */
data class CreateDemandResponse(
    val demandId: String,
    val custCd: String,
    val demandDt: LocalDate,
    val demandCharge: BigDecimal,
    val colledgerId: String,
    val createdRequestCount: Int
)

/**
 * Cancel Demand Response DTO
 */
data class CancelDemandResponse(
    val demandId: String,
    val cancelled: Boolean,
    val releasedRequestCount: Int
)

/**
 * Send Sales Statement Response DTO
 */
data class SendSalesStatementResponse(
    val demandId: String,
    val slstmtNo: String,
    val slstmtSendDt: LocalDate,
    val sentToErp: Boolean
)
