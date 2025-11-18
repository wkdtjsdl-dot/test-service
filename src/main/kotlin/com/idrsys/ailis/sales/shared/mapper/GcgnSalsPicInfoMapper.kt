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

    @Mapping(target = "empNm", ignore = true)
    fun toResponse(dto: GcgnSalsPicInfoQuery): GcgnSalsPicInfoResponse

    @Mapping(target = "empNm", ignore = true)
    fun toResponse(domain: GcgnSalsPicInfo): GcgnSalsPicInfoResponse

    @Mappings(
        Mapping(target = "gcgnSalsPicInfoId", ignore = true),
        Mapping(target = "creator", ignore = true),
        Mapping(target = "createDtime", ignore = true),
        Mapping(target = "updater", ignore = true),
        Mapping(target = "updateDtime", ignore = true),
        Mapping(target = "value1", ignore = true),
        Mapping(target = "value2", ignore = true),
        Mapping(target = "value3", ignore = true),
        Mapping(target = "value4", ignore = true),
        Mapping(target = "value5", ignore = true),
        Mapping(target = "value6", ignore = true),
        Mapping(target = "value7", ignore = true),
        Mapping(target = "value8", ignore = true),
        Mapping(target = "value9", ignore = true),
        Mapping(target = "value10", ignore = true),
        Mapping(target = "value11", ignore = true)
    )
    fun toRecord(command: GcgnSalsPicInfoCommand): ScsGcgnSalsPicInfoRecord

    @Mappings(
        Mapping(target = "value1", ignore = true),
        Mapping(target = "value2", ignore = true),
        Mapping(target = "value3", ignore = true),
        Mapping(target = "value4", ignore = true),
        Mapping(target = "value5", ignore = true),
        Mapping(target = "value6", ignore = true),
        Mapping(target = "value7", ignore = true),
        Mapping(target = "value8", ignore = true),
        Mapping(target = "value9", ignore = true),
        Mapping(target = "value10", ignore = true),
        Mapping(target = "value11", ignore = true)
    )
    fun toRecord(domain: GcgnSalsPicInfo): ScsGcgnSalsPicInfoRecord
}
