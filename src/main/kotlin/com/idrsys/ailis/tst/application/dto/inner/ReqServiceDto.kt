package com.idrsys.ailis.tst.application.dto.inner

import java.time.LocalDate

/**
 * req-service Inner API 연동용 DTO
 */

/**
 * 검사 의뢰 정보 조회 요청 - 키 정보
 */
data class TestRequestKey(
    val tstReqDt: LocalDate,
    val tstReqNo: Long
)

/**
 * 검사 항목 상태 조회 요청 - 키 정보
 */
data class TestItemKey(
    val tstReqDt: LocalDate,
    val tstReqNo: Long,
    val tstCd: String
)

/**
 * 검사 의뢰 정보 응답 (Inner API)
 */
data class TestRequestInfo(
    // 식별자
    val tstReqDt: LocalDate,
    val tstReqNo: Long,

    // 환자 정보 (from rbs_patient)
    val patientNm: String?,           // 환자명
    val custCd: String,                // 거래처코드
    val hospChartNo: String?,          // 병원차트번호

    // 의뢰 상태 (from rbs_patient)
    val tstReqStatCd: String?,         // 의뢰상태코드
    val tstReqDivCd: String?,          // 의뢰구분코드

    // 추가 정보
    val deptCd: String?,               // 부서코드
    val custNm: String?,               // 거래처명
    val hospNm: String?                // 병원명
)

/**
 * 검사 항목 상태 정보 응답 (Inner API)
 */
data class TestItemStatusInfo(
    // 식별자
    val tstReqDt: LocalDate,
    val tstReqNo: Long,
    val tstCd: String,

    // 검사 상태 (from rbs_tst_item)
    val tstStat1Cd: String,            // 검사상태1 (진행상태)
    val tstStat2Cd: String             // 검사상태2 (보고상태)
)