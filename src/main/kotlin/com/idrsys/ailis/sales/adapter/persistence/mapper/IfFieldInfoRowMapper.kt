package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.IfFieldInfoQuery
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoResponse
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

internal fun Row.toIfFieldInfoResponse(): IfFieldInfoResponse = IfFieldInfoResponse(
    ifFieldInfoId = this.get("if_field_info_id", String::class.java)!!,
    ifFieldNm = this.get("if_field_nm", String::class.java)!!,
    ifFieldColNm = this.get("if_field_col_nm", String::class.java),
    ifFieldDesc = this.get("if_field_desc", String::class.java),
    colType = this.get("col_type", String::class.java),
    targetPath = this.get("target_path", String::class.java),
    statDtlCd = this.get("stat_dtl_cd", String::class.java),
    ifFieldExps = this.get("if_field_exps", String::class.java),
    colIdx = this.get("col_idx", Integer::class.java)?.toInt()
)

