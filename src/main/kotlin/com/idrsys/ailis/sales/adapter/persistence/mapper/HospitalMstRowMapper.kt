package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.domain.model.HospitalMst
import io.r2dbc.spi.Row
import java.time.LocalDateTime

internal fun Row.toHospitalMst(): HospitalMst = HospitalMst(
    careInstId = this.get("care_inst_id", String::class.java)!!,
    encpCareInstNo = this.get("encp_care_inst_no", String::class.java)!!,
    careInstNo = this.get("care_inst_no", String::class.java),
    careInstNm = this.get("care_inst_nm", String::class.java)!!,
    asrtCd = this.get("asrt_cd", String::class.java),
    estbDivNm = this.get("estb_div_nm", String::class.java),
    sidoCd = this.get("sido_cd", String::class.java),
    sidoNm = this.get("sido_nm", String::class.java),
    sgguCd = this.get("sggu_cd", String::class.java),
    sgguNm = this.get("sggu_nm", String::class.java),
    emd = this.get("emd", String::class.java),
    zipcd = this.get("zipcd", String::class.java),
    addr = this.get("addr", String::class.java),
    telno = this.get("telno", String::class.java),
    hpUrl = this.get("hp_url", String::class.java),
    openDt = this.get("open_dt", String::class.java),
    closeDt = this.get("close_dt", String::class.java),
    drCnt = this.get("dr_cnt", Integer::class.java)?.toInt(),
    sickbedCnt = this.get("sickbed_cnt", Integer::class.java)?.toInt(),
    mapCodnX = this.get("map_codn_x", Double::class.java)?.toInt(),
    mapCodnY = this.get("map_codn_y", Double::class.java)?.toInt(),
    useYn = this.get("use_yn", Boolean::class.java)!!,
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)