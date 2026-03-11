package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.CustContactQuery
import io.r2dbc.spi.Row
import java.time.LocalDateTime

internal fun Row.toCustContactQuery(): CustContactQuery {
    return CustContactQuery(
        custContactId = this.get("cust_contact_id", Long::class.java)!!,
        custMstId = this.get("cust_mst_id", String::class.java)!!,
        custCd = this.get("cust_cd", String::class.java)!!,
        acctChargeNm = this.get("acct_charge_nm", String::class.java),
        ofpoJbpo = this.get("ofpo_jbpo", String::class.java),
        telno = this.get("telno", String::class.java),
        phno = this.get("phno", String::class.java),
        email = this.get("email", String::class.java),
        remark = this.get("remark", String::class.java),
        useYn = this.get("use_yn", Boolean::class.java)!!,
        creator = this.get("creator", String::class.java)!!,
        createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
        updater = this.get("updater", String::class.java)!!,
        updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!,
    )
}
