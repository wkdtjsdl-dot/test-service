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
    val estimateDtlId: String,
    val estimateId: String,
    val seq: Int,
    val item: String,
    val qnty: BigDecimal,
    val unitPrice: BigDecimal,
    val supval: BigDecimal,
    val addtax: BigDecimal,
    val demandCharge: BigDecimal,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
) {
    companion object {
        fun from(item: EstimateItem): EstimateItemResponse {
            return EstimateItemResponse(
                estimateDtlId = item.estimateDtlId!!,
                estimateId = item.estimateId,
                seq = item.seq,
                item = item.item,
                qnty = item.qnty,
                unitPrice = item.unitPrice,
                supval = item.supval,
                addtax = item.addtax,
                demandCharge = item.demandCharge,
                creator = item.creator,
                createDtime = item.createDtime,
                updater = item.updater,
                updateDtime = item.updateDtime
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
