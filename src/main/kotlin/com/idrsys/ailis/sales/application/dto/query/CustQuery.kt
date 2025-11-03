package com.idrsys.ailis.sales.application.dto.query

import java.time.LocalDateTime

// 목록에 뿌려질 애들
data class CustWithSalsPicInfo(
    val custCd: String,                 // 고객코드
    val custNm: String,                 // 고객명
    val bzoffiCd: String?,              // 영업소코드
    val cust_div_cd: String,            // 고객구분
    val cust_type_cd: String,           // 고객유형
    val rprs_cust_cd: String?,          // 대표고객
    val bizrno: String?,                // 사업자번호
    val careInstNo: String?,            // 요양기관번호
    val sapCustCd: String?,             // ERP코드
    val salsPicInfo: String?,           // 담당사원 (e.g., "G=1111,C=1112")
    val custStatCd: String,             // 고객상태
    val createDtime: LocalDateTime      // 등록일시
)

data class PicInfo (
    val id: String,
    val name: String
)