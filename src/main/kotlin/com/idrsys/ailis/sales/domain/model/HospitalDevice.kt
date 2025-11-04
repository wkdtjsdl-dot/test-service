package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
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
) : Persistable<Long> {

    @Id
    @Column("hosp_device_id")
    val hospDeviceId: Long? = hospDeviceId

    @Column("care_inst_id")
    var careInstId: String? = careInstId
        private set

    @Column("device_cd")
    var deviceCd: String = deviceCd
        private set

    @Column("device_nm")
    var deviceNm: String? = deviceNm
        private set

    @Column("device_cnt")
    var deviceCnt: Int? = deviceCnt
        private set

    @Column("use_yn")
    var useYn: Boolean = useYn
        private set

    @Column("creator")
    var creator: String = creator
        private set

    @Column("create_dtime")
    var createDtime: LocalDateTime = createDtime
        private set

    @Column("updater")
    var updater: String = updater
        private set

    @Column("update_dtime")
    var updateDtime: LocalDateTime = updateDtime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): Long = hospDeviceId!!

    override fun isNew(): Boolean = _isNew
}