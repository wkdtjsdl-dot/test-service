package com.idrsys.ailis.sales.application.dto.request.billing

import java.math.BigDecimal
import java.time.LocalDate

/**
 * Create Demand Command
 *
 * Command to create a billing demand (청구서 생성 마감)
 *
 * @property custCd 고객코드
 * @property demandDt 청구일자
 * @property demandStartDt 청구시작일
 * @property demandEndDt 청구기준일(종료일)
 * @property stndPrice 청구수가 (기준가)
 * @property supval 공급가액
 * @property addtax 부가세액
 * @property dscntRate 할인율
 * @property exrtId 환율ID
 * @property insurePrice 보험가
 * @property sapCustCd SAP 고객코드
 * @property invcRecpEmailAddr 청구서 수신 이메일
 * @property demandMemo 청구 메모
 */
data class CreateDemandCommand(
    val custCd: String,
    val demandDt: LocalDate,
    val demandStartDt: LocalDate,
    val demandEndDt: LocalDate,
    val stndPrice: BigDecimal,
    val demandCharge: BigDecimal,
    val supval: BigDecimal,
    val addtax: BigDecimal,
    val dscntRate: BigDecimal = BigDecimal.ZERO,
    val exrtId: Long? = null,
    val stndExrt: BigDecimal? = null,
    val insurePrice: BigDecimal? = null,
    val sapCustCd: String? = null,
    val invcRecpEmailAddr: String? = null,
    val demandMemo: String? = null
)
