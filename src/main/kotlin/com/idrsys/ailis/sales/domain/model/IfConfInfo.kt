package com.idrsys.ailis.sales.domain.model

import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_if_conf_info")
class IfConfInfo(
    ifConfInfoId: String? = null,
    ifCustInfoId: String,
    ifFieldInfoId: String,
    colIdx: Int,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<String> {

    @Id
    @UuidGeneratedId(idFieldName = "ifConfInfoId")
    @Column("if_conf_info_id")
    val ifConfInfoId: String? = ifConfInfoId

    @Column("if_cust_info_id")
    var ifCustInfoId: String = ifCustInfoId
        private set

    @Column("if_field_info_id")
    var ifFieldInfoId: String = ifFieldInfoId
        private set

    @Column("col_idx")
    var colIdx: Int = colIdx
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

    override fun getId(): String? = ifConfInfoId

    override fun isNew(): Boolean = _isNew
}
