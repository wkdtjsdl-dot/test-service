package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_appr_info")
class ApprInfo(
    apprInfoId: String,
    apprInfoNo: Long,
    apprSeq: Int,
    apprDocTypeCd: String,
    apprPersonEmpNo: String,
    apprStatCd: String,
    apprCmplDtime: LocalDateTime? = null,
    apprMemo: String? = null,
    creator: String,
    createDtime: LocalDateTime = LocalDateTime.now(),
    updater: String,
    updateDtime: LocalDateTime = LocalDateTime.now(),
) : Persistable<String> {

    @Id
    @Column("appr_info_id")
    val apprInfoId: String = apprInfoId

    @Column("appr_info_no")
    var apprInfoNo: Long = apprInfoNo
        private set

    @Column("appr_seq")
    var apprSeq: Int = apprSeq
        private set

    @Column("appr_doc_type_cd")
    var apprDocTypeCd: String = apprDocTypeCd
        private set

    @Column("appr_person_emp_no")
    var apprPersonEmpNo: String = apprPersonEmpNo
        private set

    @Column("appr_stat_cd")
    var apprStatCd: String = apprStatCd
        private set

    @Column("appr_cmpl_dtime")
    var apprCmplDtime: LocalDateTime? = apprCmplDtime
        private set

    @Column("appr_memo")
    var apprMemo: String? = apprMemo
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

    override fun getId(): String = apprInfoId

    override fun isNew(): Boolean = _isNew

    /**
     * 승인 처리
     */
    fun approve(apprMemo: String?, updater: String) {
        this.apprStatCd = "APST_C"  // 결재완료
        this.apprCmplDtime = LocalDateTime.now()
        this.apprMemo = apprMemo
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }

    /**
     * 반려 처리
     */
    fun reject(apprMemo: String?, updater: String) {
        this.apprStatCd = "APST_R"  // 반려
        this.apprCmplDtime = LocalDateTime.now()
        this.apprMemo = apprMemo
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
