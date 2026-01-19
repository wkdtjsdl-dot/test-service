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
    val demandStndDt: LocalDate,
    val stndPrice: BigDecimal,
    val supval: BigDecimal,
    val addtax: BigDecimal,
    val demandCharge: BigDecimal,
    val dscntRate: BigDecimal,
    val slstmtNo: String? = null,
    val slstmtSendDt: LocalDate? = null,
    val billPublYn: Boolean,
    val invcRecpEmailAddr: String? = null,
    val demandMemo: String? = null,
    val creator: String,
    val createDtime: LocalDateTime,
    val colledgerId: String? = null,
    val createdRequestCount: Int? = null
) {
    companion object {
        fun from(demand: Demand, colledgerId: String? = null, createdRequestCount: Int? = null): DemandResponse {
            return DemandResponse(
                demandId = demand.demandId!!,
                custCd = demand.custCd,
                demandDt = demand.demandDt,
                demandStartDt = demand.demandStartDt,
                demandStndDt = demand.demandStndDt,
                stndPrice = demand.stndPrice,
                supval = demand.supval,
                addtax = demand.addtax,
                demandCharge = demand.demandCharge,
                dscntRate = demand.dscntRate,
                slstmtNo = demand.slstmtNo,
                slstmtSendDt = demand.slstmtSendDt,
                billPublYn = demand.billPublYn,
                invcRecpEmailAddr = demand.invcRecpEmailAddr,
                demandMemo = demand.demandMemo,
                creator = demand.creator,
                createDtime = demand.createDtime,
                colledgerId = colledgerId,
                createdRequestCount = createdRequestCount
            )
        }

        fun from(demandWithCust: DemandWithCustInfo): DemandResponse {
            return DemandResponse(
                demandId = demandWithCust.demandId,
                custCd = demandWithCust.custCd,
                custNm = demandWithCust.custNm,
                demandDt = demandWithCust.demandDt,
                demandStartDt = demandWithCust.demandStartDt,
                demandStndDt = demandWithCust.demandStndDt,
                stndPrice = demandWithCust.stndPrice,
                supval = demandWithCust.supval,
                addtax = demandWithCust.addtax,
                demandCharge = demandWithCust.demandCharge,
                dscntRate = demandWithCust.dscntRate,
                slstmtNo = demandWithCust.slstmtNo,
                slstmtSendDt = demandWithCust.slstmtSendDt,
                billPublYn = demandWithCust.billPublYn,
                invcRecpEmailAddr = demandWithCust.invcRecpEmailAddr,
                creator = demandWithCust.creator,
                createDtime = demandWithCust.createDtime,
                colledgerId = demandWithCust.colledgerId
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
