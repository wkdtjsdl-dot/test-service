package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.cust.CustRegisterCommand
import com.idrsys.ailis.sales.application.dto.query.CustWithSalsPicInfo
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import com.idrsys.ailis.sales.application.dto.response.CustResponse
import com.idrsys.ailis.sales.domain.model.Cust
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import java.time.LocalDateTime
import java.util.concurrent.Flow

@Mapper(componentModel = "spring")
interface CustMapper {

    @Mappings(
        Mapping(target = "creator", source = "creator"),
        Mapping(target = "createDtime", source = "now"),
        Mapping(target = "updater", source = "creator"),
        Mapping(target = "updateDtime", source = "now"),
    )
    fun toDomain(command: CustRegisterCommand, creator: String, now: LocalDateTime) : Cust

    fun toListResponse(model: CustWithSalsPicInfo): CustListResponse

    fun toDetailResponse(cust: Cust): CustResponse

}