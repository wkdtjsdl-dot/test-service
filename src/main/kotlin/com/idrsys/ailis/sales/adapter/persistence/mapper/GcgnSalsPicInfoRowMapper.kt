package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.domain.model.GcgnSalsPicInfo
import io.r2dbc.spi.Row
import java.time.LocalDate
import java.time.LocalDateTime

internal fun Row.toGcgnSalsPicInfo(): GcgnSalsPicInfo = GcgnSalsPicInfo(
    gcgnSalsPicInfoId = this.get("gcgn_sals_pic_info_id", Long::class.java),
    custMstId = this.get("cust_mst_id", String::class.java)!!,
    applyStartDt = this.get("apply_start_dt", LocalDate::class.java)!!,
    salsTeamCd = this.get("sals_team_cd", String::class.java)!!,
    empUserId = this.get("emp_user_id", String::class.java)!!,
    custCd = this.get("cust_cd", String::class.java)!!,
    applyEndDt = this.get("apply_end_dt", LocalDate::class.java),
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)
