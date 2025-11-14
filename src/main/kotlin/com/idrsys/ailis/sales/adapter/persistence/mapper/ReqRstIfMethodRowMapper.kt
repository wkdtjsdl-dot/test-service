package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.ReqRstIfMethodQuery
import io.r2dbc.spi.Row
import java.time.LocalDate
import java.time.LocalDateTime

internal fun Row.toReqRstIfMethodQuery(): ReqRstIfMethodQuery = ReqRstIfMethodQuery(
    reqRstIfMethodId = this.get("req_rst_if_method_id", String::class.java)!!,
    custMstId = this.get("cust_mst_id", String::class.java),
    applyStartDt = this.get("apply_start_dt", LocalDate::class.java)!!,
    custCd = this.get("cust_cd", String::class.java)!!,
    applyEndDt = this.get("apply_end_dt", LocalDate::class.java),
    reqMethodCd = this.get("req_method_cd", String::class.java)!!,
    reqIfTypeCd = this.get("req_if_type_cd", String::class.java),
    useYn = this.get("use_yn", Boolean::class.java)!!,
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)
