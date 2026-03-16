package com.idrsys.ailis.tst.domain.command

data class TestCategoryCreateCommand(
    val tstLargeCateCd: String,
    val tstMediumCateCd: String,
    val cateNm: String,
    val cateAbbrNm: String,
    val cateEngNm: String,
    val cateEngAbbrNm: String,
    val useYn: Boolean,
    val sortOrder: Int
)

data class TestCategoryUpdateCommand(
    val cateNm: String,
    val cateAbbrNm: String,
    val cateEngNm: String,
    val cateEngAbbrNm: String,
    val useYn: Boolean,
    val sortOrder: Int
)
