package com.idrsys.ailis.sales.adapter.repository.custatchfile

import com.idrsys.ailis.sales.application.required.repository.custatchfile.CustAtchFileRepository
import com.idrsys.ailis.sales.domain.model.CustAtchFile
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustAtchFileDataRepository : CoroutineCrudRepository<CustAtchFile, String>

@Repository
class CustAtchFileRepositoryImpl(
    private val dataRepository: CustAtchFileDataRepository
) : CustAtchFileRepository {

    override suspend fun findById(id: String): CustAtchFile? {
        return dataRepository.findById(id)
    }

    override suspend fun save(custAtchFile: CustAtchFile): CustAtchFile {
        return dataRepository.save(custAtchFile)
    }

    override suspend fun deleteById(id: String) {
        dataRepository.deleteById(id)
    }
}
