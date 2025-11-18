package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.ReqRstIfMethodQuery
import com.idrsys.ailis.sales.application.dto.request.reqrstifmethod.ReqRstIfMethodCommand
import com.idrsys.ailis.sales.application.dto.response.ReqRstIfMethodResponse
import com.idrsys.ailis.sales.domain.model.ReqRstIfMethod
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.time.LocalDateTime

@Mapper(componentModel = "spring")
interface ReqRstIfMethodMapper {

    @Mapping(target = "reqRstIfMethodId", source = "command.reqRstIfMethodId")
    @Mapping(target = "custMstId", source = "command.custMstId")
    @Mapping(target = "applyStartDt", source = "command.applyStartDt")
    @Mapping(target = "custCd", source = "command.custCd")
    @Mapping(target = "applyEndDt", source = "command.applyEndDt")
    @Mapping(target = "reqMethodCd", source = "command.reqMethodCd")
    @Mapping(target = "reqIfTypeCd", source = "command.reqIfTypeCd")
    @Mapping(target = "useYn", source = "command.useYn")
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "updater", source = "creator")
    @Mapping(target = "createDtime", source = "now")
    @Mapping(target = "updateDtime", source = "now")
    fun toDomain(command: ReqRstIfMethodCommand, creator: String, now: LocalDateTime): ReqRstIfMethod

    fun toResponse(domain: ReqRstIfMethod): ReqRstIfMethodResponse

    fun toResponseFromQuery(query: ReqRstIfMethodQuery): ReqRstIfMethodResponse
}
