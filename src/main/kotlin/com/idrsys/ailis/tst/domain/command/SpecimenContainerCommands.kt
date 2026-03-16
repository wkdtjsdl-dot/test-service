package com.idrsys.ailis.tst.domain.command

data class SpecimenContainerCreateCommand(
    val spcmCntnCd: String,
    val cntnNm: String,
    val cntnEngNm: String,
    val cntnFileId: String?
)

data class SpecimenContainerUpdateCommand(
    val cntnNm: String,
    val cntnEngNm: String,
    val cntnFileId: String?
)
