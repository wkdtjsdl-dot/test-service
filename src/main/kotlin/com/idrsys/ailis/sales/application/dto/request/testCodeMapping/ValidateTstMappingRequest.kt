package com.idrsys.ailis.sales.application.dto.request.testCodeMapping

data class ValidateTstMappingRequest(
    val custCd: String,
    val tstCds: List<String>
)
