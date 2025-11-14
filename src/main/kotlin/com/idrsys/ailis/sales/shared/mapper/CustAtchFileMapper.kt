package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.CustAtchFileQuery
import com.idrsys.ailis.sales.application.dto.request.custatchfile.CustAtchFileCommand
import com.idrsys.ailis.sales.application.dto.response.CustAtchFileResponse
import com.idrsys.ailis.sales.domain.model.CustAtchFile
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.time.LocalDateTime


@Mapper(componentModel = "spring")
interface CustAtchFileMapper {

    @Mapping(target = "custAtchFileId", source = "command.custAtchFileId")
    @Mapping(target = "custMstId", source = "command.custMstId")
    @Mapping(target = "custCd", source = "command.custCd")
    @Mapping(target = "atchGrupId", source = "command.atchGrupId")
    @Mapping(target = "useYn", source = "command.useYn")
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "updater", source = "creator")
    @Mapping(target = "createDtime", source = "now")
    @Mapping(target = "updateDtime", source = "now")
    fun toDomain(command: CustAtchFileCommand, creator: String, now: LocalDateTime): CustAtchFile

    fun toResponse(domain: CustAtchFile): CustAtchFileResponse

    fun toResponseFromQuery(query: CustAtchFileQuery): CustAtchFileResponse
}
