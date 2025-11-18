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
    // 기본정보
    custMstId: String? = null,               // 고객마스터 UUID
    custCd: String,                 // 고객코드
    custNm: String,                 // 고객명
    custDivCd: String,              // 고객구분코드      직접거래처여부 : 직접거래처 / 재수탁거래처
    directAcctCd: String?,           // 직접거래처코드
    directAcctAcctCd: String?,       // 직접거래처거래처코드 인터페이스용
    rprsNm: String?,                 // 대표자명
    rstOutputCustNm: String?,       // 결과지출력고객명
    careInstId: String?,             // 요양기관ID  ->  심평원명    scs_hosp_mst
    custTypeCd: String,             // 고객유형코드
    bzoffiCd: String?,               // 영업소코드
    bzoffiPicId: String?,            // 영업소담당자ID
    custStatCd: String,             // 고객상태코드
    reqPossYn: Boolean = true,             // 의뢰가능여부
    rprsCustYn: Boolean = false,            // 대표고객여부
    rprsCustCd: String?,             // 대표고객코드
    studyProjCustYn: Boolean = false,       // 연구과제고객여부
    studyProjNm: String?,            // 연구과제명
    asrtCd: String?,                 // 종별코드
    careInstNo: String?,             // 요양기관번호
    telNo: String?,                 // 전화번호
    faxNo: String?,                 // 팩스번호
    zipcd: String?,                  // 우편번호
    addr1: String?,                  // 주소1
    addr2: String?,                  // 주소2
    qcCustYn: Boolean = false,              // QC고객여부
    // 사업자등록정보
    bizrno: String?,                 // 사업자번호
    sapCustCd: String?,              // SAP고객코드
    corpNo: String?,                 // 법인번호
    biznm: String?,                  // 사업자명
    bztp: String?,                   // 업종
    bzse: String?,                   // 업태
    bizregRprsNm: String?,           // 사업자등록대표자명
    openDt: LocalDate?,              // 개업일
    // 수금/정산/계산서정보
    billPublYn: Boolean = false,            // 계산서발행여부
    addtaxInclYn: Boolean = false,          // 부가세포함여부
    billPic: String?,                // 계산서담당자
    billPicEmaiAddr: String?,        // 계산서담당자이메일주소
    billPicTelno: String?,           // 계산서담당자전화번호
    billPublDt: Int?,                // 계산서발행일자
    payRtday: Int?,                  // 결제회전일
    payPlanDt: Int?,                 // 결제예정일
    payMethodCd: String?,            // 결제방법코드
    billAutoPublTargetYn: Boolean = false,  // 계산서자동발행대상여부
    gccStmtMethodCd: String,        // GCC명세서방법코드   -> Cell정산방식
    spcmPickupMethodCd: String?,     // 검체픽업방법코드
    gcgPickupPicEmpNo: String?,      // GCG픽업담당자사원번호  -> Genome 수거 담당자 user 기본역할=수거
    outamtWritingYn: Boolean = false,       // 미수금표기여부
    // 해외거래처정보
    frgnAcctYn: Boolean = false,            // 해외거래처여부
    natnCd: String,                 // 국가코드
    useLangCd: String,              // 사용언어코드
    crcyCd: String,                 // 통화코드
    //기타
    custGradeCd: String?,            // 고객등급코드
    branchCd: String?,               // 지사코드
    hpUrl: String?,                  // 홈페이지URL
    taxDivCd: String,               // 세금구분코드
    rprsAcctBillCombPublYn: Boolean = false,// 대표계정계산서통합발행여부
    truncUnitCd: String?,            // 절사단위코드
    invcEmailRecpYn: Boolean = false,        // 인보이스이메일수신여부
    invcRecpEmailAddr: String?,      // 인보이스수신이메일주소
    sotOutputYn: Boolean = false,           // SOT출력여부
    sotOutputQnty: Int,             // SOT출력수량
    rstNtcnRecpYn: Boolean = false,          // 결과알림수신여부
    rstNtcnRecpEmailAddr: String?,   // 결과알림수신이메일주소
    // 테이블공통
    creator: String,                // 생성자
    createDtime: LocalDateTime,     // 생성일시
    updater: String,                // 수정자
    updateDtime: LocalDateTime      // 수정일시
) {
    @Id
    @UuidGeneratedId(idFieldName = "custMstId")
    @Column("cust_mst_id")
    val custMstId: String? = custMstId

    // 기본정보
    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("cust_nm")
    var custNm: String = custNm
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

    @Column("rprs_nm")
    var rprsNm: String? = rprsNm
        private set

    @Column("rst_output_cust_nm")
    var rstOutputCustNm: String? = rstOutputCustNm
        private set

    @Column("care_inst_id")
    var careInstId: String? = careInstId
        private set

    @Column("cust_type_cd")
    var custTypeCd: String = custTypeCd
        private set

    @Column("bzoffi_cd")
    var bzoffiCd: String? = bzoffiCd
        private set

    @Column("bzoffi_pic_id")
    var bzoffiPicId: String? = bzoffiPicId
        private set

    @Column("cust_stat_cd")
    var custStatCd: String = custStatCd
        private set

    @Column("req_poss_yn")
    var reqPossYn: Boolean = reqPossYn
        private set

    @Column("rprs_cust_yn")
    var rprsCustYn: Boolean = rprsCustYn
        private set

    @Column("rprs_cust_cd")
    var rprsCustCd: String? = rprsCustCd
        private set

    @Column("study_proj_cust_yn")
    var studyProjCustYn: Boolean = studyProjCustYn
        private set

    @Column("study_proj_nm")
    var studyProjNm: String? = studyProjNm
        private set

    @Column("asrt_cd")
    var asrtCd: String? = asrtCd
        private set

    @Column("care_inst_no")
    var careInstNo: String? = careInstNo
        private set

    @Column("tel_no")
    var telNo: String? = telNo
        private set

    @Column("fax_no")
    var faxNo: String? = faxNo
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

    @Column("qc_cust_yn")
    var qcCustYn: Boolean = qcCustYn
        private set

    // 사업자등록정보
    @Column("bizrno")
    var bizrno: String? = bizrno
        private set

    @Column("sap_cust_cd")
    var sapCustCd: String? = sapCustCd
        private set

    @Column("corp_no")
    var corpNo: String? = corpNo
        private set

    @Column("biznm")
    var biznm: String? = biznm
        private set

    @Column("bztp")
    var bztp: String? = bztp
        private set

    @Column("bzse")
    var bzse: String? = bzse
        private set

    @Column("bizreg_rprs_nm")
    var bizregRprsNm: String? = bizregRprsNm
        private set

    @Column("open_dt")
    var openDt: LocalDate? = openDt
        private set

    // 수금/정산/계산서정보
    @Column("bill_publ_yn")
    var billPublYn: Boolean = billPublYn
        private set

    @Column("addtax_incl_yn")
    var addtaxInclYn: Boolean = addtaxInclYn
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

    @Column("pay_rtday")
    var payRtday: Int? = payRtday
        private set

    @Column("pay_plan_dt")
    var payPlanDt: Int? = payPlanDt
        private set

    @Column("pay_method_cd")
    var payMethodCd: String? = payMethodCd
        private set

    @Column("bill_auto_publ_target_yn")
    var billAutoPublTargetYn: Boolean = billAutoPublTargetYn
        private set

    @Column("gcc_stmt_method_cd")
    var gccStmtMethodCd: String = gccStmtMethodCd
        private set

    @Column("spcm_pickup_method_cd")
    var spcmPickupMethodCd: String? = spcmPickupMethodCd
        private set

    @Column("gcg_pickup_pic_emp_no")
    var gcgPickupPicEmpNo: String? = gcgPickupPicEmpNo
        private set

    @Column("outamt_writing_yn")
    var outamtWritingYn: Boolean = outamtWritingYn
        private set

    // 해외거래처정보
    @Column("frgn_acct_yn")
    var frgnAcctYn: Boolean = frgnAcctYn
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

    //기타
    @Column("cust_grade_cd")
    var custGradeCd: String? = custGradeCd
        private set

    @Column("branch_cd")
    var branchCd: String? = branchCd
        private set

    @Column("hp_url")
    var hpUrl: String? = hpUrl
        private set

    @Column("tax_div_cd")
    var taxDivCd: String = taxDivCd
        private set

    @Column("rprs_acct_bill_comb_publ_yn")
    var rprsAcctBillCombPublYn: Boolean = rprsAcctBillCombPublYn
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

    // 테이블공통
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

    fun update(command: CustUpdateCommand, updater: String) {
        // 기본정보
        this.custCd = command.custCd
        this.custNm = command.custNm
        this.custDivCd = command.custDivCd
        this.directAcctCd = command.directAcctCd
        this.directAcctAcctCd = command.directAcctAcctCd
        this.rprsNm = command.rprsNm
        this.rstOutputCustNm = command.rstOutputCustNm
        this.careInstId = command.careInstId
        this.custTypeCd = command.custTypeCd
        this.bzoffiCd = command.bzoffiCd
        this.bzoffiPicId = command.bzoffiPicId
        this.custStatCd = command.custStatCd
        this.reqPossYn = command.reqPossYn
        this.rprsCustYn = command.rprsCustYn
        this.rprsCustCd = command.rprsCustCd
        this.studyProjCustYn = command.studyProjCustYn
        this.studyProjNm = command.studyProjNm
        this.asrtCd = command.asrtCd
        this.careInstNo = command.careInstNo
        this.telNo = command.telNo
        this.faxNo = command.faxNo
        this.zipcd = command.zipcd
        this.addr1 = command.addr1
        this.addr2 = command.addr2
        this.qcCustYn = command.qcCustYn
        // 사업자등록정보
        this.bizrno = command.bizrno
        this.sapCustCd = command.sapCustCd
        this.corpNo = command.corpNo
        this.biznm = command.biznm
        this.bztp = command.bztp
        this.bzse = command.bzse
        this.bizregRprsNm = command.bizregRprsNm
        this.openDt = command.openDt
        // 수금/정산/계산서정보
        this.billPublYn = command.billPublYn
        this.addtaxInclYn = command.addtaxInclYn
        this.billPic = command.billPic
        this.billPicEmaiAddr = command.billPicEmaiAddr
        this.billPicTelno = command.billPicTelno
        this.billPublDt = command.billPublDt
        this.payRtday = command.payRtday
        this.payPlanDt = command.payPlanDt
        this.payMethodCd = command.payMethodCd
        this.billAutoPublTargetYn = command.billAutoPublTargetYn
        this.gccStmtMethodCd = command.gccStmtMethodCd
        this.spcmPickupMethodCd = command.spcmPickupMethodCd
        this.gcgPickupPicEmpNo = command.gcgPickupPicEmpNo
        this.outamtWritingYn = command.outamtWritingYn
        // 해외거래처정보
        this.frgnAcctYn = command.frgnAcctYn
        this.natnCd = command.natnCd
        this.useLangCd = command.useLangCd
        this.crcyCd = command.crcyCd
        //기타
        this.custGradeCd = command.custGradeCd
        this.branchCd = command.branchCd
        this.hpUrl = command.hpUrl
        this.taxDivCd = command.taxDivCd
        this.rprsAcctBillCombPublYn = command.rprsAcctBillCombPublYn
        this.truncUnitCd = command.truncUnitCd
        this.invcEmailRecpYn = command.invcEmailRecpYn
        this.invcRecpEmailAddr = command.invcRecpEmailAddr
        this.sotOutputYn = command.sotOutputYn
        this.sotOutputQnty = command.sotOutputQnty
        this.rstNtcnRecpYn = command.rstNtcnRecpYn
        this.rstNtcnRecpEmailAddr = command.rstNtcnRecpEmailAddr
        // 테이블공통
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}