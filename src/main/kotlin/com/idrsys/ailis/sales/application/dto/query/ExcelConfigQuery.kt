package com.idrsys.ailis.sales.application.dto.query

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "고객별 엑셀 연동 설정 조회 쿼리")
data class ExcelConfigQuery(
    @Schema(description = "헤더 포함 여부")
    val headerInclYn: Boolean,

    @Schema(description = "건너뛸 행 수")
    val skipRowCnt: Int
)
