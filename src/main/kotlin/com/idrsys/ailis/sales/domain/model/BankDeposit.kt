package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * BankDeposit (은행입금정보) Value Object
 *
 * Responsibilities:
 * - Store bank deposit information received from ERP
 * - Guarantee immutability of bank deposit data
 *
 * Business Rules:
 * - Bank deposit data is immutable once created
 * - Outstanding amount decreases as deposits are allocated to customers
 * - Registration flag indicates if mapped to customer
 */
@Table("sales_scm.sbl_bank_deposit")
data class BankDeposit(
    @Id
    @Column("bank_deposit_id")
    val bankDepositId: String,

    @Column("account_div_cd")
    val accountDivCd: String? = null,

    @Column("account_no")
    val accountNo: String? = null,

    @Column("account_year")
    val accountYear: String? = null,

    @Column("surecp_slstmt_no")
    val surecpSlstmtNo: String? = null,

    @Column("deposit_dt")
    val depositDt: LocalDate,

    @Column("deposit_amt")
    val depositAmt: BigDecimal,

    @Column("outamt")
    val outamt: BigDecimal? = null,

    @Column("crcy_cd")
    val crcyCd: String? = null,

    @Column("remark")
    val remark: String? = null,

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
)
