package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.SalsActionQuery
import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionCommand
import com.idrsys.ailis.sales.application.dto.response.SalsActionResponse
import com.idrsys.ailis.sales.domain.model.SalsAction
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.time.LocalDateTime

@Mapper(componentModel = "spring")
interface SalsActionMapper {

    @Mapping(target = "salsActionId", ignore = true)
    @Mapping(target = "custMstId", source = "command.custMstId")
    @Mapping(target = "custCd", source = "command.custCd")
    @Mapping(target = "visitDtime", source = "command.visitDtime")
    @Mapping(target = "visitPrpsCd", source = "command.visitPrpsCd")
    @Mapping(target = "visitTargetPersonNm", source = "command.visitTargetPersonNm")
    @Mapping(target = "visitTargetPersonContact", source = "command.visitTargetPersonContact")
    @Mapping(target = "memo", source = "command.memo")
    @Mapping(target = "useYn", source = "command.useYn")
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "updater", source = "creator")
    @Mapping(target = "createDtime", source = "now")
    @Mapping(target = "updateDtime", source = "now")
    fun toDomain(command: SalsActionCommand, creator: String, now: LocalDateTime): SalsAction

    fun toResponse(domain: SalsAction): SalsActionResponse

    fun toResponseFromQuery(query: SalsActionQuery): SalsActionResponse
}
