package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_hosp_device")
class HospitalDevice(
    hospDeviceId: Long? = null,
    careInstId: String?,
    deviceCd: String,
    deviceNm: String?,
    deviceCnt: Int?,
    useYn: Boolean,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime
) : Persistable<Long?> {

    @Id
    @Column("hosp_device_id")
    var hospDeviceId: Long? = hospDeviceId
        private set

    @Column("care_inst_id")
    var careInstId: String? = careInstId

    @Column("device_cd")
    var deviceCd: String = deviceCd

    @Column("device_nm")
    var deviceNm: String? = deviceNm

    @Column("device_cnt")
    var deviceCnt: Int? = deviceCnt

    @Column("use_yn")
    var useYn: Boolean = useYn

    @Column("creator")
    var creator: String = creator
        private set

    @Column("create_dtime")
    var createDtime: LocalDateTime = createDtime
        private set

    @Column("updater")
    var updater: String = updater

    @Column("update_dtime")
    var updateDtime: LocalDateTime = updateDtime

    override fun getId(): Long? = hospDeviceId

    override fun isNew(): Boolean = hospDeviceId == null
}
