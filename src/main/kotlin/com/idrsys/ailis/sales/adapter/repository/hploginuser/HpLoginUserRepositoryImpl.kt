package com.idrsys.ailis.sales.adapter.repository.hploginuser

import com.idrsys.ailis.sales.application.required.repository.hploginuser.HpLoginUserRepository
import com.idrsys.ailis.sales.domain.model.HpLoginUser
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HpLoginUserDataRepository : CoroutineCrudRepository<HpLoginUser, String>

@Repository
class HpLoginUserRepositoryImpl(
    private val dataRepository: HpLoginUserDataRepository
) : HpLoginUserRepository {

    override suspend fun findById(id: String): HpLoginUser? {
        return dataRepository.findById(id)
    }

    override suspend fun save(hpLoginUser: HpLoginUser): HpLoginUser {
        return dataRepository.save(hpLoginUser)
    }
}
