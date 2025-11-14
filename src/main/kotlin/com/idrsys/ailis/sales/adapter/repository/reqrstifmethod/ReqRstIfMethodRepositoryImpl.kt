package com.idrsys.ailis.sales.adapter.repository.reqrstifmethod

import com.idrsys.ailis.sales.application.required.repository.reqrstifmethod.ReqRstIfMethodRepository
import com.idrsys.ailis.sales.domain.model.ReqRstIfMethod
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ReqRstIfMethodDataRepository : CoroutineCrudRepository<ReqRstIfMethod, String>

@Repository
class ReqRstIfMethodRepositoryImpl(
    private val dataRepository: ReqRstIfMethodDataRepository
) : ReqRstIfMethodRepository {

    override suspend fun findById(id: String): ReqRstIfMethod? {
        return dataRepository.findById(id)
    }

    override suspend fun save(reqRstIfMethod: ReqRstIfMethod): ReqRstIfMethod {
        return dataRepository.save(reqRstIfMethod)
    }
}
