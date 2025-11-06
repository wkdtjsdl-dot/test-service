package com.idrsys.ailis.sales.domain.model

import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoCommand
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("sales_scm.scs_gcgn_sals_pic_info")
class GcgnSalsPicInfo(
    gcgnSalsPicInfoId: Long? = null,
    custMstId: String,
    applyStartDt: LocalDate,
    salsTeamCd: String,
    empno: String,
    custCd: String,
    applyEndDt: LocalDate?,
    useYn: Boolean = true,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<Long> {

    @Id
    @Column("gcgn_sals_pic_info_id")
    val gcgnSalsPicInfoId: Long? = gcgnSalsPicInfoId

    @Column("cust_mst_id")
    var custMstId: String = custMstId
        private set

    @Column("apply_start_dt")
    var applyStartDt: LocalDate = applyStartDt
        private set

    @Column("sals_team_cd")
    var salsTeamCd: String = salsTeamCd
        private set

    @Column("empno")
    var empno: String = empno
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("apply_end_dt")
    var applyEndDt: LocalDate? = applyEndDt
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

    override fun getId(): Long? = gcgnSalsPicInfoId

    override fun isNew(): Boolean = _isNew

    fun update(command: GcgnSalsPicInfoCommand, updater: String) {
        this.custMstId = command.custMstId
        this.applyStartDt = command.applyStartDt
        this.salsTeamCd = command.salsTeamCd
        this.empno = command.empno
        this.custCd = command.custCd
        this.applyEndDt = command.applyEndDt
        this.useYn = command.useYn
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
