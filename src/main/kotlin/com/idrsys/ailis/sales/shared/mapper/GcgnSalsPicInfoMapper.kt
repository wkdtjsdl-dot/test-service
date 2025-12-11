package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.GcgnSalsPicInfoQuery
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoCommand
import com.idrsys.ailis.sales.application.dto.response.GcgnSalsPicInfoResponse
import com.idrsys.ailis.sales.domain.model.GcgnSalsPicInfo
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.time.LocalDateTime

@Mapper(componentModel = "spring")
interface GcgnSalsPicInfoMapper {

    @Mapping(target = "empUserNm", ignore = true)
    fun toResponse(dto: GcgnSalsPicInfoQuery): GcgnSalsPicInfoResponse

    @Mapping(target = "empUserNm", ignore = true)
    fun toResponse(domain: GcgnSalsPicInfo): GcgnSalsPicInfoResponse

    @Mapping(target = "gcgnSalsPicInfoId", ignore = true)
    @Mapping(target = "custMstId", source = "command.custMstId")
    @Mapping(target = "applyStartDt", source = "command.applyStartDt")
    @Mapping(target = "salsTeamCd", source = "command.salsTeamCd")
    @Mapping(target = "empUserId", source = "command.empUserId")
    @Mapping(target = "custCd", source = "command.custCd")
    @Mapping(target = "applyEndDt", source = "command.applyEndDt")
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "updater", source = "creator")
    @Mapping(target = "createDtime", source = "now")
    @Mapping(target = "updateDtime", source = "now")
    fun toDomain(command: GcgnSalsPicInfoCommand, creator: String, now: LocalDateTime): GcgnSalsPicInfo
}
