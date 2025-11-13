package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.CustWithSalsPicInfo
import io.r2dbc.spi.Row
import java.time.LocalDateTime

/**
 * Maps a [Row] to a [CustWithSalsPicInfo] object.
 */
internal fun Row.toCustWithSalsPicInfo(): CustWithSalsPicInfo {
    return CustWithSalsPicInfo(
        custMstId = this.get("cust_mst_id", String::class.java)!!,
        custCd = this.get("cust_cd", String::class.java)!!,
        custNm = this.get("cust_nm", String::class.java)!!,
        bzoffiCd = this.get("bzoffi_cd", String::class.java),
        custDivCd = this.get("cust_div_cd", String::class.java)!!,
        custTypeCd = this.get("cust_type_cd", String::class.java)!!,
        rprsCustCd = this.get("rprs_cust_cd", String::class.java),
        rprsCustNm = this.get("rprs_cust_nm", String::class.java),
        bizrno = this.get("bizrno", String::class.java),
        careInstNo = this.get("care_inst_no", String::class.java),
        sapCustCd = this.get("sap_cust_cd", String::class.java),
        salsPicInfo = this.get("sals_pic_info", String::class.java), // Reads the aggregated string
        custStatCd = this.get("cust_stat_cd", String::class.java)!!,
        createDtime = this.get("create_dtime", LocalDateTime::class.java)!!
    )
}
