package com.idrsys.ailis.sales.domain.model

import com.idrsys.ailis.sales.application.dto.request.charge.ChargeUpdateCommand
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("sales_scm.scs_cust_charge")
class Charge(
    custChargeId: String,
    custMstId: String?,
    custCd: String,
    applyStartDt: LocalDate,
    applyEndDt: LocalDate,
    tstCd: String,
    crcyCd: String,
    stndPrice: Long?,
    specialCharge: Long,
    supval: Long?,
    addtax: Long?,
    remark: String?,
    apprInfoNo: String? = null,
    currApprSeq: Int? = null,
    apprSubmsEmpNo: String? = null,
    apprSubmsDtime: LocalDateTime? = null,
    lastApprStatCd: String,
    apprLvlCd: String? = null,
    creator: String,
    createDtime: LocalDateTime = LocalDateTime.now(),
    updater: String,
    updateDtime: LocalDateTime = LocalDateTime.now(),
) : Persistable<String> {

    @Id
    @Column("cust_charge_id")
    val custChargeId: String = custChargeId

    @Column("cust_mst_id")
    var custMstId: String? = custMstId
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("apply_start_dt")
    var applyStartDt: LocalDate = applyStartDt
        private set

    @Column("apply_end_dt")
    var applyEndDt: LocalDate = applyEndDt
        private set

    @Column("tst_cd")
    var tstCd: String = tstCd
        private set

    @Column("crcy_cd")
    var crcyCd: String = crcyCd
        private set

    @Column("stnd_price")
    var stndPrice: Long? = stndPrice
        private set

    @Column("special_charge")
    var specialCharge: Long = specialCharge
        private set

    @Column("supval")
    var supval: Long? = supval
        private set

    @Column("addtax")
    var addtax: Long? = addtax
        private set

    @Column("remark")
    var remark: String? = remark
        private set

    @Column("appr_info_no")
    var apprInfoNo: String? = apprInfoNo
        private set

    @Column("curr_appr_seq")
    var currApprSeq: Int? = currApprSeq
        private set

    @Column("appr_subms_emp_no")
    var apprSubmsEmpNo: String? = apprSubmsEmpNo
        private set

    @Column("appr_subms_dtime")
    var apprSubmsDtime: LocalDateTime? = apprSubmsDtime
        private set

    @Column("last_appr_stat_cd")
    var lastApprStatCd: String = lastApprStatCd
        private set

    @Column("appr_lvl_cd")
    var apprLvlCd: String? = apprLvlCd
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

    override fun getId(): String = custChargeId

    override fun isNew(): Boolean = _isNew

    fun update(command: ChargeUpdateCommand, updater: String) {
        this.applyEndDt = command.applyEndDt
        this.crcyCd = command.crcyCd
        this.specialCharge = command.specialCharge
        this.supval = command.supval
        this.addtax = command.addtax
        this.remark = command.remark
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }

    /**
     * 승인 요청 (임시저장 → 결재중)
     */
    fun requestApproval(
        apprInfoNo: Long,
        apprSubmsEmpNo: String,
        apprLvlCd: String,
        updater: String
    ) {
        this.apprInfoNo = apprInfoNo.toString()
        this.currApprSeq = 1
        this.apprSubmsEmpNo = apprSubmsEmpNo
        this.apprSubmsDtime = LocalDateTime.now()
        this.lastApprStatCd = "LAST_I"  // 결재중
        this.apprLvlCd = apprLvlCd
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }

    /**
     * 결재 진행 중 (다음 결재자로 이동)
     */
    fun proceedToNextApprover(updater: String) {
        this.currApprSeq = (this.currApprSeq ?: 0) + 1
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }

    /**
     * 결재 완료
     */
    fun completeApproval(updater: String) {
        this.lastApprStatCd = "LAST_C"  // 결재완료
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }

}
