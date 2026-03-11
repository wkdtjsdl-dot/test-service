package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.CustContactQuery
import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactCommand
import com.idrsys.ailis.sales.application.dto.response.CustContactResponse
import com.idrsys.ailis.sales.domain.model.CustContact
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.time.LocalDateTime

@Mapper(componentModel = "spring")
interface CustContactMapper {

    @Mapping(target = "custContactId", ignore = true)
    @Mapping(target = "custMstId", source = "command.custMstId")
    @Mapping(target = "custCd", source = "command.custCd")
    @Mapping(target = "acctChargeNm", source = "command.acctChargeNm")
    @Mapping(target = "ofpoJbpo", source = "command.ofpoJbpo")
    @Mapping(target = "telno", source = "command.telno")
    @Mapping(target = "phno", source = "command.phno")
    @Mapping(target = "email", source = "command.email")
    @Mapping(target = "remark", source = "command.remark")
    @Mapping(target = "useYn", source = "command.useYn")
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "updater", source = "creator")
    @Mapping(target = "createDtime", source = "now")
    @Mapping(target = "updateDtime", source = "now")
    fun toDomain(command: CustContactCommand, creator: String, now: LocalDateTime): CustContact

    @Mapping(target = "empNm", ignore = true)
    fun toResponse(domain: CustContact): CustContactResponse

    @Mapping(target = "empNm", source = "empNm")
    fun toResponseFromQuery(query: CustContactQuery): CustContactResponse
}
