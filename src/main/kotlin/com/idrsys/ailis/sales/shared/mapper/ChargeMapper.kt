package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.ChargeWithDetails
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeRegisterCommand
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import com.idrsys.ailis.sales.domain.model.Charge
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.ReportingPolicy
import java.time.LocalDateTime

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ChargeMapper {
    @Mappings(
        Mapping(target = "bzoffiNm", ignore = true)
    )
    fun toResponse(dto: ChargeWithDetails): ChargeResponse

    @Mappings(
        Mapping(target = "custChargeId", source = "custChargeId"),
        Mapping(target = "apprInfoNo", ignore = true),
        Mapping(target = "currApprSeq", ignore = true),
        Mapping(target = "apprSubmsEmpNo", ignore = true),
        Mapping(target = "apprSubmsDtime", ignore = true),
        Mapping(target = "apprLvlCd", ignore = true),
        Mapping(target = "creator", source = "creator"),
        Mapping(target = "updater", source = "creator"),
        Mapping(target = "createDtime", source = "now"),
        Mapping(target = "updateDtime", source = "now")
    )
    fun toDomain(command: ChargeRegisterCommand, custChargeId: String, creator: String, now: LocalDateTime): Charge

    @Mappings(
        Mapping(target = "salesPics", ignore = true),
        Mapping(target = "custNm", ignore = true),
        Mapping(target = "bzoffiNm", ignore = true),
        Mapping(target = "bzoffiCd", ignore = true)
    )
    fun toResponse(charge: Charge): ChargeResponse
}
