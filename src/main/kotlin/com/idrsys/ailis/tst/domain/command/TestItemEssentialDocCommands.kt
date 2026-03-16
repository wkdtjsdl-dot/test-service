package com.idrsys.ailis.tst.domain.command

data class TestItemEssentialDocCreateCommand(
    val tstCd: String,
    val docCd: String
)

data class TestItemEssentialDocUpdateCommand(
    val docCd: String
)
