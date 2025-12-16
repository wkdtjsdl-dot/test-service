package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.CustBasicInfo
import io.r2dbc.spi.Row

/**
 * Maps a [Row] to a [CustBasicInfo] object.
 */
internal fun Row.toCustBasicInfo(): CustBasicInfo {
    return CustBasicInfo(
        custMstId = this.get("cust_mst_id", String::class.java),
        custCd = this.get("cust_cd", String::class.java)!!,
        custNm = this.get("cust_nm", String::class.java)!!,
        telNo = this.get("tel_no", String::class.java),
        branchCd = this.get("branch_cd", String::class.java),
        bzoffiCd = this.get("bzoffi_cd", String::class.java)
    )
}
