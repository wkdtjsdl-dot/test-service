package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.ifFieldInfo.IfFieldInfoAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoResponse
import com.idrsys.ailis.sales.application.required.repository.ifFieldInfo.IfFieldInfoCustomRepository
import com.idrsys.ailis.sales.application.usecase.ifFieldInfo.IfFieldInfoUseCase
import com.idrsys.ailis.sales.shared.mapper.IfFieldInfoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class IfFieldInfoService(
    private val ifFieldInfoCustomRepository: IfFieldInfoCustomRepository,
    private val ifFieldInfoMapper: IfFieldInfoMapper,
) : IfFieldInfoUseCase {

    override suspend fun getAllIfFieldInfoList(): List<IfFieldInfoResponse> {
        return ifFieldInfoCustomRepository.findAllIfFieldInfos()
            .map { ifFieldInfoMapper.toResponseFromQuery(it) }
            .toList()
    }

    override fun getIfFieldInfoAutoCompleteList(searchParam: IfFieldInfoAutoCompleteSearchParam): Flow<IfFieldInfoAutoCompleteResponse> {
        return ifFieldInfoCustomRepository.findIfFieldInfoAutoCompleteList(searchParam)
    }
}
