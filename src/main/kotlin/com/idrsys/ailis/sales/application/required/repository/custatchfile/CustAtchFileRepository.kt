package com.idrsys.ailis.sales.application.required.repository.custatchfile

import com.idrsys.ailis.sales.domain.model.CustAtchFile

interface CustAtchFileRepository {
    suspend fun findById(id: String): CustAtchFile?
    suspend fun save(custAtchFile: CustAtchFile): CustAtchFile
    suspend fun deleteById(id: String)
}
