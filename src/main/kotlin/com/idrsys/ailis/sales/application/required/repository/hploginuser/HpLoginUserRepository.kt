package com.idrsys.ailis.sales.application.required.repository.hploginuser

import com.idrsys.ailis.sales.domain.model.HpLoginUser

interface HpLoginUserRepository {
    suspend fun findById(id: String): HpLoginUser?
    suspend fun save(hpLoginUser: HpLoginUser): HpLoginUser
}
