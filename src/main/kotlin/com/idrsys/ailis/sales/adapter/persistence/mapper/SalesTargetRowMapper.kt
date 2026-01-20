package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.SalesTargetDetailQuery
import com.idrsys.ailis.sales.application.dto.query.SalesTargetQuery
import com.idrsys.ailis.sales.domain.model.SalesTarget
import io.r2dbc.spi.Row
import java.math.BigDecimal
import java.time.LocalDateTime

internal fun Row.toSalesTarget(): SalesTarget = SalesTarget(
    salesTargetId = this.get("sales_target_id", String::class.java),
    custCd = this.get("cust_cd", String::class.java)!!,
    salesYear = this.get("sales_year", String::class.java)!!,
    salesMonth = this.get("sales_month", String::class.java)!!,
    salsTeamCd = this.get("sals_team_cd", String::class.java)!!,
    monthSalesTargetAmt = this.get("month_sales_target_amt", BigDecimal::class.java) ?: BigDecimal.ZERO,
    pastYearMonthSalesAmt = this.get("past_year_month_sales_amt", BigDecimal::class.java) ?: BigDecimal.ZERO,
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)

internal fun Row.toSalesTargetQuery(): SalesTargetQuery = SalesTargetQuery(
    salesYear = this.get("sales_year", String::class.java)!!,
    custCd = this.get("cust_cd", String::class.java)!!,
    custNm = this.get("cust_nm", String::class.java)!!,
    salsTeamCd = this.get("sals_team_cd", String::class.java)!!,
    salsTeamNm = this.get("sals_team_nm", String::class.java),
    totalTarget = this.get("total_target", BigDecimal::class.java) ?: BigDecimal.ZERO,
    prevYearSales = this.get("prev_year_sales", BigDecimal::class.java) ?: BigDecimal.ZERO
)

internal fun Row.toSalesTargetDetailQuery(): SalesTargetDetailQuery = SalesTargetDetailQuery(
    salesYear = this.get("sales_year", String::class.java)!!,
    salesMonth = this.get("sales_month", String::class.java)!!,
    custCd = this.get("cust_cd", String::class.java)!!,
    custNm = this.get("cust_nm", String::class.java)!!,
    salsTeamCd = this.get("sals_team_cd", String::class.java)!!,
    salsTeamNm = this.get("sals_team_nm", String::class.java),
    monthlyTarget = this.get("monthly_target", BigDecimal::class.java) ?: BigDecimal.ZERO,
    prevYearSales = this.get("prev_year_sales", BigDecimal::class.java) ?: BigDecimal.ZERO
)
