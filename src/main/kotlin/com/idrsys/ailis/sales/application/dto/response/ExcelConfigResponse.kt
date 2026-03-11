package com.idrsys.ailis.sales.application.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "고객별 엑셀 연동 설정 응답")
data class ExcelConfigResponse(
    @Schema(description = "헤더 포함 여부")
    val headerInclYn: Boolean,

    @Schema(description = "건너뛸 행 수")
    val skipRowCnt: Int
)
