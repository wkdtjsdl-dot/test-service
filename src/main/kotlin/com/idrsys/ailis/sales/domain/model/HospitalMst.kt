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
    mapCodnX: Int?,
    mapCodnY: Int?,
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
        private set

    @Column("care_inst_no")
    var careInstNo: String? = careInstNo
        private set

    @Column("care_inst_nm")
    var careInstNm: String = careInstNm
        private set

    @Column("asrt_cd")
    var asrtCd: String? = asrtCd
        private set

    @Column("estb_div_nm")
    var estbDivNm: String? = estbDivNm
        private set

    @Column("sido_cd")
    var sidoCd: String? = sidoCd
        private set

    @Column("sido_nm")
    var sidoNm: String? = sidoNm
        private set

    @Column("sggu_cd")
    var sgguCd: String? = sgguCd
        private set

    @Column("sggu_nm")
    var sgguNm: String? = sgguNm
        private set

    @Column("emd")
    var emd: String? = emd
        private set

    @Column("zipcd")
    var zipcd: String? = zipcd
        private set

    @Column("addr")
    var addr: String? = addr
        private set

    @Column("telno")
    var telno: String? = telno
        private set

    @Column("hp_url")
    var hpUrl: String? = hpUrl
        private set

    @Column("open_dt")
    var openDt: String? = openDt
        private set

    @Column("close_dt")
    var closeDt: String? = closeDt
        private set

    @Column("dr_cnt")
    var drCnt: Int? = drCnt
        private set

    @Column("sickbed_cnt")
    var sickbedCnt: Int? = sickbedCnt
        private set

    @Column("map_codn_x")
    var mapCodnX: Int? = mapCodnX
        private set

    @Column("map_codn_y")
    var mapCodnY: Int? = mapCodnY
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

    override fun getId(): String = careInstId

    override fun isNew(): Boolean = _isNew
}