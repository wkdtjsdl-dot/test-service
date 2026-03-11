package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.ContractWithDetails
import com.idrsys.ailis.sales.application.dto.request.contract.ContractCommand
import com.idrsys.ailis.sales.application.dto.response.ContractListResponse
import com.idrsys.ailis.sales.application.dto.response.ContractResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import java.time.LocalDateTime

@Mapper(componentModel = "spring")
interface ContractMapper {

    @Mappings(
        Mapping(target = "custNm", ignore = true),
        Mapping(target = "cntrPicNm", ignore = true)
    )
    fun toResponse(contract: com.idrsys.ailis.sales.domain.model.Contract): ContractResponse

    @Mappings(
        Mapping(target = "custNm", ignore = true),
        Mapping(target = "cntrPicNm", ignore = true)
    )
    fun toResponse(dto: ContractWithDetails): ContractResponse

    fun toListResponse(dto: ContractWithDetails): ContractListResponse

    @Mapping(target = "custCntrId", ignore = true)
    @Mapping(target = "custMstId", source = "custMstId")
    @Mapping(target = "custCd", source = "command.custCd")
    @Mapping(target = "cntrNo", source = "command.cntrNo")
    @Mapping(target = "cntrDt", source = "command.cntrDt")
    @Mapping(target = "cntrStartDt", source = "command.cntrStartDt")
    @Mapping(target = "cntrEndDt", source = "command.cntrEndDt")
    @Mapping(target = "cntrType", source = "command.cntrType")
    @Mapping(target = "recntrMonth", source = "command.recntrMonth")
    @Mapping(target = "cntrNm", source = "command.cntrNm")
    @Mapping(target = "cntrCont", source = "command.cntrCont")
    @Mapping(target = "cntrPicId", source = "command.cntrPicId")
    @Mapping(target = "atchGrupId", source = "command.atchGrupId")
    @Mapping(target = "useYn", source = "command.useYn")
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "updater", source = "creator")
    @Mapping(target = "createDtime", source = "now")
    @Mapping(target = "updateDtime", source = "now")
    fun toDomain(command: ContractCommand, custMstId: String, creator: String, now: LocalDateTime): com.idrsys.ailis.sales.domain.model.Contract

}
