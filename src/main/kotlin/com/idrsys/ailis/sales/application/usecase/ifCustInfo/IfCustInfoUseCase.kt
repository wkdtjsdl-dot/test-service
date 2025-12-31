package com.idrsys.ailis.sales.application.usecase.ifCustInfo

import com.idrsys.ailis.sales.application.dto.request.ifCustInfo.IfCustInfoCommand
import com.idrsys.ailis.sales.application.dto.request.ifCustInfo.IfCustInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.IfCustInfoResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IfCustInfoUseCase {
    suspend fun getIfCustInfoPage(searchParam: IfCustInfoSearchParam, pageable: Pageable): Page<IfCustInfoResponse>
    suspend fun getIfCustInfoDetail(ifCustInfoId: String): IfCustInfoResponse
    suspend fun createIfCustInfo(command: IfCustInfoCommand, adminId: String): IfCustInfoResponse
    suspend fun updateIfCustInfo(ifCustInfoId: String, command: IfCustInfoCommand, adminId: String): IfCustInfoResponse
    suspend fun deleteIfCustInfo(ifCustInfoId: String)
}
