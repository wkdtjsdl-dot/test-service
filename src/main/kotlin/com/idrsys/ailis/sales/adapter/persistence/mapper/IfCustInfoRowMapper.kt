package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.IfCustInfoQuery
import com.idrsys.ailis.sales.domain.model.IfCustInfo
import io.r2dbc.spi.Row
import java.time.LocalDateTime

internal fun Row.toIfCustInfo(): IfCustInfo = IfCustInfo(
    ifCustInfoId = this.get("if_cust_info_id", String::class.java),
    custMstId = this.get("cust_mst_id", String::class.java)!!,
    custCd = this.get("cust_cd", String::class.java)!!,
    headerInclYn = this.get("header_incl_yn", Boolean::class.java)!!,
    skipRowCnt = this.get("skip_row_cnt", Integer::class.java)?.toInt(),
    ifDesc = this.get("if_desc", String::class.java),
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)

internal fun Row.toIfCustInfoQuery(): IfCustInfoQuery = IfCustInfoQuery(
    ifCustInfoId = this.get("if_cust_info_id", String::class.java)!!,
    custMstId = this.get("cust_mst_id", String::class.java)!!,
    custCd = this.get("cust_cd", String::class.java)!!,
    custNm = this.get("cust_nm", String::class.java),
    headerInclYn = this.get("header_incl_yn", Boolean::class.java)!!,
    skipRowCnt = this.get("skip_row_cnt", Integer::class.java)?.toInt(),
    ifDesc = this.get("if_desc", String::class.java),
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)
