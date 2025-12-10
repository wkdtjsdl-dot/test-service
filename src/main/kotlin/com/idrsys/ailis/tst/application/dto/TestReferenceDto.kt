package com.idrsys.ailis.tst.application.dto

import java.time.LocalDateTime

// --- Test Reference ---

data class TestReferenceAutoCompleteParam(
    val refCdNm: String
)

data class TestReferenceAutoCompleteResponse(
    val refCd: String,
    val refNm: String
)

data class TestReferenceRegisterRequest(
    val refCd: String,
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

data class TestReferenceUpdateRequest(
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

data class TestReferenceResponse(
    val refCd: String,
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
    val dftEngData: String,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDetime: LocalDateTime
)

// --- Test Reference Group ---

data class TestReferenceGroupRegisterRequest(
    val refGroupCd: String,
    val refNm: String,
    val refAbbrNm: String,
    val refEngNm: String,
    val refEngAbbrNm: String,
    val sortOrder: Int
)

data class TestReferenceGroupUpdateRequest(
    val refNm: String,
    val refAbbrNm: String,
    val refEngNm: String,
    val refEngAbbrNm: String,
    val sortOrder: Int
)

data class TestReferenceGroupResponse(
    val refGroupCd: String,
    val refNm: String,
    val refAbbrNm: String,
    val refEngNm: String,
    val refEngAbbrNm: String,
    val sortOrder: Int,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDetime: LocalDateTime
)

// --- Test Reference Group Item ---

data class TestReferenceGroupItemRegisterRequest(
    val refGroupCd: String,
    val refCd: String,
    val sortOrder: Int
)

data class TestReferenceGroupItemUpdateRequest(
    val refGroupCd: String,
    val refCd: String,
    val sortOrder: Int
)

data class TestReferenceGroupItemResponse(
    val tstRefGroupItemId: String,
    val refGroupCd: String,
    val refCd: String,
    val refNm: String?,
    val sortOrder: Int,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDetime: LocalDateTime
)
