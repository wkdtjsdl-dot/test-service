package com.idrsys.ailis.sales.domain.model

import com.idrsys.ailis.sales.application.dto.request.contract.ContractCommand
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("sales_scm.scs_cust_cntr")
class Contract(
    custCntrId: Long? = null,
    custMstId: String,
    custCd: String,
    cntrNo: String?,
    cntrDt: LocalDate?,
    cntrStartDt: LocalDate?,
    cntrEndDt: LocalDate?,
    cntrType: String?,
    recntrMonth: String?,
    cntrNm: String?,
    cntrCont: String?,
    cntrPicId: String?,
    atchGrupId: String,
    useYn: Boolean = true,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<Long> {

    @Id
    @Column("cust_cntr_id")
    val custCntrId: Long? = custCntrId

    @Column("cust_mst_id")
    var custMstId: String = custMstId
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("cntr_no")
    var cntrNo: String? = cntrNo
        private set

    @Column("cntr_dt")
    var cntrDt: LocalDate? = cntrDt
        private set

    @Column("cntr_start_dt")
    var cntrStartDt: LocalDate? = cntrStartDt
        private set

    @Column("cntr_end_dt")
    var cntrEndDt: LocalDate? = cntrEndDt
        private set

    @Column("cntr_type_cd")
    var cntrType: String? = cntrType
        private set

    @Column("recntr_month")
    var recntrMonth: String? = recntrMonth
        private set

    @Column("cntr_nm")
    var cntrNm: String? = cntrNm
        private set

    @Column("cntr_cont")
    var cntrCont: String? = cntrCont
        private set

    @Column("cntr_pic_id")
    var cntrPicId: String? = cntrPicId
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

    override fun getId(): Long? = custCntrId

    override fun isNew(): Boolean = _isNew

    fun update(command: ContractCommand, updater: String) {
        this.custCd = command.custCd
        this.cntrNo = command.cntrNo
        this.cntrDt = command.cntrDt
        this.cntrStartDt = command.cntrStartDt
        this.cntrEndDt = command.cntrEndDt
        this.cntrType = command.cntrType
        this.recntrMonth = command.recntrMonth
        this.cntrNm = command.cntrNm
        this.cntrCont = command.cntrCont
        this.cntrPicId = command.cntrPicId
        this.atchGrupId = command.atchGrupId
        this.useYn = command.useYn
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
