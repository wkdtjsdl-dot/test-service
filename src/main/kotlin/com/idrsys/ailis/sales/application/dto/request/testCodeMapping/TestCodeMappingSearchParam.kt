package com.idrsys.ailis.sales.application.dto.request.testCodeMapping

data class TestCodeMappingSearchParam(
    val custCdNm: String? = null, // 고객코드/명 사용자 입력값 or 자동완성 선택시 고객코드명
    val custCd: String? = null, // 자동완성 선택시 고객코드
)
