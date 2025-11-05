package com.idrsys.ailis.sales.domain.model

import com.idrsys.ailis.sales.application.dto.cust.CustUpdateCommand
import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("sales_scm.scs_cust_mst")
class Cust(
    custMstId: String? = null,
    custCd: String,
    custNm: String,
    rstOutputCustNm: String?,
    rprsNm: String?,
    rprsCustYn: Boolean = false,
    rprsCustCd: String?,
    custDivCd: String,
    directAcctCd: String?,
    directAcctAcctCd: String?,
    frgnAcctYn: Boolean = false,
    studyProjCustYn: Boolean = false,
    studyProjNm: String?,
    natnCd: String,
    useLangCd: String,
    crcyCd: String,
    custStatCd: String,
    reqPossYn: Boolean = true,
    custTypeCd: String,
    custGradeCd: String?,
    branchCd: String?,
    bzoffiCd: String?,
    bzoffiPicId: String?,
    asrtCd: String?,
    zipcd: String?,
    addr1: String?,
    addr2: String?,
    hpUrl: String?,
    careInstNo: String?,
    careInstId: String?,
    bizrno: String?,
    corpNo: String?,
    bzse: String?,
    bztp: String?,
    biznm: String?,
    bizregRprsNm: String?,
    openDt: LocalDate?,
    billPublYn: Boolean = false,
    billAutoPublTargetYn: Boolean = false,
    addtaxInclYn: Boolean = false,
    taxDivCd: String,
    billPic: String?,
    billPicEmaiAddr: String?,
    billPicTelno: String?,
    billPublDt: Int?,
    rprsAcctBillCombPublYn: Boolean = false,
    payRtday: Int?,
    payPlanDt: Int?,
    payMethodCd: String?,
    gccStmtMethodCd: String,
    sapCustCd: String?,
    spcmPickupMethodCd: String?,
    gcgPickupPicEmpNo: String?,
    truncUnitCd: String?,
    invcEmailRecpYn: Boolean = false,
    invcRecpEmailAddr: String?,
    outamtWritingYn: Boolean = false,
    sotOutputYn: Boolean = false,
    sotOutputQnty: Int,
    rstNtcnRecpYn: Boolean = false,
    rstNtcnRecpEmailAddr: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime
) : Persistable<String> {
    @Id
    @UuidGeneratedId(idFieldName = "custMstId")
    @Column("cust_mst_id")
    val custMstId: String? = custMstId

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("cust_nm")
    var custNm: String = custNm
        private set

    @Column("rst_output_cust_nm")
    var rstOutputCustNm: String? = rstOutputCustNm
        private set

    @Column("rprs_nm")
    var rprsNm: String? = rprsNm
        private set

    @Column("rprs_cust_yn")
    var rprsCustYn: Boolean = rprsCustYn
        private set

    @Column("rprs_cust_cd")
    var rprsCustCd: String? = rprsCustCd
        private set

    @Column("cust_div_cd")
    var custDivCd: String = custDivCd
        private set

    @Column("direct_acct_cd")
    var directAcctCd: String? = directAcctCd
        private set

    @Column("direct_acct_acct_cd")
    var directAcctAcctCd: String? = directAcctAcctCd
        private set

    @Column("frgn_acct_yn")
    var frgnAcctYn: Boolean = frgnAcctYn
        private set

    @Column("study_proj_cust_yn")
    var studyProjCustYn: Boolean = studyProjCustYn
        private set

    @Column("study_proj_nm")
    var studyProjNm: String? = studyProjNm
        private set

    @Column("natn_cd")
    var natnCd: String = natnCd
        private set

    @Column("use_lang_cd")
    var useLangCd: String = useLangCd
        private set

    @Column("crcy_cd")
    var crcyCd: String = crcyCd
        private set

    @Column("cust_stat_cd")
    var custStatCd: String = custStatCd
        private set

    @Column("req_poss_yn")
    var reqPossYn: Boolean = reqPossYn
        private set

    @Column("cust_type_cd")
    var custTypeCd: String = custTypeCd
        private set

    @Column("cust_grade_cd")
    var custGradeCd: String? = custGradeCd
        private set

    @Column("branch_cd")
    var branchCd: String? = branchCd
        private set

    @Column("bzoffi_cd")
    var bzoffiCd: String? = bzoffiCd
        private set

    @Column("bzoffi_pic_id")
    var bzoffiPicId: String? = bzoffiPicId
        private set

    @Column("asrt_cd")
    var asrtCd: String? = asrtCd
        private set

    @Column("zipcd")
    var zipcd: String? = zipcd
        private set

    @Column("addr1")
    var addr1: String? = addr1
        private set

    @Column("addr2")
    var addr2: String? = addr2
        private set

    @Column("hp_url")
    var hpUrl: String? = hpUrl
        private set

    @Column("care_inst_no")
    var careInstNo: String? = careInstNo
        private set

    @Column("care_inst_id")
    var careInstId: String? = careInstId
        private set

    @Column("bizrno")
    var bizrno: String? = bizrno
        private set

    @Column("corp_no")
    var corpNo: String? = corpNo
        private set

    @Column("bzse")
    var bzse: String? = bzse
        private set

    @Column("bztp")
    var bztp: String? = bztp
        private set

    @Column("biznm")
    var biznm: String? = biznm
        private set

    @Column("bizreg_rprs_nm")
    var bizregRprsNm: String? = bizregRprsNm
        private set

    @Column("open_dt")
    var openDt: LocalDate? = openDt
        private set

    @Column("bill_publ_yn")
    var billPublYn: Boolean = billPublYn
        private set

    @Column("bill_auto_publ_target_yn")
    var billAutoPublTargetYn: Boolean = billAutoPublTargetYn
        private set

    @Column("addtax_incl_yn")
    var addtaxInclYn: Boolean = addtaxInclYn
        private set

    @Column("tax_div_cd")
    var taxDivCd: String = taxDivCd
        private set

    @Column("bill_pic")
    var billPic: String? = billPic
        private set

    @Column("bill_pic_emai_addr")
    var billPicEmaiAddr: String? = billPicEmaiAddr
        private set

    @Column("bill_pic_telno")
    var billPicTelno: String? = billPicTelno
        private set

    @Column("bill_publ_dt")
    var billPublDt: Int? = billPublDt
        private set

    @Column("rprs_acct_bill_comb_publ_yn")
    var rprsAcctBillCombPublYn: Boolean = rprsAcctBillCombPublYn
        private set

    @Column("pay_rtday")
    var payRtday: Int? = payRtday
        private set

    @Column("pay_plan_dt")
    var payPlanDt: Int? = payPlanDt
        private set

    @Column("pay_method_cd")
    var payMethodCd: String? = payMethodCd
        private set

    @Column("gcc_stmt_method_cd")
    var gccStmtMethodCd: String = gccStmtMethodCd
        private set

    @Column("sap_cust_cd")
    var sapCustCd: String? = sapCustCd
        private set

    @Column("spcm_pickup_method_cd")
    var spcmPickupMethodCd: String? = spcmPickupMethodCd
        private set

    @Column("gcg_pickup_pic_emp_no")
    var gcgPickupPicEmpNo: String? = gcgPickupPicEmpNo
        private set

    @Column("trunc_unit_cd")
    var truncUnitCd: String? = truncUnitCd
        private set

    @Column("invc_email_recp_yn")
    var invcEmailRecpYn: Boolean = invcEmailRecpYn
        private set

    @Column("invc_recp_email_addr")
    var invcRecpEmailAddr: String? = invcRecpEmailAddr
        private set

    @Column("outamt_writing_yn")
    var outamtWritingYn: Boolean = outamtWritingYn
        private set

    @Column("sot_output_yn")
    var sotOutputYn: Boolean = sotOutputYn
        private set

    @Column("sot_output_qnty")
    var sotOutputQnty: Int = sotOutputQnty
        private set

    @Column("rst_ntcn_recp_yn")
    var rstNtcnRecpYn: Boolean = rstNtcnRecpYn
        private set

    @Column("rst_ntcn_recp_email_addr")
    var rstNtcnRecpEmailAddr: String? = rstNtcnRecpEmailAddr
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

    override fun getId(): String = custMstId!!

    override fun isNew(): Boolean = _isNew

    fun update(command: CustUpdateCommand, updater: String) {
        this.custCd = command.custCd
        this.custNm = command.custNm
        this.rstOutputCustNm = command.rstOutputCustNm
        this.rprsNm = command.rprsNm
        this.rprsCustYn = command.rprsCustYn
        this.rprsCustCd = command.rprsCustCd
        this.custDivCd = command.custDivCd
        this.directAcctCd = command.directAcctCd
        this.directAcctAcctCd = command.directAcctAcctCd
        this.frgnAcctYn = command.frgnAcctYn
        this.studyProjCustYn = command.studyProjCustYn
        this.studyProjNm = command.studyProjNm
        this.natnCd = command.natnCd
        this.useLangCd = command.useLangCd
        this.crcyCd = command.crcyCd
        this.custStatCd = command.custStatCd
        this.reqPossYn = command.reqPossYn
        this.custTypeCd = command.custTypeCd
        this.custGradeCd = command.custGradeCd
        this.branchCd = command.branchCd
        this.bzoffiCd = command.bzoffiCd
        this.bzoffiPicId = command.bzoffiPicId
        this.asrtCd = command.asrtCd
        this.zipcd = command.zipcd
        this.addr1 = command.addr1
        this.addr2 = command.addr2
        this.hpUrl = command.hpUrl
        this.careInstNo = command.careInstNo
        this.careInstId = command.careInstId
        this.bizrno = command.bizrno
        this.corpNo = command.corpNo
        this.bzse = command.bzse
        this.bztp = command.bztp
        this.biznm = command.biznm
        this.bizregRprsNm = command.bizregRprsNm
        this.openDt = command.openDt
        this.billPublYn = command.billPublYn
        this.billAutoPublTargetYn = command.billAutoPublTargetYn
        this.addtaxInclYn = command.addtaxInclYn
        this.taxDivCd = command.taxDivCd
        this.billPic = command.billPic
        this.billPicEmaiAddr = command.billPicEmaiAddr
        this.billPicTelno = command.billPicTelno
        this.billPublDt = command.billPublDt
        this.rprsAcctBillCombPublYn = command.rprsAcctBillCombPublYn
        this.payRtday = command.payRtday
        this.payPlanDt = command.payPlanDt
        this.payMethodCd = command.payMethodCd
        this.gccStmtMethodCd = command.gccStmtMethodCd
        this.sapCustCd = command.sapCustCd
        this.spcmPickupMethodCd = command.spcmPickupMethodCd
        this.gcgPickupPicEmpNo = command.gcgPickupPicEmpNo
        this.truncUnitCd = command.truncUnitCd
        this.invcEmailRecpYn = command.invcEmailRecpYn
        this.invcRecpEmailAddr = command.invcRecpEmailAddr
        this.outamtWritingYn = command.outamtWritingYn
        this.sotOutputYn = command.sotOutputYn
        this.sotOutputQnty = command.sotOutputQnty
        this.rstNtcnRecpYn = command.rstNtcnRecpYn
        this.rstNtcnRecpEmailAddr = command.rstNtcnRecpEmailAddr
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}