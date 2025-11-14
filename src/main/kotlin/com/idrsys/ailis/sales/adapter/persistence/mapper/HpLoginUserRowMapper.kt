package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.HpLoginUserQuery
import io.r2dbc.spi.Row
import java.time.LocalDateTime

internal fun Row.toHpLoginUserQuery(): HpLoginUserQuery = HpLoginUserQuery(
    hpLoginUserId = this.get("hp_login_user_id", String::class.java)!!,
    custMstId = this.get("cust_mst_id", String::class.java)!!,
    custCd = this.get("cust_cd", String::class.java)!!,
    hpCustDiv = this.get("hp_cust_div", String::class.java),
    loginId = this.get("login_id", String::class.java)!!,
    loginPswd = this.get("login_pswd", String::class.java)!!,
    loginFailNum = this.get("login_fail_num", Int::class.javaObjectType),
    pswdChngDtime = this.get("pswd_chng_dtime", LocalDateTime::class.java),
    lastLoginDtime = this.get("last_login_dtime", LocalDateTime::class.java),
    loginNm = this.get("login_nm", String::class.java),
    loginPersonContact = this.get("login_person_contact", String::class.java),
    useYn = this.get("use_yn", Boolean::class.java)!!,
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)
