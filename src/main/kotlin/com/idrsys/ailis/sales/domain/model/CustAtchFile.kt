package com.idrsys.ailis.sales.domain.model

import com.idrsys.ailis.sales.application.dto.request.custatchfile.CustAtchFileCommand
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_cust_atch_file")
class CustAtchFile(
    custAtchFileId: String? = null,
    custMstId: String,
    custCd: String,
    atchGrupId: String,
    useYn: Boolean = true,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<String> {

    @Id
    @Column("cust_atch_file_id")
    val custAtchFileId: String? = custAtchFileId

    @Column("cust_mst_id")
    var custMstId: String = custMstId
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("atch_grup_id")
    var atchGrupId: String = atchGrupId
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

    override fun getId(): String? = custAtchFileId

    override fun isNew(): Boolean = _isNew

    fun update(command: CustAtchFileCommand, updater: String) {
        this.custMstId = command.custMstId
        this.custCd = command.custCd
        this.atchGrupId = command.atchGrupId
        this.useYn = command.useYn
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
