package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.response.UnsettledDemandSummary
import io.r2dbc.spi.Row
import java.math.BigDecimal
import java.time.LocalDate

internal fun Row.toUnsettledDemandSummary(): UnsettledDemandSummary = UnsettledDemandSummary(
    custCd = this.get("cust_cd", String::class.java)!!,
    custNm = this.get("cust_nm", String::class.java),
    branchNm = this.get("branch_nm", String::class.java),
    demandStndDt = this.get("demand_stnd_dt", LocalDate::class.java)!!,
    stndPrice = this.get("stnd_price", BigDecimal::class.java)!!,
    supval = this.get("supval", BigDecimal::class.java)!!,
    addtax = this.get("addtax", BigDecimal::class.java)!!,
    demandCharge = this.get("demand_charge", BigDecimal::class.java)!!,
    requestCount = this.get("request_count", Integer::class.java)!!.toInt()
)