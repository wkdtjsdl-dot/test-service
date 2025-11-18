package com.idrsys.ailis.sales.domain.model

import com.idrsys.ailis.sales.application.dto.request.reqrstifmethod.ReqRstIfMethodCommand
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("sales_scm.scs_req_rst_if_method")
class ReqRstIfMethod(
    reqRstIfMethodId: String? = null,
    custMstId: String?,
    applyStartDt: LocalDate,
    custCd: String,
    applyEndDt: LocalDate?,
    reqMethodCd: String,
    reqIfTypeCd: String?,
    useYn: Boolean,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<String> {

    @Id
    @Column("req_rst_if_method_id")
    val reqRstIfMethodId: String? = reqRstIfMethodId

    @Column("cust_mst_id")
    var custMstId: String? = custMstId
        private set

    @Column("apply_start_dt")
    var applyStartDt: LocalDate = applyStartDt
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("apply_end_dt")
    var applyEndDt: LocalDate? = applyEndDt
        private set

    @Column("req_method_cd")
    var reqMethodCd: String = reqMethodCd
        private set

    @Column("req_if_type_cd")
    var reqIfTypeCd: String? = reqIfTypeCd
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

    override fun getId(): String? = reqRstIfMethodId

    override fun isNew(): Boolean = _isNew

    fun update(command: ReqRstIfMethodCommand, updater: String) {
        this.custMstId = command.custMstId
        this.applyStartDt = command.applyStartDt
        this.custCd = command.custCd
        this.applyEndDt = command.applyEndDt
        this.reqMethodCd = command.reqMethodCd
        this.reqIfTypeCd = command.reqIfTypeCd
        this.useYn = command.useYn
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
