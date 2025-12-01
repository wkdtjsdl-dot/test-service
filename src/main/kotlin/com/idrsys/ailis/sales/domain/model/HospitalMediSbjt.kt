package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
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
) : Persistable<Long?> {

    @Id
    @Column("hosp_medi_sbjt_id")
    val hospMediSbjtId: Long? = hospMediSbjtId

    @Column("care_inst_id")
    var careInstId: String? = careInstId

    @Column("medi_sbjt_cd")
    var mediSbjtCd: String = mediSbjtCd

    @Column("medi_sbjt_nm")
    var mediSbjtNm: String? = mediSbjtNm

    @Column("medi_sbjt_mdsp_cnt")
    var mediSbjtMdspCnt: Int? = mediSbjtMdspCnt

    @Column("selcare_dr_cnt")
    var selcareDrCnt: Int? = selcareDrCnt

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

    override fun getId(): Long? = hospMediSbjtId

    override fun isNew(): Boolean = hospMediSbjtId == null
}
