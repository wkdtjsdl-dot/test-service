package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.RequestDocumentRegisterRequest
import com.idrsys.ailis.tst.application.dto.RequestDocumentResponse
import com.idrsys.ailis.tst.application.dto.RequestDocumentUpdateRequest
import kotlinx.coroutines.flow.Flow

interface RequestDocumentUseCase {
    suspend fun getDocuments(docDivCd: String?): Flow<RequestDocumentResponse>
    suspend fun getDocument(docCd: String): RequestDocumentResponse
    suspend fun registerDocument(request: RequestDocumentRegisterRequest, adminId: String): RequestDocumentResponse
    suspend fun updateDocument(docCd: String, request: RequestDocumentUpdateRequest, adminId: String): RequestDocumentResponse
    suspend fun deleteDocument(docCd: String, adminId: String)
}
