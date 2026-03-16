package com.idrsys.ailis.tst.application.dto.inner

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * req-service Inner API 응답 DTO
 * (req-service의 TstRequestDetailResponse와 동일한 구조)
 */
data class TstRequestDetailResponse(
    val patientId: String,
    val tstReqDt: LocalDate,
    val tstReqNo: Long,
    val patNm: String?,
    val hospChartNo: String?,
    val patRrn1: String?,
    val patBday: LocalDate?,
    val patAge: Int?,
    val patGenderCd: String?,
    val mediSbjtNm: String?,
    val sikRoom: String?,
    val drNm: String?,
    val spcmTakeDt: LocalDate?,
    val spcmCnt: Int?,
    val memo: String?,
    val tstReqDivCd: String?,
    val tstReqStatCd: String?,
    val reportLanCd: String,
    val directAcctCd: String,
    val custCd: String,
    val telNo: String?,
    val custNm: String?,
    val branchCd: String?,
    val bzoffiCd: String?,
    val salsPics: List<SalesPicResponse>?,
    val tstItems: List<TstItemResponse>?,
    val refItems: List<RefItemResponse>?
)

/**
 * 검사 항목 응답 DTO
 */
data class TstItemResponse(
    val tstCd: String,
    val tstNm: String,
    val spcmCd: String?,
    val creator: String,
    val create_dtime: LocalDateTime
)

/**
 * 참조 항목 응답 DTO
 */
data class RefItemResponse(
    val refCd: String,
    val refNm: String,
    val refCont: String
)

/**
 * 영업 담당자 응답 DTO
 */
data class SalesPicResponse(
    val empUserNm: String?,
    val empUserMobile: String?
)

/**
 * TstRequestDetailResponse를 TestRequestInfo로 변환
 */
fun TstRequestDetailResponse.toTestRequestInfo(): TestRequestInfo {
    return TestRequestInfo(
        tstReqDt = this.tstReqDt,
        tstReqNo = this.tstReqNo,
        patientNm = this.patNm,
        custCd = this.custCd,
        hospChartNo = this.hospChartNo,
        tstReqStatCd = this.tstReqStatCd,
        tstReqDivCd = this.tstReqDivCd,
        deptCd = null, // TstRequestDetailResponse에 deptCd 필드가 없음
        custNm = this.custNm,
        hospNm = null  // TstRequestDetailResponse에 hospNm 필드가 없음
    )
}