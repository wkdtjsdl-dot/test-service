package com.idrsys.ailis.tst.application.dto

import java.time.LocalDateTime

data class TestCategoryResponse(
    val tstCateId: String,
    val tstLargeCateCd: String,
    val tstMediumCateCd: String,
    val cateNm: String,
    val cateAbbrNm: String,
    val cateEngNm: String,
    val cateEngAbbrNm: String,
    val useYn: Boolean,
    val sortOrder: Int,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDetime: LocalDateTime
)

data class TestCategoryRegisterRequest(
    val tstLargeCateCd: String,
    val tstMediumCateCd: String,
    val cateNm: String,
    val cateAbbrNm: String,
    val cateEngNm: String,
    val cateEngAbbrNm: String,
    val useYn: Boolean,
    val sortOrder: Int
)

data class TestCategoryUpdateRequest(
    val cateNm: String,
    val cateAbbrNm: String,
    val cateEngNm: String,
    val cateEngAbbrNm: String,
    val useYn: Boolean,
    val sortOrder: Int
)
