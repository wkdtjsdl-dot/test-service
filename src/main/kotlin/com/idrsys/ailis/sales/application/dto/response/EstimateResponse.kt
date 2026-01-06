package com.idrsys.ailis.sales.application.dto.response

import com.idrsys.ailis.sales.domain.model.Estimate
import com.idrsys.ailis.sales.domain.model.EstimateItem
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Estimate Response DTO
 */
data class EstimateResponse(
    val estimateId: String,
    val docType: String,
    val docNo: String,
    val regDt: LocalDate,
    val title: String,
    val receiver: String? = null,
    val reference: String? = null,
    val writerEmpNo: String? = null,
    val deptCd: String? = null,
    val remark: String? = null,
    val note: String? = null,
    val items: List<EstimateItemResponse>,
    val totalSupval: BigDecimal,
    val totalAddtax: BigDecimal,
    val totalAmt: BigDecimal,
    val creator: String,
    val createDtime: LocalDateTime
) {
    companion object {
        fun from(estimate: Estimate, items: List<EstimateItem>): EstimateResponse {
            return EstimateResponse(
                estimateId = estimate.estimateId!!,
                docType = estimate.docType,
                docNo = estimate.docNo,
                regDt = estimate.regDt,
                title = estimate.title,
                receiver = estimate.receiver,
                reference = estimate.reference,
                items = items.map { EstimateItemResponse.from(it) },
                totalSupval = estimate.totalSupval,
                totalAddtax = estimate.totalAddtax,
                totalAmt = estimate.totalAmt,
                creator = estimate.creator,
                createDtime = estimate.createDtime
            )
        }
    }
}

/**
 * Estimate Item Response DTO
 */
data class EstimateItemResponse(
    val estimateItemId: String,
    val estimateId: String,
    val itemNm: String,
    val qty: BigDecimal,
    val unitPrice: BigDecimal,
    val supval: BigDecimal,
    val addtax: BigDecimal,
    val totalAmt: BigDecimal
) {
    companion object {
        fun from(item: EstimateItem): EstimateItemResponse {
            return EstimateItemResponse(
                estimateItemId = item.estimateItemId,
                estimateId = item.estimateId,
                itemNm = item.itemNm,
                qty = item.qty,
                unitPrice = item.unitPrice,
                supval = item.supval,
                addtax = item.addtax,
                totalAmt = item.totalAmt
            )
        }
    }
}

/**
 * Delete Estimate Response DTO
 */
data class DeleteEstimateResponse(
    val estimateId: String,
    val deleted: Boolean
)
