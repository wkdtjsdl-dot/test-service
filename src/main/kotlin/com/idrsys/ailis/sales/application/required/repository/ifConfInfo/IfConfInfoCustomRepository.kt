package com.idrsys.ailis.sales.application.required.repository.ifConfInfo

import com.idrsys.ailis.sales.application.dto.query.IfConfInfoQuery
import kotlinx.coroutines.flow.Flow

interface IfConfInfoCustomRepository {
    fun findByIfCustInfoId(ifCustInfoId: String): Flow<IfConfInfoQuery>
}
