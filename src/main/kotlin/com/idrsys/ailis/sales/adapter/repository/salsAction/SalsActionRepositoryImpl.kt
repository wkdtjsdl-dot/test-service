package com.idrsys.ailis.sales.adapter.repository.salsAction

import com.idrsys.ailis.sales.application.required.repository.salsAction.SalsActionRepository
import com.idrsys.ailis.sales.domain.model.SalsAction
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SalsActionDataRepository : CoroutineCrudRepository<SalsAction, Long>

@Repository
class SalsActionRepositoryImpl(
    private val salsActionDataRepository: SalsActionDataRepository
) : SalsActionRepository {
    override suspend fun save(salsAction: SalsAction): SalsAction {
        return salsActionDataRepository.save(salsAction)
    }

    override suspend fun findById(id: Long): SalsAction? {
        return salsActionDataRepository.findById(id)
    }
}
