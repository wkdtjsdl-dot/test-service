package com.idrsys.ailis.tst.application.dto.request

data class WorkListSearchParam(
    val wrklistCd: String? = null,
    val wrklistCdLike: String? = null,
    val wrklistNm: String? = null,
    val useYn: Boolean? = null
)
