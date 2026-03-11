package com.idrsys.ailis.sales.domain.model

import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserCommand
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_hp_login_user")
class HpLoginUser(
    hpLoginUserId: String? = null,
    custMstId: String,
    custCd: String,
    hpCustDiv: String?,
    loginId: String,
    loginPswd: String,
    loginFailNum: Int?,
    pswdChngDtime: LocalDateTime?,
    lastLoginDtime: LocalDateTime?,
    loginNm: String?,
    loginPersonContact: String?,
    useYn: Boolean,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<String> {

    @Id
    @Column("hp_login_user_id")
    val hpLoginUserId: String? = hpLoginUserId

    @Column("cust_mst_id")
    var custMstId: String = custMstId
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("hp_cust_div")
    var hpCustDiv: String? = hpCustDiv
        private set

    @Column("login_id")
    var loginId: String = loginId
        private set

    @Column("login_pswd")
    var loginPswd: String = loginPswd
        private set

    @Column("login_fail_num")
    var loginFailNum: Int? = loginFailNum
        private set

    @Column("pswd_chng_dtime")
    var pswdChngDtime: LocalDateTime? = pswdChngDtime
        private set

    @Column("last_login_dtime")
    var lastLoginDtime: LocalDateTime? = lastLoginDtime
        private set

    @Column("login_nm")
    var loginNm: String? = loginNm
        private set

    @Column("login_person_contact")
    var loginPersonContact: String? = loginPersonContact
        private set

    @Column("use_yn")
    var useYn: Boolean = useYn
        private set

    @Column("creator")
    val creator: String = creator

    @Column("create_dtime")
    val createDtime: LocalDateTime = createDtime

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

    override fun getId(): String? = hpLoginUserId

    override fun isNew(): Boolean = _isNew

    fun update(command: HpLoginUserCommand, updater: String) {
        this.custMstId = command.custMstId
        this.custCd = command.custCd
        this.hpCustDiv = command.hpCustDiv
        this.loginId = command.loginId
        this.loginPswd = command.loginPswd
        this.loginFailNum = command.loginFailNum
        this.pswdChngDtime = command.pswdChngDtime
        this.lastLoginDtime = command.lastLoginDtime
        this.loginNm = command.loginNm
        this.loginPersonContact = command.loginPersonContact
        this.useYn = command.useYn
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
