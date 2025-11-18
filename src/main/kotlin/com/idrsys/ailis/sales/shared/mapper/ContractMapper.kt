package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.ContractWithDetails
import com.idrsys.ailis.sales.application.dto.request.contract.ContractCommand
import com.idrsys.ailis.sales.application.dto.response.ContractListResponse
import com.idrsys.ailis.sales.application.dto.response.ContractResponse
import com.idrsys.ailis.sales.generated.jooq.tables.records.ScsCustCntrRecord
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

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

    @Mappings(
        Mapping(target = "custCntrId", ignore = true),
        Mapping(target = "creator", ignore = true),
        Mapping(target = "createDtime", ignore = true),
        Mapping(target = "updater", ignore = true),
        Mapping(target = "updateDtime", ignore = true),
        Mapping(target = "useYn", defaultValue = "true"),
        Mapping(target = "custMstId", ignore = true)
    )
    fun toRecord(command: ContractCommand): ScsCustCntrRecord

}
