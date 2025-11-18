package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.cust.CustRegisterCommand
import com.idrsys.ailis.sales.application.dto.query.CustCdNmAutoCompleteInfo
import com.idrsys.ailis.sales.application.dto.query.CustDetailInfo
import com.idrsys.ailis.sales.application.dto.query.RprsCustCdNmAutoCompleteInfo
import com.idrsys.ailis.sales.application.dto.query.CustWithSalsPicInfo
import com.idrsys.ailis.sales.application.dto.query.DirectAcctCdNmAutoCompleteInfo
import com.idrsys.ailis.sales.application.dto.response.CustCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.RprsCustCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import com.idrsys.ailis.sales.application.dto.response.CustResponse
import com.idrsys.ailis.sales.application.dto.response.DirectAcctCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.domain.model.Cust
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import java.time.LocalDateTime

@Mapper(componentModel = "spring")
interface CustMapper {

    @Mappings(
        Mapping(target = "custMstId", ignore = true),
        Mapping(target = "creator", source = "creator"),
        Mapping(target = "createDtime", source = "now"),
        Mapping(target = "updater", source = "creator"),
        Mapping(target = "updateDtime", source = "now"),
    )
    fun toDomain(command: CustRegisterCommand, creator: String, now: LocalDateTime) : Cust

    fun toListResponse(model: CustWithSalsPicInfo): CustListResponse

    fun toDetailResponse(model: CustDetailInfo): CustResponse

    fun toCustCdNmAutoCompleteResponse(model: CustCdNmAutoCompleteInfo): CustCdNmAutoCompleteResponse

    fun toRprsCustCdNmAutoCompleteResponse(model: RprsCustCdNmAutoCompleteInfo): RprsCustCdNmAutoCompleteResponse

    fun toDirectAcctCdNmAutoCompleteResponse(model: DirectAcctCdNmAutoCompleteInfo): DirectAcctCdNmAutoCompleteResponse
}
