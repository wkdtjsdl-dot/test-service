package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.cust.CustRegisterCommand
import com.idrsys.ailis.sales.application.dto.cust.CustUpdateCommand
import com.idrsys.ailis.sales.application.dto.query.CustBasicInfo
import com.idrsys.ailis.sales.application.dto.query.CustCdNmAutoCompleteInfo
import com.idrsys.ailis.sales.application.dto.query.CustDetailInfo
import com.idrsys.ailis.sales.application.dto.query.RprsCustCdNmAutoCompleteInfo
import com.idrsys.ailis.sales.application.dto.query.CustWithSalsPicInfo
import com.idrsys.ailis.sales.application.dto.query.DirectAcctCdNmAutoCompleteInfo
import com.idrsys.ailis.sales.application.dto.response.CustBasicResponse
import com.idrsys.ailis.sales.application.dto.response.CustCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.RprsCustCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import com.idrsys.ailis.sales.application.dto.response.CustResponse
import com.idrsys.ailis.sales.application.dto.response.DirectAcctCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.domain.model.Cust
import com.idrsys.ailis.sales.domain.model.CustMstHst
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import java.time.LocalDateTime

@Mapper(componentModel = "spring")
abstract class CustMapper {

    @Mappings(
        Mapping(target = "custMstId", ignore = true),
        Mapping(target = "creator", source = "creator"),
        Mapping(target = "createDtime", source = "now"),
        Mapping(target = "updater", source = "creator"),
        Mapping(target = "updateDtime", source = "now"),
    )
    abstract fun toDomain(command: CustRegisterCommand, creator: String, now: LocalDateTime) : Cust

    @Mappings(
        Mapping(target = "custMstHstId", ignore = true),
        Mapping(target = "updateReason", source = "updateReason")
    )
    abstract fun toHistDomain(cust: Cust, updateReason: String?): CustMstHst

    @Mappings(
        Mapping(target = "deptNm", ignore = true),
        Mapping(target = "cntr", ignore = true),
        Mapping(target = "createDtime", expression = "java(formatDateTime(model.getCreateDtime()))")
    )
    abstract fun toListResponse(model: CustWithSalsPicInfo): CustListResponse

    protected fun formatDateTime(dateTime: LocalDateTime?): String {
        if (dateTime == null) return ""
        val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dateTime.format(formatter)
    }

    @Mappings(
        Mapping(target = "careInstNm", ignore = true)
    )
    abstract fun toDetailResponse(model: CustDetailInfo): CustResponse

    abstract fun toUpdateCommand(command: CustRegisterCommand): CustUpdateCommand

    abstract fun toCustCdNmAutoCompleteResponse(model: CustCdNmAutoCompleteInfo): CustCdNmAutoCompleteResponse

    abstract fun toRprsCustCdNmAutoCompleteResponse(model: RprsCustCdNmAutoCompleteInfo): RprsCustCdNmAutoCompleteResponse

    abstract fun toDirectAcctCdNmAutoCompleteResponse(model: DirectAcctCdNmAutoCompleteInfo): DirectAcctCdNmAutoCompleteResponse

    abstract fun toBasicResponse(model: CustBasicInfo): CustBasicResponse
}
