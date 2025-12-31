package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.IfConfInfoQuery
import io.r2dbc.spi.Row
import java.time.LocalDateTime

internal fun Row.toIfConfInfoQuery(): IfConfInfoQuery = IfConfInfoQuery(
    ifConfInfoId = this.get("if_conf_info_id", String::class.java)!!,
    ifCustInfoId = this.get("if_cust_info_id", String::class.java)!!,
    ifFieldInfoId = this.get("if_field_info_id", String::class.java)!!,
    ifFieldNm = this.get("if_field_nm", String::class.java)!!,
    colIdx = this.get("col_idx", Integer::class.java)!!.toInt(),
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!
)
