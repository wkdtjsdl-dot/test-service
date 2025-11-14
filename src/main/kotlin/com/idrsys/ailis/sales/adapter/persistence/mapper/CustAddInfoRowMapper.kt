package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.CustAddInfoQuery
import io.r2dbc.spi.Row
import java.time.LocalDateTime

internal fun Row.toCustAddInfoQuery(): CustAddInfoQuery = CustAddInfoQuery(
    custAddInfoId = this.get("cust_add_info_id", Long::class.javaObjectType)!!,
    custMstId = this.get("cust_mst_id", String::class.java)!!,
    custCd = this.get("cust_cd", String::class.java)!!,
    spnoteDivCd = this.get("spnote_div_cd", String::class.java),
    spnote = this.get("spnote", String::class.java),
    useYn = this.get("use_yn", Boolean::class.java)!!,
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)
