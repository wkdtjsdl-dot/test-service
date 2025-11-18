package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.CustReqPossTstItemQuery
import io.r2dbc.spi.Row
import java.time.LocalDateTime

internal fun Row.toCustReqPossTstItemQuery(): CustReqPossTstItemQuery = CustReqPossTstItemQuery(
    custReqPossTstItemId = this.get("cust_req_poss_tst_item_id", Long::class.javaObjectType)!!,
    custMstId = this.get("cust_mst_id", String::class.java),
    custCd = this.get("cust_cd", String::class.java)!!,
    tstCd = this.get("tst_cd", String::class.java)!!,
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!
)
