package com.idrsys.ailis.tst.application.dto

import java.time.LocalDateTime

data class RequestDocumentResponse(
    val docCd: String,
    val docDivCd: String,
    val docNm: String,
    val docEngNm: String,
    val docFileId: String,
    val docEngFileId: String,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)

data class RequestDocumentRegisterRequest(
    val docCd: String,
    val docDivCd: String,
    val docNm: String,
    val docEngNm: String,
    val docFileId: String,
    val docEngFileId: String
)

data class RequestDocumentUpdateRequest(
    val docDivCd: String,
    val docNm: String,
    val docEngNm: String,
    val docFileId: String,
    val docEngFileId: String
)
