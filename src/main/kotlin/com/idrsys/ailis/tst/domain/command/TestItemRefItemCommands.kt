package com.idrsys.ailis.tst.domain.command

data class TestItemRefItemCreateCommand(
    val tstCd: String,
    val refCd: String,
    val estlYn: Boolean,
    val sortOrder: Int
)

data class TestItemRefItemUpdateCommand(
    val estlYn: Boolean,
    val sortOrder: Int
)
