package com.idrsys.ailis.sales.application.dto.query

import java.time.LocalDateTime

// 고객관리 리스트
data class CustWithSalsPicInfo(
    val custMstId: String,              // 고객마스터UUID
    val custCd: String,                 // 고객코드
    val custNm: String,                 // 고객명
    val bzoffiCd: String?,              // 영업소코드
    val custDivCd: String,            // 고객구분
    val custTypeCd: String,           // 고객유형
    val rprsCustCd: String?,          // 대표고객
    val bizrno: String?,                // 사업자번호
    val careInstNo: String?,            // 요양기관번호
    val sapCustCd: String?,             // ERP코드
    val salsPicInfo: String?,           // 담당사원 (e.g., "G=홍길동,C=홍길동,H=홍길동")
    val custStatCd: String,             // 고객상태
    val createDtime: LocalDateTime      // 등록일시
)

data class CustCdNmAutoCompleteInfo(
    val custCd: String,
    val custNm: String,
)

data class PicInfo (
    val id: String,
    val name: String
)