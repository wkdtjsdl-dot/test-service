package com.idrsys.ailis.sales.application.dto.response

import com.idrsys.web.excel.ExcelColumn
import org.springframework.data.domain.Page
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.String

// 고객 관리
data class CustResponse(
    val custCd: String,                 // 고객코드
    val custNm: String,                 // 고객명
    val bzoffiCd: String?,              // 영업소코드
    val custDivCd: String,            // 고객구분
    val custTypeCd: String,           // 고객유형
    val rprsCustCd: String?,          // 대표고객
    val bizrno: String?,                // 사업자번호
    val careInstNo: String?,            // 요양기관번호
    val sapCustCd: String?,             // ERP코드
    val custStatCd: String,             // 고객상태
    val createDtime: LocalDateTime      // 등록일시
)

// 고객 관리 목록
data class CustListResponse(
    @ExcelColumn("고객코드")
    val custCd: String,                 // 고객코드
    @ExcelColumn("고객명")
    val custNm: String,                 // 고객명
    @ExcelColumn("영업소코드")
    val bzoffiCd: String?,              // 영업소코드
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
    val createDtime: LocalDateTime      // 등록일시
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