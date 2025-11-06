package com.idrsys.ailis.sales.domain.model

import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionCommand
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_sals_action")
class SalsAction(
    salsActionId: Long? = null,
    custMstId: String,
    custCd: String,
    visitDtime: LocalDateTime?,
    visitPrpsCd: String?,
    visitTargetPersonNm: String?,
    visitTargetPersonContact: String?,
    memo: String?,
    useYn: Boolean = true,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<Long> {

    @Id
    @Column("sals_action_id")
    val salsActionId: Long? = salsActionId

    @Column("cust_mst_id")
    var custMstId: String = custMstId
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("visit_dtime")
    var visitDtime: LocalDateTime? = visitDtime
        private set

    @Column("visit_prps_cd")
    var visitPrpsCd: String? = visitPrpsCd
        private set

    @Column("visit_target_person_nm")
    var visitTargetPersonNm: String? = visitTargetPersonNm
        private set

    @Column("visit_target_person_contact")
    var visitTargetPersonContact: String? = visitTargetPersonContact
        private set

    @Column("memo")
    var memo: String? = memo
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

    override fun getId(): Long? = salsActionId

    override fun isNew(): Boolean = _isNew

    fun update(command: SalsActionCommand, updater: String) {
        this.custMstId = command.custMstId
        this.custCd = command.custCd
        this.visitDtime = command.visitDtime
        this.visitPrpsCd = command.visitPrpsCd
        this.visitTargetPersonNm = command.visitTargetPersonNm
        this.visitTargetPersonContact = command.visitTargetPersonContact
        this.memo = command.memo
        this.useYn = command.useYn
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
