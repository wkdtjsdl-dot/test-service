package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_cust_req_poss_tst_item")
class CustReqPossTstItem(
    custReqPossTstItemId: Long? = null,
    custMstId: String?,
    custCd: String,
    tstCd: String,
    creator: String,
    createDtime: LocalDateTime,
) : Persistable<Long> {

    @Id
    @Column("cust_req_poss_tst_item_id")
    val custReqPossTstItemId: Long? = custReqPossTstItemId

    @Column("cust_mst_id")
    var custMstId: String? = custMstId
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("tst_cd")
    var tstCd: String = tstCd
        private set

    @Column("creator")
    val creator: String = creator

    @Column("create_dtime")
    val createDtime: LocalDateTime = createDtime

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): Long? = custReqPossTstItemId

    override fun isNew(): Boolean = _isNew
}
