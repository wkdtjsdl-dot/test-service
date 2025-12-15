package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.CustReqPossTstItemQuery
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemCommand
import com.idrsys.ailis.sales.application.dto.response.CustReqPossTstItemResponse
import com.idrsys.ailis.sales.domain.model.CustReqPossTstItem
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
import java.time.LocalDateTime

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface CustReqPossTstItemMapper {

    @Mapping(target = "custReqPossTstItemId", ignore = true)
    @Mapping(target = "custMstId", source = "command.custMstId")
    @Mapping(target = "custCd", source = "command.custCd")
    @Mapping(target = "tstCd", source = "command.tstCd")
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "createDtime", source = "now")
    fun toDomain(command: CustReqPossTstItemCommand, creator: String, now: LocalDateTime): CustReqPossTstItem

    fun toResponse(domain: CustReqPossTstItem): CustReqPossTstItemResponse

    fun toResponseFromQuery(query: CustReqPossTstItemQuery): CustReqPossTstItemResponse
}
