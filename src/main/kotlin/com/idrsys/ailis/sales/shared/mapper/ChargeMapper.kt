package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.ChargeWithDetails
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface ChargeMapper {
    @Mappings(
        Mapping(target = "bzoffiNm", ignore = true)
    )
    fun toResponse(dto: ChargeWithDetails): ChargeResponse
}
