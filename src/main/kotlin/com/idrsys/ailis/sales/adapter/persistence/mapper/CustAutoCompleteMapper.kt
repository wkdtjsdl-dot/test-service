package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.CustCdNmAutoCompleteInfo
import com.idrsys.ailis.sales.application.dto.query.DirectAcctCdNmAutoCompleteInfo
import com.idrsys.ailis.sales.application.dto.query.RprsCustCdNmAutoCompleteInfo
import io.r2dbc.spi.Row

fun Row.toCustCdNmAutoCompleteInfo(): CustCdNmAutoCompleteInfo {
    return CustCdNmAutoCompleteInfo(
        custMstId = this.get("cust_mst_id", String::class.java),
        custCd = this.get("cust_cd", String::class.java),
        custNm = this.get("cust_nm", String::class.java)
    )
}

fun Row.toRprsCustCdNmAutoCompleteInfo(): RprsCustCdNmAutoCompleteInfo {
    return RprsCustCdNmAutoCompleteInfo(
        rprsCustCd = this.get("rprs_cust_cd", String::class.java),
        rprsCustNm = this.get("rprs_cust_nm", String::class.java)
    )
}

fun Row.toDirectAcctCdNmAutoCompleteInfo(): DirectAcctCdNmAutoCompleteInfo {
    return DirectAcctCdNmAutoCompleteInfo(
        directAcctCd = this.get("direct_acct_cd", String::class.java),
        directAcctNm = this.get("direct_acct_nm", String::class.java)
    )
}