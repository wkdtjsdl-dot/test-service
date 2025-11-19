package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("sales_scm.scs_cust_mst_hst")
class CustMstHst(
    @Id
    @Column("cust_mst_hst_id")
    val custMstHstId: Long? = null,

    // Foreign key to the original table
    @Column("cust_mst_id")
    val custMstId: String,

    // Mirrored fields from Cust
    @Column("cust_cd")
    val custCd: String,
    @Column("cust_nm")
    val custNm: String,
    @Column("cust_div_cd")
    val custDivCd: String,
    @Column("direct_acct_cd")
    val directAcctCd: String?,
    @Column("direct_acct_acct_cd")
    val directAcctAcctCd: String?,
    @Column("rprs_nm")
    val rprsNm: String?,
    @Column("rst_output_cust_nm")
    val rstOutputCustNm: String?,
    @Column("care_inst_id")
    val careInstId: String?,
    @Column("cust_type_cd")
    val custTypeCd: String,
    @Column("bzoffi_cd")
    val bzoffiCd: String?,
    @Column("bzoffi_pic_id")
    val bzoffiPicId: String?,
    @Column("cust_stat_cd")
    val custStatCd: String,
    @Column("req_poss_yn")
    val reqPossYn: Boolean,
    @Column("rprs_cust_yn")
    val rprsCustYn: Boolean,
    @Column("rprs_cust_cd")
    val rprsCustCd: String?,
    @Column("study_proj_cust_yn")
    val studyProjCustYn: Boolean,
    @Column("study_proj_nm")
    val studyProjNm: String?,
    @Column("asrt_cd")
    val asrtCd: String?,
    @Column("care_inst_no")
    val careInstNo: String?,
    @Column("tel_no")
    val telNo: String?,
    @Column("fax_no")
    val faxNo: String?,
    @Column("zipcd")
    val zipcd: String?,
    @Column("addr1")
    val addr1: String?,
    @Column("addr2")
    val addr2: String?,
    @Column("qc_cust_yn")
    val qcCustYn: Boolean,
    @Column("bizrno")
    val bizrno: String?,
    @Column("sap_cust_cd")
    val sapCustCd: String?,
    @Column("corp_no")
    val corpNo: String?,
    @Column("biznm")
    val biznm: String?,
    @Column("bztp")
    val bztp: String?,
    @Column("bzse")
    val bzse: String?,
    @Column("bizreg_rprs_nm")
    val bizregRprsNm: String?,
    @Column("open_dt")
    val openDt: LocalDate?,
    @Column("bill_publ_yn")
    val billPublYn: Boolean,
    @Column("addtax_incl_yn")
    val addtaxInclYn: Boolean,
    @Column("bill_pic")
    val billPic: String?,
    @Column("bill_pic_emai_addr")
    val billPicEmaiAddr: String?,
    @Column("bill_pic_telno")
    val billPicTelno: String?,
    @Column("bill_publ_dt")
    val billPublDt: Int?,
    @Column("pay_rtday")
    val payRtday: Int?,
    @Column("pay_plan_dt")
    val payPlanDt: Int?,
    @Column("pay_method_cd")
    val payMethodCd: String?,
    @Column("bill_auto_publ_target_yn")
    val billAutoPublTargetYn: Boolean,
    @Column("gcc_stmt_method_cd")
    val gccStmtMethodCd: String,
    @Column("spcm_pickup_method_cd")
    val spcmPickupMethodCd: String?,
    @Column("gcg_pickup_pic_emp_no")
    val gcgPickupPicEmpNo: String?,
    @Column("outamt_writing_yn")
    val outamtWritingYn: Boolean,
    @Column("frgn_acct_yn")
    val frgnAcctYn: Boolean,
    @Column("natn_cd")
    val natnCd: String,
    @Column("use_lang_cd")
    val useLangCd: String,
    @Column("crcy_cd")
    val crcyCd: String,
    @Column("cust_grade_cd")
    val custGradeCd: String?,
    @Column("branch_cd")
    val branchCd: String?,
    @Column("hp_url")
    val hpUrl: String?,
    @Column("tax_div_cd")
    val taxDivCd: String,
    @Column("rprs_acct_bill_comb_publ_yn")
    val rprsAcctBillCombPublYn: Boolean,
    @Column("trunc_unit_cd")
    val truncUnitCd: String?,
    @Column("invc_email_recp_yn")
    val invcEmailRecpYn: Boolean,
    @Column("invc_recp_email_addr")
    val invcRecpEmailAddr: String?,
    @Column("sot_output_yn")
    val sotOutputYn: Boolean,
    @Column("sot_output_qnty")
    val sotOutputQnty: Int,
    @Column("rst_ntcn_recp_yn")
    val rstNtcnRecpYn: Boolean,
    @Column("rst_ntcn_recp_email_addr")
    val rstNtcnRecpEmailAddr: String?,
    @Column("req_method_cd")
    val reqMethodCd: String?,
    @Column("req_if_type_cd")
    val reqIfTypeCd: String?,
    @Column("creator")
    val creator: String,
    @Column("create_dtime")
    val createDtime: LocalDateTime,
    @Column("updater")
    val updater: String,
    @Column("update_dtime")
    val updateDtime: LocalDateTime
) : Persistable<Long> {

    @Transient
    private var isNew: Boolean = true

    override fun getId(): Long? = this.custMstHstId
    override fun isNew(): Boolean = this.isNew

}
