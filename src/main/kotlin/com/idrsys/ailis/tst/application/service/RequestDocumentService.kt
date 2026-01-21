package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.RequestDocumentRegisterRequest
import com.idrsys.ailis.tst.application.dto.RequestDocumentResponse
import com.idrsys.ailis.tst.application.dto.RequestDocumentUpdateRequest
import com.idrsys.ailis.tst.application.mapper.RequestDocumentCommandMapper
import com.idrsys.ailis.tst.application.mapper.RequestDocumentMapper
import com.idrsys.ailis.tst.application.required.repository.RequestDocumentRepository
import com.idrsys.ailis.tst.application.usecase.RequestDocumentUseCase
import com.idrsys.ailis.tst.domain.model.RequestDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class RequestDocumentService(
    private val requestDocumentRepository: RequestDocumentRepository,
    private val requestDocumentMapper: RequestDocumentMapper,
    private val commandMapper: RequestDocumentCommandMapper
) : RequestDocumentUseCase {

    @Transactional(readOnly = true)
    override suspend fun getDocuments(docDivCd: String?): Flow<RequestDocumentResponse> {
        return requestDocumentRepository.findAllByDocDivCd(docDivCd)
            .map { requestDocumentMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    override suspend fun getDocument(docCd: String): RequestDocumentResponse {
        val document = requestDocumentRepository.findById(docCd)
            ?: throw NoSuchElementException("Document not found: $docCd")
        return requestDocumentMapper.toResponse(document)
    }

    @Transactional
    override suspend fun registerDocument(request: RequestDocumentRegisterRequest, adminId: String): RequestDocumentResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = LocalDateTime.now()
        val document = RequestDocument.create(command, adminId, now)

        val saved = requestDocumentRepository.save(document)
        return requestDocumentMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun updateDocument(docCd: String, request: RequestDocumentUpdateRequest, adminId: String): RequestDocumentResponse {
        val document = requestDocumentRepository.findById(docCd)
            ?: throw NoSuchElementException("Document not found: $docCd")

        val command = commandMapper.toUpdateCommand(request)
        val now = LocalDateTime.now()
        document.update(command, adminId, now)

        val saved = requestDocumentRepository.save(document)
        return requestDocumentMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteDocument(docCd: String, adminId: String) {
        requestDocumentRepository.deleteById(docCd)
    }
}
