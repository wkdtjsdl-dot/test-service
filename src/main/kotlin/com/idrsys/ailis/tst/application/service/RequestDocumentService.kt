package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.RequestDocumentRegisterRequest
import com.idrsys.ailis.tst.application.dto.RequestDocumentResponse
import com.idrsys.ailis.tst.application.dto.RequestDocumentUpdateRequest
import com.idrsys.ailis.tst.application.mapper.RequestDocumentMapper
import com.idrsys.ailis.tst.application.required.RequestDocumentRepository
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
    private val requestDocumentMapper: RequestDocumentMapper
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
        val now = LocalDateTime.now()
        val document = RequestDocument(
            docCd = request.docCd,
            docDivCd = request.docDivCd,
            docNm = request.docNm,
            docEngNm = request.docEngNm,
            docFileId = request.docFileId,
            docEngFileId = request.docEngFileId,
            creator = adminId,
            createDtime = now,
            updater = adminId,
            updateDetime = now
        ).apply { setAsNew() }
        
        val saved = requestDocumentRepository.save(document)
        return requestDocumentMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun updateDocument(docCd: String, request: RequestDocumentUpdateRequest, adminId: String): RequestDocumentResponse {
        val document = requestDocumentRepository.findById(docCd)
            ?: throw NoSuchElementException("Document not found: $docCd")
        
        val now = LocalDateTime.now()
        document.update(
            docDivCd = request.docDivCd,
            docNm = request.docNm,
            docEngNm = request.docEngNm,
            docFileId = request.docFileId,
            docEngFileId = request.docEngFileId,
            updater = adminId,
            updateDetime = now
        )
        
        val saved = requestDocumentRepository.save(document)
        return requestDocumentMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteDocument(docCd: String, adminId: String) {
        requestDocumentRepository.deleteById(docCd)
    }
}
