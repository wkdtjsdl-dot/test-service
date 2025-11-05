package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.CustWithSalsPicInfo
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import com.idrsys.ailis.sales.application.dto.response.CustResponse
import com.idrsys.ailis.sales.domain.model.Cust
import org.mapstruct.Mapper
import java.util.concurrent.Flow

@Mapper(componentModel = "spring")
interface CustMapper {
    fun toListResponse(model: CustWithSalsPicInfo): CustListResponse

    fun toDetailResponse(cust: Cust): CustResponse

}