package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_if_field_info")
class IfFieldInfo(
    ifFieldInfoId: String? = null,
    ifFieldNm: String,
    ifFieldColNm: String?,
    ifFieldExps: String?,
    ifFieldDesc: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<String> {

    @Id
    @Column("if_field_info_id")
    val ifFieldInfoId: String? = ifFieldInfoId

    @Column("if_field_nm")
    var ifFieldNm: String = ifFieldNm
        private set

    @Column("if_field_col_nm")
    var ifFieldColNm: String? = ifFieldColNm
        private set

    @Column("if_field_exps")
    var ifFieldExps: String? = ifFieldExps
        private set

    @Column("if_field_desc")
    var ifFieldDesc: String? = ifFieldDesc
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

    override fun getId(): String? = ifFieldInfoId

    override fun isNew(): Boolean = _isNew
}
