package com.idrsys.ailis.tst.domain.command

data class TestReferenceCreateCommand(
    val refCateCd: String,
    val useYn: Boolean,
    val refNm: String,
    val refAbbrNm: String,
    val refEngNm: String,
    val refEngAbbrNm: String,
    val sortOrder: Int,
    val refType: String,
    val refSize: Int,
    val rangeChkYn: Boolean,
    val refMinVal: Int,
    val refMaxVal: Int,
    val dataFormat: String,
    val dftData: String,
    val dftEngData: String
)

data class TestReferenceUpdateCommand(
    val refCateCd: String,
    val useYn: Boolean,
    val refNm: String,
    val refAbbrNm: String,
    val refEngNm: String,
    val refEngAbbrNm: String,
    val sortOrder: Int,
    val refType: String,
    val refSize: Int,
    val rangeChkYn: Boolean,
    val refMinVal: Int,
    val refMaxVal: Int,
    val dataFormat: String,
    val dftData: String,
    val dftEngData: String
)

data class TestReferenceGroupCreateCommand(
    val refNm: String,
    val refAbbrNm: String,
    val refEngNm: String,
    val refEngAbbrNm: String,
    val sortOrder: Int
)

data class TestReferenceGroupItemCreateCommand(
    val refGroupCd: String,
    val refCd: String,
    val sortOrder: Int
)

data class TestReferenceGroupUpdateCommand(
    val refNm: String,
    val refAbbrNm: String,
    val refEngNm: String,
    val refEngAbbrNm: String,
    val sortOrder: Int
)

data class TestReferenceGroupItemUpdateCommand(
    val refGroupCd: String,
    val refCd: String,
    val sortOrder: Int
)
