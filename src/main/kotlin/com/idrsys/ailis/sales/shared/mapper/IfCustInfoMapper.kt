package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.IfCustInfoQuery
import com.idrsys.ailis.sales.application.dto.request.ifCustInfo.IfCustInfoCommand
import com.idrsys.ailis.sales.application.dto.response.IfCustInfoResponse
import com.idrsys.ailis.sales.domain.model.IfCustInfo
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.time.LocalDateTime

@Mapper(componentModel = "spring")
interface IfCustInfoMapper {

    @Mapping(target = "ifCustInfoId", ignore = true)
    @Mapping(target = "custMstId", source = "command.custMstId")
    @Mapping(target = "custCd", source = "command.custCd")
    @Mapping(target = "headerInclYn", source = "command.headerInclYn")
    @Mapping(target = "skipRowCnt", source = "command.skipRowCnt")
    @Mapping(target = "ifDesc", source = "command.ifDesc")
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "updater", source = "creator")
    @Mapping(target = "createDtime", source = "now")
    @Mapping(target = "updateDtime", source = "now")
    fun toDomain(command: IfCustInfoCommand, creator: String, now: LocalDateTime): IfCustInfo

    @Mapping(target = "custNm", ignore = true)
    @Mapping(target = "confInfoList", expression = "java(java.util.Collections.emptyList())")
    fun toResponse(domain: IfCustInfo): IfCustInfoResponse

    @Mapping(target = "confInfoList", expression = "java(java.util.Collections.emptyList())")
    fun toResponseFromQuery(query: IfCustInfoQuery): IfCustInfoResponse
}
