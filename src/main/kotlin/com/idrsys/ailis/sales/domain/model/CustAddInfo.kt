package com.idrsys.ailis.sales.domain.model

import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoCommand
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.annotation.Transient
import java.time.LocalDateTime

@Table("sales_scm.scs_cust_add_info")
class CustAddInfo(
    custAddInfoId: Long? = null,
    custMstId: String,
    custCd: String,
    spnoteDivCd: String?,
    spnote: String?,
    useYn: Boolean = true,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<Long> {

    @Id
    @Column("cust_add_info_id")
    val custAddInfoId: Long? = custAddInfoId

    @Column("cust_mst_id")
    var custMstId: String = custMstId
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("spnote_div_cd")
    var spnoteDivCd: String? = spnoteDivCd
        private set

    @Column("spnote")
    var spnote: String? = spnote
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

    override fun getId(): Long? = custAddInfoId

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun isNew(): Boolean = _isNew

    fun update(command: CustAddInfoCommand, updater: String) {
        this.custMstId = command.custMstId
        this.custCd = command.custCd
        this.spnoteDivCd = command.spnoteDivCd
        this.spnote = command.spnote
        this.useYn = command.useYn
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
