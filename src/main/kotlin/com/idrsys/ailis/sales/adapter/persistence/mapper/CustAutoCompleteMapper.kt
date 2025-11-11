package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.CustCdNmAutoCompleteInfo
import io.r2dbc.spi.Row

fun Row.toCustCdNmAutoCompleteInfo(): CustCdNmAutoCompleteInfo {
    return CustCdNmAutoCompleteInfo(
        custCd = this.get("cust_cd", String::class.java)!!,
        custNm = this.get("cust_nm", String::class.java)!!
    )
}
