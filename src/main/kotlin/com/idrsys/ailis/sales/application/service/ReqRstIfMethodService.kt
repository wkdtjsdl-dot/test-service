package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.reqrstifmethod.ReqRstIfMethodCommand
import com.idrsys.ailis.sales.application.dto.request.reqrstifmethod.ReqRstIfMethodSearchParam
import com.idrsys.ailis.sales.application.dto.response.ReqRstIfMethodResponse
import com.idrsys.ailis.sales.application.required.repository.reqrstifmethod.ReqRstIfMethodCustomRepository
import com.idrsys.ailis.sales.application.required.repository.reqrstifmethod.ReqRstIfMethodRepository
import com.idrsys.ailis.sales.application.usecase.reqrstifmethod.ReqRstIfMethodUseCase
import com.idrsys.ailis.sales.shared.mapper.ReqRstIfMethodMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ReqRstIfMethodService(
    private val repository: ReqRstIfMethodRepository,
    private val customRepository: ReqRstIfMethodCustomRepository,
    private val mapper: ReqRstIfMethodMapper
) : ReqRstIfMethodUseCase {

    override suspend fun findById(rstIfMethodId: String): ReqRstIfMethodResponse? {
        return repository.findById(rstIfMethodId)?.let { mapper.toResponse(it) }
    }

    override fun findAllByCustMstId(searchParam: ReqRstIfMethodSearchParam): Flow<ReqRstIfMethodResponse> {
        return customRepository.findAllByCustMstId(searchParam)
            .map(mapper::toResponseFromQuery)
    }

    @Transactional
    override suspend fun save(command: ReqRstIfMethodCommand, creator: String): ReqRstIfMethodResponse {
        val now = LocalDateTime.now()
        val domain = mapper.toDomain(command, creator, now)
        domain.setAsNew()
        val saved = repository.save(domain)
        return mapper.toResponse(saved)
    }
}
