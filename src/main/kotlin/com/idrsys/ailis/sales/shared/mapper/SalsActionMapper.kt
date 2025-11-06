package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.SalsActionQuery
import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionCommand
import com.idrsys.ailis.sales.application.dto.response.SalsActionResponse
import com.idrsys.ailis.sales.domain.model.SalsAction
import com.idrsys.ailis.sales.generated.jooq.tables.records.ScsSalsActionRecord
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface SalsActionMapper {

    fun toResponse(dto: SalsActionQuery): SalsActionResponse
    fun toResponse(domain: SalsAction): SalsActionResponse

    @Mappings(
        Mapping(target = "salsActionId", ignore = true),
        Mapping(target = "creator", ignore = true),
        Mapping(target = "createDtime", ignore = true),
        Mapping(target = "updater", ignore = true),
        Mapping(target = "updateDtime", ignore = true),
        Mapping(target = "useYn", defaultValue = "true")
    )
    fun toRecord(command: SalsActionCommand): ScsSalsActionRecord

    fun toRecord(domain: SalsAction): ScsSalsActionRecord
}
