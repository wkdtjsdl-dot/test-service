package com.idrsys.ailis.sales.application.usecase.charge

import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ChargeUseCase {
    suspend fun getChargePage(searchParam: ChargeSearchParam, pageable: Pageable): Page<ChargeResponse>
}
