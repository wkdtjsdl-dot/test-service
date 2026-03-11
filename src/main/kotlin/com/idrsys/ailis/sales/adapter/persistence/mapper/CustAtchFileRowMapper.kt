package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.CustAtchFileQuery
import io.r2dbc.spi.Row
import java.time.LocalDateTime

internal fun Row.toCustAtchFileQuery(): CustAtchFileQuery = CustAtchFileQuery(
    custAtchFileId = this.get("cust_atch_file_id", String::class.java)!!,
    custMstId = this.get("cust_mst_id", String::class.java)!!,
    custCd = this.get("cust_cd", String::class.java)!!,
    atchGrupId = this.get("atch_grup_id", String::class.java)!!,
    useYn = this.get("use_yn", Boolean::class.java)!!,
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)
