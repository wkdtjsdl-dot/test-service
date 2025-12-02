package com.idrsys.ailis.tst.application.dto

import java.time.LocalDateTime

// --- Department Group ---

data class DepartmentGroupRegisterRequest(
    val deptCd: String,
    val tstCateCd: String,
    val tstCateNm: String,
    val updateAuthCd: String,
    val dupAllowYn: Boolean,
    val sortOrder: Int
)

data class DepartmentGroupUpdateRequest(
    val deptCd: String,
    val tstCateCd: String,
    val tstCateNm: String,
    val updateAuthCd: String,
    val dupAllowYn: Boolean,
    val sortOrder: Int
)

data class DepartmentGroupResponse(
    val deptGroupId: String,
    val deptCd: String,
    val tstCateCd: String,
    val tstCateNm: String,
    val updateAuthCd: String,
    val dupAllowYn: Boolean,
    val sortOrder: Int,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)

// --- Department Group Item ---

data class DepartmentGroupItemRegisterRequest(
    val deptCd: String,
    val tstCateCd: String,
    val tstCateItemCd: String,
    val tstCateItemNm: String,
    val sortOrder: Int
)

data class DepartmentGroupItemUpdateRequest(
    val deptCd: String,
    val tstCateCd: String,
    val tstCateItemCd: String,
    val tstCateItemNm: String,
    val sortOrder: Int
)

data class DepartmentGroupItemResponse(
    val deptGrpItmId: String,
    val deptCd: String,
    val tstCateCd: String,
    val tstCateItemCd: String,
    val tstCateItemNm: String,
    val sortOrder: Int,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)

// --- Department Group Item Test ---

data class DepartmentGroupItemTestRegisterRequest(
    val deptCd: String,
    val tstCateCd: String,
    val tstCateItemCd: String,
    val tstCd: String
)

data class DepartmentGroupItemTestResponse(
    val deptGrpItmTstId: String,
    val deptCd: String,
    val tstCateCd: String,
    val tstCateItemCd: String,
    val tstCd: String,
    val creator: String,
    val createDtime: LocalDateTime
)

// --- Department Test Item ---

data class DepartmentTestItemRegisterRequest(
    val deptCd: String,
    val tstCd: String,
    val danDivCd: String = "D",
    val tstDayweek: String = "YYYYYNN",
    val tstTatday: Int,
    val deptTstDesc: String? = null
)

data class DepartmentTestItemUpdateRequest(
    val deptCd: String,
    val tstCd: String,
    val danDivCd: String,
    val tstDayweek: String,
    val tstTatday: Int,
    val deptTstDesc: String?
)

data class DepartmentTestItemResponse(
    val deptTstItemId: String,
    val deptCd: String,
    val tstCd: String,
    val danDivCd: String,
    val tstDayweek: String,
    val tstTatday: Int,
    val deptTstDesc: String?,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)

data class DeptTestItemCategoryResponse(
    val tstLargeCateCd: String,
    val tstMediumCateCd: String,
    val deptTstItemId: String,
    val deptCd: String,
    val tstCd: String,
    val danDivCd: String,
    val tstDayweek: String,
    val tstTatday: Int,
    val deptTstDesc: String?,
)