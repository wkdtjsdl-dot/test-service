package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.domain.model.SalsAction
import io.r2dbc.spi.Row
import java.time.LocalDateTime

internal fun Row.toSalsAction(): SalsAction = SalsAction(
    salsActionId = this.get("sals_action_id", Long::class.java),
    custMstId = this.get("cust_mst_id", String::class.java)!!,
    custCd = this.get("cust_cd", String::class.java)!!,
    visitDtime = this.get("visit_dtime", LocalDateTime::class.java),
    visitPrpsCd = this.get("visit_prps_cd", String::class.java),
    visitTargetPersonNm = this.get("visit_target_person_nm", String::class.java),
    visitTargetPersonContact = this.get("visit_target_person_contact", String::class.java),
    memo = this.get("memo", String::class.java),
    useYn = this.get("use_yn", Boolean::class.java)!!,
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)
