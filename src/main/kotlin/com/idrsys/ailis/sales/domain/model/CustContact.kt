package com.idrsys.ailis.sales.domain.model

import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactCommand
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_cust_contact")
class CustContact(
    custContactId: Long? = null,
    custMstId: String,
    custCd: String,
    acctChargeNm: String?,
    ofpoJbpo: String?,
    telno: String?,
    phno: String?,
    email: String?,
    remark: String?,
    useYn: Boolean = true,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<Long> {

    @Id
    @Column("cust_contact_id")
    val custContactId: Long? = custContactId

    @Column("cust_mst_id")
    var custMstId: String = custMstId
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("acct_charge_nm")
    var acctChargeNm: String? = acctChargeNm
        private set

    @Column("ofpo_jbpo")
    var ofpoJbpo: String? = ofpoJbpo
        private set

    @Column("telno")
    var telno: String? = telno
        private set

    @Column("phno")
    var phno: String? = phno
        private set

    @Column("email")
    var email: String? = email
        private set

    @Column("remark")
    var remark: String? = remark
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

    override fun getId(): Long? = custContactId

    override fun isNew(): Boolean = _isNew

    fun update(command: CustContactCommand, updater: String) {
        this.custMstId = command.custMstId
        this.custCd = command.custCd
        this.acctChargeNm = command.acctChargeNm
        this.ofpoJbpo = command.ofpoJbpo
        this.telno = command.telno
        this.phno = command.phno
        this.email = command.email
        this.remark = command.remark
        this.useYn = command.useYn
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
