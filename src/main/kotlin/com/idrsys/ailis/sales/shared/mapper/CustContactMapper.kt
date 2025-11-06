package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.CustContactQuery
import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactCommand
import com.idrsys.ailis.sales.application.dto.response.CustContactResponse
import com.idrsys.ailis.sales.domain.model.CustContact
import com.idrsys.ailis.sales.generated.jooq.tables.records.ScsCustContactRecord
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface CustContactMapper {

    fun toResponse(dto: CustContactQuery): CustContactResponse
    fun toResponse(domain: CustContact): CustContactResponse

    @Mappings(
        Mapping(target = "custContactId", ignore = true),
        Mapping(target = "creator", ignore = true),
        Mapping(target = "createDtime", ignore = true),
        Mapping(target = "updater", ignore = true),
        Mapping(target = "updateDtime", ignore = true),
        Mapping(target = "useYn", defaultValue = "true")
    )
    fun toRecord(command: CustContactCommand): ScsCustContactRecord

    fun toRecord(domain: CustContact): ScsCustContactRecord
}
