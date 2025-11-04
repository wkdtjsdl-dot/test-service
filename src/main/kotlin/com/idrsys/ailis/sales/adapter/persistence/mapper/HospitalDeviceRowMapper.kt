package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.domain.model.HospitalDevice
import io.r2dbc.spi.Row
import java.time.LocalDateTime

internal fun Row.toHospitalDevice(): HospitalDevice = HospitalDevice(
    hospDeviceId = this.get("hosp_device_id", Long::class.java),
    careInstId = this.get("care_inst_id", String::class.java),
    deviceCd = this.get("device_cd", String::class.java)!!,
    deviceNm = this.get("device_nm", String::class.java),
    deviceCnt = this.get("device_cnt", Int::class.java),
    useYn = this.get("use_yn", Boolean::class.java)!!,
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)
