package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_hosp_medi_sbjt")
class HospitalMediSbjt(
    hospMediSbjtId: Long? = null,
    careInstId: String?,
    mediSbjtCd: String,
    mediSbjtNm: String?,
    mediSbjtMdspCnt: Int?,
    selcareDrCnt: Int?,
    useYn: Boolean,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime
) : Persistable<Long> {

    @Id
    @Column("hosp_medi_sbjt_id")
    val hospMediSbjtId: Long? = hospMediSbjtId

    @Column("care_inst_id")
    var careInstId: String? = careInstId
        private set

    @Column("medi_sbjt_cd")
    var mediSbjtCd: String = mediSbjtCd
        private set

    @Column("medi_sbjt_nm")
    var mediSbjtNm: String? = mediSbjtNm
        private set

    @Column("medi_sbjt_mdsp_cnt")
    var mediSbjtMdspCnt: Int? = mediSbjtMdspCnt
        private set

    @Column("selcare_dr_cnt")
    var selcareDrCnt: Int? = selcareDrCnt
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

    override fun getId(): Long = hospMediSbjtId!!

    override fun isNew(): Boolean = _isNew
}