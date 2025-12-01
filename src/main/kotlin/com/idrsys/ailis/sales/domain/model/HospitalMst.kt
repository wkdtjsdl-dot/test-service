package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_hosp_mst")
class HospitalMst(
    careInstId: String,
    encpCareInstNo: String,
    careInstNo: String?,
    careInstNm: String,
    asrtCd: String?,
    estbDivNm: String?,
    sidoCd: String?,
    sidoNm: String?,
    sgguCd: String?,
    sgguNm: String?,
    emd: String?,
    zipcd: String?,
    addr: String?,
    telno: String?,
    hpUrl: String?,
    openDt: String?,
    closeDt: String?,
    drCnt: Int?,
    sickbedCnt: Int?,
    mapCodnX: Double?,
    mapCodnY: Double?,
    useYn: Boolean,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("care_inst_id")
    val careInstId: String = careInstId

    @Column("encp_care_inst_no")
    var encpCareInstNo: String = encpCareInstNo

    @Column("care_inst_no")
    var careInstNo: String? = careInstNo

    @Column("care_inst_nm")
    var careInstNm: String = careInstNm

    @Column("asrt_cd")
    var asrtCd: String? = asrtCd

    @Column("estb_div_nm")
    var estbDivNm: String? = estbDivNm

    @Column("sido_cd")
    var sidoCd: String? = sidoCd

    @Column("sido_nm")
    var sidoNm: String? = sidoNm

    @Column("sggu_cd")
    var sgguCd: String? = sgguCd

    @Column("sggu_nm")
    var sgguNm: String? = sgguNm

    @Column("emd")
    var emd: String? = emd

    @Column("zipcd")
    var zipcd: String? = zipcd

    @Column("addr")
    var addr: String? = addr

    @Column("telno")
    var telno: String? = telno

    @Column("hp_url")
    var hpUrl: String? = hpUrl

    @Column("open_dt")
    var openDt: String? = openDt

    @Column("close_dt")
    var closeDt: String? = closeDt

    @Column("dr_cnt")
    var drCnt: Int? = drCnt

    @Column("sickbed_cnt")
    var sickbedCnt: Int? = sickbedCnt

    @Column("map_codn_x")
    var mapCodnX: Double? = mapCodnX

    @Column("map_codn_y")
    var mapCodnY: Double? = mapCodnY

    @Column("use_yn")
    var useYn: Boolean = useYn

    @Column("creator")
    var creator: String = creator

    @Column("create_dtime")
    var createDtime: LocalDateTime = createDtime

    @Column("updater")
    var updater: String = updater

    @Column("update_dtime")
    var updateDtime: LocalDateTime = updateDtime

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String = careInstId

    override fun isNew(): Boolean = _isNew
}