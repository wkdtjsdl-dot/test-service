package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.GcgnSalsPicInfoQuery
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoCommand
import com.idrsys.ailis.sales.application.dto.response.GcgnSalsPicInfoResponse
import com.idrsys.ailis.sales.domain.model.GcgnSalsPicInfo
import com.idrsys.ailis.sales.generated.jooq.tables.records.ScsGcgnSalsPicInfoRecord
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface GcgnSalsPicInfoMapper {

    fun toResponse(dto: GcgnSalsPicInfoQuery): GcgnSalsPicInfoResponse
    fun toResponse(domain: GcgnSalsPicInfo): GcgnSalsPicInfoResponse

    @Mappings(
        Mapping(target = "gcgnSalsPicInfoId", ignore = true),
        Mapping(target = "creator", ignore = true),
        Mapping(target = "createDtime", ignore = true),
        Mapping(target = "updater", ignore = true),
        Mapping(target = "updateDtime", ignore = true)
    )
    fun toRecord(command: GcgnSalsPicInfoCommand): ScsGcgnSalsPicInfoRecord

    fun toRecord(domain: GcgnSalsPicInfo): ScsGcgnSalsPicInfoRecord
}
