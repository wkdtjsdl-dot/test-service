package com.idrsys.ailis.sales.adapter.repository.cust

import com.idrsys.ailis.sales.application.required.repository.cust.CustRepository
import com.idrsys.ailis.sales.domain.model.Cust
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustDataRepository : CoroutineCrudRepository<Cust, String> {
    suspend fun findByExtnAuthKey(extnAuthKey: String): Cust?
}

@Repository
class CustRepositoryImpl(
    private val custDataRepository: CustDataRepository
) : CustRepository {
    override suspend fun findByCustMstId(custMstId: String): Cust? {
        return custDataRepository.findById(custMstId)
    }

    override suspend fun save(cust: Cust): Cust{
        return custDataRepository.save(cust)
    }
}
