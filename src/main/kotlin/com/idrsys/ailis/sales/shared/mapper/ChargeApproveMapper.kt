package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.ChargeApproveQuery
import com.idrsys.ailis.sales.application.dto.response.ApprovalLineInfo
import com.idrsys.ailis.sales.application.dto.response.ChargeApproveResponse
import com.idrsys.ailis.sales.domain.model.ApprInfo
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * 고객수가 승인 Mapper (MapStruct)
 */
@Mapper(componentModel = "spring")
interface ChargeApproveMapper {

    /**
     * ChargeApproveQuery → ChargeApproveResponse
     */
    @Mapping(target = "tstNm", ignore = true)
    @Mapping(target = "lowestCharge", ignore = true)
    @Mapping(target = "lastApprStatNm", ignore = true)
    @Mapping(target = "apprLvlNm", ignore = true)
    @Mapping(target = "approvalLines", ignore = true)
    fun toResponse(query: ChargeApproveQuery): ChargeApproveResponse

    /**
     * ApprInfo → ApprovalLineInfo
     */
    @Mapping(target = "apprPersonNm", ignore = true)
    @Mapping(target = "apprStatNm", ignore = true)
    fun toApprovalLineInfo(apprInfo: ApprInfo): ApprovalLineInfo
}
