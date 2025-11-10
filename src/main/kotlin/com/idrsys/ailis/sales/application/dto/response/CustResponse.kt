package com.idrsys.ailis.sales.application.dto.response

import com.idrsys.web.excel.ExcelColumn
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.String

// 고객 관리 상세
data class CustResponse(
    val custMstId: String?,               // 고객마스터 UUID
    val custCd: String,                 // 고객코드
    val custNm: String,                 // 고객명
    val rstOutputCustNm: String?,       // 결과지출력고객명
    val rprsNm: String?,                 // 대표자명
    val rprsCustYn: Boolean,            // 대표고객여부
    val rprsCustCd: String?,             // 대표고객코드
    val custDivCd: String,              // 고객구분코드
    val directAcctCd: String?,           // 직납처코드
    val directAcctAcctCd: String?,       // 직납처 거래처코드
    val frgnAcctYn: Boolean,            // 해외거래처여부
    val studyProjCustYn: Boolean,       // 연구과제고객여부
    val studyProjNm: String?,            // 연구과제명
    val natnCd: String,                 // 국가코드
    val useLangCd: String,              // 사용언어코드
    val crcyCd: String,                 // 통화코드
    val custStatCd: String,             // 고객상태코드
    val reqPossYn: Boolean,             // 의뢰가능여부
    val custTypeCd: String,             // 고객유형코드
    val custGradeCd: String?,            // 고객등급코드
    val branchCd: String?,               // 지사코드
    val bzoffiCd: String?,               // 영업소코드
    val bzoffiPicId: String?,            // 영업소담당자ID
    val asrtCd: String?,                 // 종별코드
    val zipcd: String?,                  // 우편번호
    val addr1: String?,                  // 주소1
    val addr2: String?,                  // 주소2
    val hpUrl: String?,                  // 홈페이지URL
    val careInstNo: String?,             // 요양기관번호
    val careInstId: String?,             // 요양기관ID
    val bizrno: String?,                 // 사업자번호
    val corpNo: String?,                 // 법인번호
    val bzse: String?,                   // 업태
    val bztp: String?,                   // 업종
    val biznm: String?,                  // 상호
    val bizregRprsNm: String?,           // 사업자등록대표자명
    val openDt: LocalDate?,              // 개업일
    val billPublYn: Boolean,            // 계산서발행여부
    val billAutoPublTargetYn: Boolean,  // 계산서자동발행대상여부
    val addtaxInclYn: Boolean,          // 부가세포함여부
    val taxDivCd: String,               // 세금구분코드
    val billPic: String?,                // 계산서담당자
    val billPicEmaiAddr: String?,        // 계산서담당자이메일주소
    val billPicTelno: String?,           // 계산서담당자전화번호
    val billPublDt: Int?,                // 계산서발행일자
    val rprsAcctBillCombPublYn: Boolean,// 대표계정계산서통합발행여부
    val payRtday: Int?,                  // 수금예정일
    val payPlanDt: Int?,                 // 결제예정일
    val payMethodCd: String?,            // 결제방법코드
    val gccStmtMethodCd: String,        // GCC명세서방법코드
    val sapCustCd: String?,              // SAP고객코드
    val spcmPickupMethodCd: String?,     // 검체픽업방법코드
    val gcgPickupPicEmpNo: String?,      // GCG픽업담당자사원번호
    val truncUnitCd: String?,            // 절사단위코드
    val invcEmailRecpYn: Boolean,        // 인보이스이메일수신여부
    val invcRecpEmailAddr: String?,      // 인보이스수신이메일주소
    val outamtWritingYn: Boolean,       // 외화표기여부
    val sotOutputYn: Boolean,           // SOT출력여부
    val sotOutputQnty: Int,             // SOT출력수량
    val rstNtcnRecpYn: Boolean,          // 결과알림수신여부
    val rstNtcnRecpEmailAddr: String?,   // 결과알림수신이메일주소
    val creator: String,                // 생성자
    val createDtime: LocalDateTime,     // 생성일시
    val updater: String,                // 수정자
    val updateDtime: LocalDateTime      // 수정일시
)

// 고객 관리 목록
data class CustListResponse(
    val custMstId: String,               // 고객마스터 UUID
    @ExcelColumn("고객코드")
    val custCd: String,                 // 고객코드
    @ExcelColumn("고객명")
    val custNm: String,                 // 고객명
    val bzoffiCd: String?,              // 영업소코드
    @ExcelColumn("영업소")        // 영업소 deptNm
    val deptNm: String?,
    @ExcelColumn("고객구분")
    val custDivCd: String,            // 고객구분
    @ExcelColumn("고객유형")
    val custTypeCd: String,           // 고객유형
    @ExcelColumn("대표고객")
    val rprsCustCd: String?,          // 대표고객
    @ExcelColumn("사업자번호")
    val bizrno: String?,                // 사업자번호
    @ExcelColumn("요양기관번호")
    val careInstNo: String?,            // 요양기관번호
    @ExcelColumn("ERP코드")
    val sapCustCd: String?,             // ERP코드
    @ExcelColumn("담당사원")
    val salsPicInfo: String?,           // 담당사원 //TODO 지놈영업담당자정보 scs_gcgn_sals_pic_info
    @ExcelColumn("고객상태")
    val custStatCd: String,             // 고객상태
    @ExcelColumn("등록일시")
    val createDtime: LocalDateTime,      // 등록일시
    val cntr: custCntrResponse?,         //

)

data class salsPicInfoResponse(
    val custMstId: String,
    val applyStartDt: LocalDate,
    val salsTeamCd: String,
    val empno: String,
    val cust_cd: String,
    val applyEndDt: LocalDate?,
    val useYn: Boolean,
)

data class custCntrResponse(
    val custCntrId: String
)
