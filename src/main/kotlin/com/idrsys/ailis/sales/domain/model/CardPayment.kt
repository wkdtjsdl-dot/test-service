package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * CardPayment (카드결제정보) Value Object
 *
 * Responsibilities:
 * - Store card approval information received from VAN
 * - Guarantee immutability of card payment data
 *
 * Business Rules:
 * - Card payment data is immutable once created
 * - Registration flag (regYn) indicates if mapped to customer
 * - Payment division "10" = approved, "20" = cancelled
 */
@Table("sales_scm.sbl_card_pay")
data class CardPayment(
    @Id
    @Column("card_pay_id")
    val cardPayId: String,

    @Column("shop_id")
    val shopId: String,

    @Column("trade_no")
    val tradeNo: String,

    @Column("card_bill_no")
    val cardBillNo: String? = null,

    @Column("card_comp_cd")
    val cardCompCd: String? = null,

    @Column("card_comp_nm")
    val cardCompNm: String? = null,

    @Column("card_no")
    val cardNo: String? = null,

    @Column("instl_month")
    val instlMonth: String? = null,

    @Column("pay_amt")
    val payAmt: BigDecimal,

    @Column("pay_dt")
    val payDt: String,

    @Column("pay_time")
    val payTime: String? = null,

    @Column("card_appr_no")
    val cardApprNo: String? = null,

    @Column("pay_div_cd")
    val payDivCd: String,

    @Column("reg_yn")
    val regYn: Boolean = false,

    @Column("creator")
    val creator: String,

    @Column("create_dtime")
    val createDtime: LocalDateTime,

    @Column("updater")
    val updater: String,

    @Column("update_dtime")
    val updateDtime: LocalDateTime
) {
    companion object {
        const val PAY_DIV_APPROVED = "10"  // 승인
        const val PAY_DIV_CANCELLED = "20"  // 취소
    }
}
