package com.idrsys.ailis.sales.domain.model

import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * 고객매출목표 (Customer Sales Target)
 *
 * 월별 고객 매출 목표를 관리하는 도메인 모델
 */
@Table("sales_scm.sbl_sales_target")
class SalesTarget(
    salesTargetId: String? = null,
    custCd: String,
    salesYear: String,
    salesMonth: String,
    salsTeamCd: String,
    monthSalesTargetAmt: BigDecimal = BigDecimal.ZERO,
    pastYearMonthSalesAmt: BigDecimal = BigDecimal.ZERO,
    creator: String,
    createDtime: LocalDateTime = LocalDateTime.now(),
    updater: String,
    updateDtime: LocalDateTime = LocalDateTime.now()
) : Persistable<String> {

    @Id
    @UuidGeneratedId("salesTargetId")
    @Column("sales_target_id")
    val salesTargetId: String? = salesTargetId

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("sales_year")
    var salesYear: String = salesYear
        private set

    @Column("sales_month")
    var salesMonth: String = salesMonth
        private set

    @Column("sals_team_cd")
    var salsTeamCd: String = salsTeamCd
        private set

    @Column("month_sales_target_amt")
    var monthSalesTargetAmt: BigDecimal = monthSalesTargetAmt
        private set

    @Column("past_year_month_sales_amt")
    var pastYearMonthSalesAmt: BigDecimal = pastYearMonthSalesAmt
        private set

    @Column("creator")
    var creator: String = creator
        private set

    @Column("create_dtime")
    var createDtime: LocalDateTime = createDtime
        private set

    @Column("updater")
    var updater: String = updater
        private set

    @Column("update_dtime")
    var updateDtime: LocalDateTime = updateDtime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = salesTargetId

    override fun isNew(): Boolean = _isNew

    /**
     * 매출 목표 정보 수정
     */
    fun update(
        monthSalesTargetAmt: BigDecimal,
        pastYearMonthSalesAmt: BigDecimal,
        updater: String
    ) {
        this.monthSalesTargetAmt = monthSalesTargetAmt
        this.pastYearMonthSalesAmt = pastYearMonthSalesAmt
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
