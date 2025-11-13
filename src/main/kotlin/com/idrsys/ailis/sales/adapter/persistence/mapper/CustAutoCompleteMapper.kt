package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.CustAutoCompleteInfo
import io.r2dbc.spi.Row

fun Row.toCustCdNmAutoCompleteInfo(): CustAutoCompleteInfo {
    return CustAutoCompleteInfo(
        custCd = this.get("cust_cd", String::class.java),
        custNm = this.get("cust_nm", String::class.java),
        rprsCustCd = this.get("rprs_cust_cd", String::class.java),
        rprsCustNm = this.get("rprs_cust_nm", String::class.java)
    )
}
