package com.idrsys.ailis.sales.application.required.repository.cust

import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.query.CustCdNmAutoCompleteInfo
import com.idrsys.ailis.sales.application.dto.query.CustWithSalsPicInfo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface CustCustomRepository {
    fun findCustsWithSalsPicInfo(searchParam: CustSearchParam,pageable: Pageable): Flow<CustWithSalsPicInfo>
    suspend fun countCusts(searchParam: CustSearchParam): Long
    suspend fun existByCustCd(custCd: String): Boolean
    fun findAutoCompleteCustCdNm(searchParam: CustSearchParam) : Flow<CustCdNmAutoCompleteInfo>
}