package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sales_scm.scs_cust_tst_cd_mpg")
class CustTstCdMpg(
    custTstCdMpgId: String,
    custMstId: String?,
    custCd: String,
    custTstCd: String,
    custSubTstCd: String?,
    custTstNm: String?,
    tstCd: String?,
    tstNm: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<String> {

    @Id
    @Column("cust_tst_cd_mpg_id")
    private val custTstCdMpgId: String = custTstCdMpgId

    @Column("cust_mst_id")
    var custMstId: String? = custMstId
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("cust_tst_cd")
    var custTstCd: String = custTstCd
        private set

    @Column("cust_sub_tst_cd")
    var custSubTstCd: String? = custSubTstCd
        private set

    @Column("cust_tst_nm")
    var custTstNm: String? = custTstNm
        private set

    @Column("tst_cd")
    var tstCd: String? = tstCd
        private set

    @Column("tst_nm")
    var tstNm: String? = tstNm
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

    override fun getId(): String = custTstCdMpgId

    override fun isNew(): Boolean = _isNew

    fun setAsNew() {
        this._isNew = true
    }
}