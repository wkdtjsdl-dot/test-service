package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.IfConfInfoQuery
import com.idrsys.ailis.sales.application.dto.response.IfConfInfoResponse
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface IfConfInfoMapper {

    fun toResponseFromQuery(query: IfConfInfoQuery): IfConfInfoResponse
}
