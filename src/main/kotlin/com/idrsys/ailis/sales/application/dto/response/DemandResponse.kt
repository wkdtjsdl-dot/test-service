package com.idrsys.ailis.sales.application.dto.response

import com.idrsys.ailis.sales.application.dto.query.DemandWithCustInfo
import com.idrsys.ailis.sales.domain.model.Demand
import com.idrsys.web.excel.ExcelColumn
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

data class DemandDomesticExcelRow(
    @ExcelColumn("거래처코드") val custCd: String?,
    @ExcelColumn("거래처명") val custNm: String?,
    @ExcelColumn("영업소코드") val bzoffiCd: String?,
    @ExcelColumn("청구기준일") val demandDt: String?,
    @ExcelColumn("청구시작일") val demandStartDt: String?,
    @ExcelColumn("청구종료일") val demandEndDt: String?,
    @ExcelColumn("기준수가") val stndPrice: BigDecimal?,
    @ExcelColumn("공급가액") val supval: BigDecimal?,
    @ExcelColumn("부가세액") val addtax: BigDecimal?,
    @ExcelColumn("청구수가") val demandCharge: BigDecimal?,
    @ExcelColumn("할인율") val dscntRate: BigDecimal?,
    @ExcelColumn("전표번호") val slstmtNo: String?,
    @ExcelColumn("전표전송일") val slstmtSendDt: String?,
    @ExcelColumn("계산서발행") val billPublYn: String?,
    @ExcelColumn("SAP고객코드") val sapCustCd: String?,
    @ExcelColumn("청구메모") val demandMemo: String?,
)

internal fun DemandResponse.toDomesticExcelRow() = DemandDomesticExcelRow(
    custCd = custCd,
    custNm = custNm,
    bzoffiCd = bzoffiCd,
    demandDt = demandDt?.toString(),
    demandStartDt = demandStartDt?.toString(),
    demandEndDt = demandEndDt?.toString(),
    stndPrice = stndPrice,
    supval = supval,
    addtax = addtax,
    demandCharge = demandCharge,
    dscntRate = dscntRate,
    slstmtNo = slstmtNo,
    slstmtSendDt = slstmtSendDt?.toString(),
    billPublYn = if (billPublYn) "Y" else "N",
    sapCustCd = sapCustCd,
    demandMemo = demandMemo,
)

data class DemandForeignExcelRow(
    @ExcelColumn("거래처코드") val custCd: String?,
    @ExcelColumn("거래처명") val custNm: String?,
    @ExcelColumn("영업소코드") val bzoffiCd: String?,
    @ExcelColumn("청구기준일") val demandDt: String?,
    @ExcelColumn("청구시작일") val demandStartDt: String?,
    @ExcelColumn("청구종료일") val demandEndDt: String?,
    @ExcelColumn("통화코드") val crcyCd: String?,
    @ExcelColumn("공급가액") val supval: BigDecimal?,
    @ExcelColumn("부가세액") val addtax: BigDecimal?,
    @ExcelColumn("청구수가") val demandCharge: BigDecimal?,
    @ExcelColumn("해외청구수가") val frgnCrcyAmt: BigDecimal?,
    @ExcelColumn("할인율") val dscntRate: BigDecimal?,
    @ExcelColumn("전표번호") val slstmtNo: String?,
    @ExcelColumn("전표전송일") val slstmtSendDt: String?,
    @ExcelColumn("계산서발행") val billPublYn: String?,
    @ExcelColumn("SAP고객코드") val sapCustCd: String?,
    @ExcelColumn("청구메모") val demandMemo: String?,
)

internal fun DemandResponse.toForeignExcelRow() = DemandForeignExcelRow(
    custCd = custCd,
    custNm = custNm,
    bzoffiCd = bzoffiCd,
    demandDt = demandDt?.toString(),
    demandStartDt = demandStartDt?.toString(),
    demandEndDt = demandEndDt?.toString(),
    crcyCd = crcyCd,
    supval = supval,
    addtax = addtax,
    demandCharge = demandCharge,
    frgnCrcyAmt = frgnCrcyAmt,
    dscntRate = dscntRate,
    slstmtNo = slstmtNo,
    slstmtSendDt = slstmtSendDt?.toString(),
    billPublYn = if (billPublYn) "Y" else "N",
    sapCustCd = sapCustCd,
    demandMemo = demandMemo,
)
