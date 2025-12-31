package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.IfFieldInfoQuery
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoAutoCompleteResponse
import io.r2dbc.spi.Row

internal fun Row.toIfFieldInfoQuery(): IfFieldInfoQuery = IfFieldInfoQuery(
    ifFieldInfoId = this.get("if_field_info_id", String::class.java)!!,
    ifFieldNm = this.get("if_field_nm", String::class.java)!!,
    ifFieldColNm = this.get("if_field_col_nm", String::class.java),
    ifFieldDesc = this.get("if_field_desc", String::class.java)
)

internal fun Row.toIfFieldInfoAutoCompleteResponse(): IfFieldInfoAutoCompleteResponse = IfFieldInfoAutoCompleteResponse(
    ifFieldInfoId = this.get("if_field_info_id", String::class.java)!!,
    ifFieldNm = this.get("if_field_nm", String::class.java)!!
)
