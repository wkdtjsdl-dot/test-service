package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.ChargeWithDetails
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ChargeMapper {
    fun toResponse(dto: ChargeWithDetails): ChargeResponse
}
