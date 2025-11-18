package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.response.HospitalMstResponse
import com.idrsys.ailis.sales.domain.model.HospitalMst
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface HospitalDataMapper {
    fun toResponse(domain: HospitalMst): HospitalMstResponse
}
