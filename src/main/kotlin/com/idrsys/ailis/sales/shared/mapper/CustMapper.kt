package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.CustWithSalsPicInfo
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface CustMapper {
    fun toResponse(model: CustWithSalsPicInfo): CustListResponse
}