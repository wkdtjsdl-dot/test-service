package com.idrsys.ailis.sales.application.dto.request.testCodeMapping

data class CustTstCdPair(
    val custCd: String,
    val tstCd: String,
)

data class CustTstCdBulkSearchParam(
    val pairs: List<CustTstCdPair>,
)
