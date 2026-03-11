package com.idrsys.ailis.sales.adapter.repository.cust

import com.idrsys.ailis.sales.application.required.repository.cust.CustMstHstRepository
import com.idrsys.ailis.sales.domain.model.CustMstHst
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustMstHstDataRepository : CoroutineCrudRepository<CustMstHst, Long>

@Repository
class CustMstHistRepositoryImpl(
    private val custMstHstDataRepository: CustMstHstDataRepository
) : CustMstHstRepository {
    override suspend fun save(custMstHst: CustMstHst): CustMstHst {
        return custMstHstDataRepository.save(custMstHst)
    }
}
