package com.idrsys.ailis.sales.application.dto.cust

import java.time.LocalDate
import java.time.LocalDateTime

data class CustCommand(
    // 기본정보
    val custCd: String,                 // 고객코드
    val custNm: String,                 // 고객명
    val custDivCd: String,              // 고객구분코드      직접거래처여부 : 직접거래처 / 재수탁거래처
    val directAcctCd: String,           // 직접거래처코드
    val directAcctAcctCd: String?,      // 직접거래처거래처코드 인터페이스용
    val rprsNm: String?,                 // 대표자명
    val rstOutputCustNm: String?,       // 결과지출력고객명
    val careInstId: String?,             // 요양기관ID  ->  심평원명    scs_hosp_mst
    val custTypeCd: String,             // 고객유형코드
    val bzoffiCd: String?,               // 영업소코드
    val bzoffiPicId: String?,            // 영업소담당자ID
    val custStatCd: String,             // 고객상태코드
    val reqPossYn: Boolean = true,      // 의뢰가능여부
    val rprsCustYn: Boolean,            // 대표고객여부
    val rprsCustCd: String?,             // 대표고객코드
    val studyProjCustYn: Boolean,       // 연구과제고객여부
    val studyProjNm: String?,            // 연구과제명
    val asrtCd: String?,                 // 종별코드
    val careInstNo: String?,             // 요양기관번호
    val telNo: String?,                  // 전화번호
    val faxNo: String?,                  // 팩스번호
    val zipcd: String?,                  // 우편번호
    val addr1: String?,                  // 주소1
    val addr2: String?,                  // 주소2
    val reqDivCd: String,               // 의뢰구분코드 (공통코드 RQDV)
    // 사업자등록정보
    val bizrno: String?,                 // 사업자번호
    val sapCustCd: String?,              // SAP고객코드
    val corpNo: String?,                 // 법인번호
    val biznm: String?,                  // 사업자명
    val bztp: String?,                   // 업종
    val bzse: String?,                   // 업태
    val bizregRprsNm: String?,           // 사업자등록대표자명
    val openDt: LocalDate?,              // 개업일
    // 수금/정산/계산서정보
    val billPublYn: Boolean,            // 계산서발행여부
    val addtaxInclYn: Boolean,          // 부가세포함여부
    val billPic: String?,                // 계산서담당자
    val billPicEmaiAddr: String?,        // 계산서담당자이메일주소
    val billPicTelno: String?,           // 계산서담당자전화번호
    val billPublDt: Int?,                // 계산서발행일자
    val payRtday: Int?,                  // 결제회전일
    val payPlanDt: Int?,                 // 결제예정일
    val payMethodCd: String?,            // 결제방법코드
    val billAutoPublTargetYn: Boolean,  // 계산서자동발행대상여부
    val gccStmtMethodCd: String,        // GCC명세서방법코드   -> Cell정산방식
    val spcmPickupMethodCd: String?,     // 검체픽업방법코드
    val gcgPickupPicEmpNo: String?,      // GCG픽업담당자사원번호  -> Genome 수거 담당자 user 기본역할=수거
    val outamtWritingYn: Boolean,       // 미수금표기여부
    // 해외거래처정보
    val frgnAcctYn: Boolean,            // 해외거래처여부
    val natnCd: String,                 // 국가코드
    val useLangCd: String,              // 사용언어코드
    val crcyCd: String,                 // 통화코드
    //기타
    val custGradeCd: String?,            // 고객등급코드
    val branchCd: String?,               // 지사코드
    val hpUrl: String?,                  // 홈페이지URL
    val taxDivCd: String,               // 세금구분코드
    val rprsAcctBillCombPublYn: Boolean,// 대표계정계산서통합발행여부
    val truncUnitCd: String?,            // 절사단위코드
    val invcEmailRecpYn: Boolean,        // 인보이스이메일수신여부
    val invcRecpEmailAddr: String?,      // 인보이스수신이메일주소
    val sotOutputYn: Boolean,           // SOT출력여부
    val sotOutputQnty: Int,             // SOT출력수량
    val rstNtcnRecpYn: Boolean,          // 결과알림수신여부
    val rstNtcnRecpEmailAddr: String?,   // 결과알림수신이메일주소
    val reqMethodCd: String?,           // 의뢰메소드(방법)코드
    val reqIfTypeCd: String?,           // 의뢰연동유형코드
    val atchFileGrupId: String?,         // 첨부파일그룹아이디
    val reqPossTstLimitYn: Boolean?,     // 의뢰가능검사제한여부
    val updateReason: String? = null    // 변경사유 (히스토리 테이블용)
)

data class CustSearchCommand (
    val directAcctCd: String,
    val serial: String? = null,
    val name: String? = null,
    val registrationNumber: String? = null,
    val nursingNumber: String? = null,
    val branchCode: String? = null,
    val branchName: String? = null,
    val branchCodes: List<String>? = null,
    val employeeId: String? = null,
    val employeeName: String? = null,
    val employeePhone: String? = null,
    val type: String? = null,
)

data class CustResponseCommand (
    val serial: String,
    val name: String?,
    val registrationNumber: String?,
    val nursingNumber: String?,
    val branchCode: String?,
    val branchName: String?,
    val employeeId: String?,
    val employeeName: String?,
    val employeePhone: String?,
    val type: String?,
    val createAt: LocalDateTime?
)

data class CustRegisterWrapper(
    val command: CustCommand,
    val authId: String
)

typealias CustRegisterCommand = CustCommand
typealias CustUpdateCommand = CustCommand