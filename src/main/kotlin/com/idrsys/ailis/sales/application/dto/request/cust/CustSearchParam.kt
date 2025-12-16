package com.idrsys.ailis.sales.application.dto.cust

data class CustSearchParam(
    val bracnCd: String? = null,  // 지점
    val bzoffiCd: String? = null, // 영업소
    val custCdNm: String? = null, // 고객코드/명 사용자 입력값 or 자동완성 선택시 고객코드명
    val custCd: String? = null, // 자동완성 선택시 고객코드
    val custCds: List<String> = emptyList(), // in절 조회용 고객코드 리스트
    val rprsCustCdNm: String? = null, // 대표고객코드/명 사용자 입력값 or 자동완성 선택시 대표고객코드의 custNm
    val rprsCustCd: String? = null, // 자동완성 선택시 대표고객코드
    val custStatCd: String? = null, // 고객상태코드
    val regStartDt: String? = null, // 등록일시 시작일
    val regEndDt: String? = null, // 등록일시 종료일
    val empUserIdNm: String? = null, // 담당사원 ID/명 사용자 입력값 or 자동완성 선택시 담당사원명
    val empUserId: String? = null, // 자동완성 선택시 담당사원 ID
    val empUserIds: List<String> = emptyList(),
    val custDivCd: String? = null, // 고객구분코드
    val asrtCd: String? = null,
    val medicalSubj: String? = null,
    val bizrno:String? = null,
    val careInstNo:String? = null,
    val cntrStartDt: String? = null,
    val cntrEndDt: String? = null,
    val cntrEndStartDt: String? = null,
    val cntrEndEndDt: String? = null,
    val recntrMonth: String? = null,
    val frgnAcctYn:Boolean? = null,
    val studyProjCustYn:Boolean? = null,
    val sapCustCd:String? = null,
    val custTypeCd:String? = null
)

data class CustAutoCompleteSearchParam(
    val custCdNm: String? = null, // 고객코드/명 자동완성 검색어
    val rprsCustCdNm: String? = null, // 대표고객코드/명 자동완성 검색어
    val directAcctCdNm: String? = null, // 직접거래처코드/명 자동완성 검색어
)
