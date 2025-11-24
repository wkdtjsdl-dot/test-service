package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.response.ExrtListResponse
import com.idrsys.ailis.sales.application.dto.response.ExrtResponse
import com.idrsys.ailis.sales.domain.model.Exrt
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ExrtMapper {

    fun toResponse(exrt: Exrt): ExrtResponse

    fun toListResponse(exrt: Exrt): ExrtListResponse
}
