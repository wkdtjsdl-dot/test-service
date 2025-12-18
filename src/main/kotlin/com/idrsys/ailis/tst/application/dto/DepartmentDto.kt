package com.idrsys.ailis.tst.application.dto

/**
 * base-service의 부서 응답 DTO
 * (Inner API 응답용)
 */
data class DepartmentSimpleDto(
    val deptCd: String,
    val deptNm: String,
    val upDeptCd: String?,
    val deptLvl: Int,
    val sortOrder: Int
)