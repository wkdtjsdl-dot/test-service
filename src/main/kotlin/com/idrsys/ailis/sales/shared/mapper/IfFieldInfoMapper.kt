package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.IfFieldInfoQuery
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoResponse
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface IfFieldInfoMapper {

    fun toResponseFromQuery(query: IfFieldInfoQuery): IfFieldInfoResponse
}
