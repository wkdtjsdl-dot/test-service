package com.idrsys.ailis.tst.application.required.repository

import com.idrsys.ailis.tst.domain.model.RequestDocument
import kotlinx.coroutines.flow.Flow

interface RequestDocumentRepository {
    // Basic CRUD
    suspend fun save(requestDocument: RequestDocument): RequestDocument
    suspend fun findById(id: String): RequestDocument?
    suspend fun deleteById(id: String)

    // Custom Queries
    suspend fun findAllByDocDivCd(docDivCd: String?): Flow<RequestDocument>
}
