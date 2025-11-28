package com.idrsys.ailis.tst.domain.command

data class RequestDocumentCreateCommand(
    val docCd: String,
    val docDivCd: String,
    val docNm: String,
    val docEngNm: String,
    val docFileId: String?,
    val docEngFileId: String?
)

data class RequestDocumentUpdateCommand(
    val docDivCd: String,
    val docNm: String,
    val docEngNm: String,
    val docFileId: String?,
    val docEngFileId: String?
)
