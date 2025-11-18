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
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class HpLoginUserService(
    private val repository: HpLoginUserRepository,
    private val customRepository: HpLoginUserCustomRepository,
    private val mapper: HpLoginUserMapper
) : HpLoginUserUseCase {

    override suspend fun getHpLoginUserPage(searchParam: HpLoginUserSearchParam, pageable: Pageable): Page<HpLoginUserResponse> {
        val total = customRepository.countHpLoginUsers(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val hpLoginUsers = customRepository.findHpLoginUsers(searchParam, pageable)
            .map(mapper::toResponseFromQuery)
            .toList()

        return PageImpl(hpLoginUsers, pageable, total)
    }

    override fun findAllByCustMstId(searchParam: HpLoginUserSearchParam): Flow<HpLoginUserResponse> {
        return customRepository.findAllByCustMstId(searchParam)
            .map(mapper::toResponseFromQuery)
    }

    @Transactional
    override suspend fun createHpLoginUser(custMstId: String, command: HpLoginUserCommand, creator: String): HpLoginUserResponse {
        val now = LocalDateTime.now()
        val hpLoginUser = mapper.toDomain(command, creator, now).apply { setAsNew() }
        val savedHpLoginUser = repository.save(hpLoginUser)
        return mapper.toResponse(savedHpLoginUser)
    }

    @Transactional
    override suspend fun updateHpLoginUser(hpLoginUserId: String, command: HpLoginUserCommand, updater: String): HpLoginUserResponse {
        val hpLoginUser = customRepository.findDomainById(hpLoginUserId)
            ?: throw NoSuchElementException("HpLoginUser not found with id: $hpLoginUserId")

        hpLoginUser.update(command, updater)

        val updatedHpLoginUser = repository.save(hpLoginUser)
        return mapper.toResponse(updatedHpLoginUser)
    }

    @Transactional
    override suspend fun deleteHpLoginUser(hpLoginUserId: String) {
        repository.deleteById(hpLoginUserId)
    }
}
