package com.idrsys.ailis.sales.application.usecase.cust

import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustUseCase {
    suspend fun getCustPage(param: CustSearchParam, pageable: Pageable): Page<CustListResponse>
}