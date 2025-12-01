package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt
import io.r2dbc.spi.Row
import java.time.LocalDateTime

internal fun Row.toHospitalMediSbjt(): HospitalMediSbjt = HospitalMediSbjt(
    hospMediSbjtId = this.get("hosp_medi_sbjt_id", Long::class.java),
    careInstId = this.get("care_inst_id", String::class.java),
    mediSbjtCd = this.get("medi_sbjt_cd", String::class.java)!!,
    mediSbjtNm = this.get("medi_sbjt_nm", String::class.java),
    mediSbjtMdspCnt = this.get("medi_sbjt_mdsp_cnt", Int::class.java),
    selcareDrCnt = this.get("selcare_dr_cnt", Integer::class.java) as Int?,
    useYn = this.get("use_yn", Boolean::class.java)!!,
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)
