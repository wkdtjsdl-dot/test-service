package com.idrsys.ailis.sales.application.required.repository.salsAction

import com.idrsys.ailis.sales.domain.model.SalsAction

interface SalsActionRepository {
    suspend fun save(salsAction: SalsAction): SalsAction
    suspend fun findById(id: Long): SalsAction?
}
