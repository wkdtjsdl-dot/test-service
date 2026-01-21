package com.idrsys.ailis.sales.domain.model

import com.idrsys.ailis.sales.application.dto.request.ifCustInfo.IfCustInfoCommand
import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_if_cust_info")
class IfCustInfo(
    ifCustInfoId: String? = null,
    custMstId: String,
    custCd: String,
    headerInclYn: Boolean,
    skipRowCnt: Int?,
    ifDesc: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<String> {

    @Id
    @UuidGeneratedId(idFieldName = "ifCustInfoId")
    @Column("if_cust_info_id")
    val ifCustInfoId: String? = ifCustInfoId

    @Column("cust_mst_id")
    var custMstId: String = custMstId
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("header_incl_yn")
    var headerInclYn: Boolean = headerInclYn
        private set

    @Column("skip_row_cnt")
    var skipRowCnt: Int? = skipRowCnt
        private set

    @Column("if_desc")
    var ifDesc: String? = ifDesc
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

    override fun getId(): String? = ifCustInfoId

    override fun isNew(): Boolean = _isNew

    fun update(command: IfCustInfoCommand, updater: String) {
        this.headerInclYn = command.headerInclYn
        this.skipRowCnt = command.skipRowCnt
        this.ifDesc = command.ifDesc
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
