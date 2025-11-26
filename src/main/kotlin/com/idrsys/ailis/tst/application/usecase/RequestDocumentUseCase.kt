package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import kotlinx.coroutines.flow.Flow

interface RequestDocumentUseCase {
    suspend fun getDocuments(docDivCd: String?): Flow<RequestDocumentResponse>
    suspend fun getDocument(docCd: String): RequestDocumentResponse
    suspend fun registerDocument(request: RequestDocumentRegisterRequest): RequestDocumentResponse
    suspend fun updateDocument(docCd: String, request: RequestDocumentUpdateRequest): RequestDocumentResponse
    suspend fun deleteDocument(docCd: String)
}
