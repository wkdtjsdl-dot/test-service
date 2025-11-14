package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserCommand
import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserSearchParam
import com.idrsys.ailis.sales.application.dto.response.HpLoginUserResponse
import com.idrsys.ailis.sales.application.required.repository.hploginuser.HpLoginUserCustomRepository
import com.idrsys.ailis.sales.application.required.repository.hploginuser.HpLoginUserRepository
import com.idrsys.ailis.sales.application.usecase.hploginuser.HpLoginUserUseCase
import com.idrsys.ailis.sales.shared.mapper.HpLoginUserMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class HpLoginUserService(
    private val repository: HpLoginUserRepository,
    private val customRepository: HpLoginUserCustomRepository,
    private val mapper: HpLoginUserMapper
) : HpLoginUserUseCase {

    override suspend fun findById(hpLoginUserId: String): HpLoginUserResponse? {
        return repository.findById(hpLoginUserId)?.let { mapper.toResponse(it) }
    }

    override fun findAllByCustMstId(searchParam: HpLoginUserSearchParam): Flow<HpLoginUserResponse> {
        return customRepository.findAllByCustMstId(searchParam)
            .map(mapper::toResponseFromQuery)
    }

    @Transactional
    override suspend fun save(command: HpLoginUserCommand, creator: String): HpLoginUserResponse {
        val now = LocalDateTime.now()
        val domain = mapper.toDomain(command, creator, now)
        domain.setAsNew()
        val saved = repository.save(domain)
        return mapper.toResponse(saved)
    }
}
