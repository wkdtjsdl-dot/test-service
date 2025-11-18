package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.CustAddInfoQuery
import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoCommand
import com.idrsys.ailis.sales.application.dto.response.CustAddInfoResponse
import com.idrsys.ailis.sales.domain.model.CustAddInfo
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.time.LocalDateTime


@Mapper(componentModel = "spring")
interface CustAddInfoMapper {

    @Mapping(target = "custAddInfoId", ignore = true)
    @Mapping(target = "custMstId", source = "command.custMstId")
    @Mapping(target = "custCd", source = "command.custCd")
    @Mapping(target = "spnoteDivCd", source = "command.spnoteDivCd")
    @Mapping(target = "spnote", source = "command.spnote")
    @Mapping(target = "useYn", source = "command.useYn")
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "updater", source = "creator")
    @Mapping(target = "createDtime", source = "now")
    @Mapping(target = "updateDtime", source = "now")
    fun toDomain(command: CustAddInfoCommand, creator: String, now: LocalDateTime): CustAddInfo

    fun toResponse(domain: CustAddInfo): CustAddInfoResponse

    fun toResponseFromQuery(query: CustAddInfoQuery): CustAddInfoResponse
}
