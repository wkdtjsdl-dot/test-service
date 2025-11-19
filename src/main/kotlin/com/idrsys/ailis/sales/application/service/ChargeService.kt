package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.adapter.external.BaseServiceClient
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseDepartmentResponse
import com.idrsys.ailis.sales.application.required.repository.charge.ChargeCustomRepository
import com.idrsys.ailis.sales.application.usecase.charge.ChargeUseCase
import com.idrsys.ailis.sales.shared.mapper.ChargeMapper
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ChargeService(
    private val chargeCustomRepository: ChargeCustomRepository,
    private val chargeMapper: ChargeMapper,
    private val baseServiceClient: BaseServiceClient,
) : ChargeUseCase {

    override suspend fun getChargePage(
        searchParam: ChargeSearchParam,
        pageable: Pageable
    ): Page<ChargeResponse> {

        var finalSearchParam = searchParam

        val users = baseServiceClient.getUsers() ?: emptyList()
        val userMap = users.associateBy { it.userId }

        if (!searchParam.empUserIdNm.isNullOrBlank()) {
            val keyword = searchParam.empUserIdNm
            val userIds = userMap.values.filter { it.userNm.contains(keyword, ignoreCase = true) || it.userId.contains(keyword, ignoreCase = true) }
                .map { it.userId }
                .toList()
            finalSearchParam = searchParam.copy(empUserIds = userIds)
        }

        val departments = baseServiceClient.getDepartments() ?: emptyList()
        val deptNameByCd = departments.associate { it.deptCd to it.deptNm }

        val total = chargeCustomRepository.countCharge(finalSearchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val charges: List<ChargeResponse> = chargeCustomRepository.findCharges(finalSearchParam, pageable)
            .map(chargeMapper::toResponse)
            .toList()

        val chargeResponses = charges.map { charge ->
            val updatedSalesPics = charge.salesPics?.map { pic ->
                pic.copy(empUserNm = userMap[pic.empUserId]?.userNm)
            }
            charge.copy(
                bzoffiNm = deptNameByCd[charge.bzoffiCd],
                salesPics = updatedSalesPics
            )
        }

        return PageImpl(chargeResponses, pageable, total)
    }

}
