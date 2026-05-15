package com.idrsys.ailis.sales.application.dto.request.testCodeMapping

data class TestCodeMappingSearchParam(
    val custCdNm: String? = null, // 고객코드/명 사용자 입력값 or 자동완성 선택시 고객코드명
    val custCd: String? = null, // 자동완성 선택시 고객코드
    val custTstCd: String? = null,
    val custTstNm: String? = null,
    val tstCd: String? = null,
    val tstNm: String? = null,
)

data class InnerTestCodeSearchParam(
    val userId: String,
    val code: String? = null,
    val serial: String? = null,
    val nameKr: String? = null,
    val nameEn: String? = null
)