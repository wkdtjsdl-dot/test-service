package com.idrsys.ailis.sales.adapter.repository.custLogs

import com.idrsys.ailis.sales.application.required.repository.custLogs.CustLogsRepository
import com.idrsys.ailis.sales.domain.model.CustMstHst
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustLogsImpl : CoroutineCrudRepository<CustMstHst, Long>

@Repository
class CustLogsRepositoryImpl(
    private val custLogsRepository: CustLogsImpl,
) : CustLogsRepository {

    override suspend fun save(custLogs: CustMstHst): CustMstHst {
        return custLogsRepository.save(custLogs)
    }

    override suspend fun findById(id: Long): CustMstHst? {
        return custLogsRepository.findById(id)
    }
}
