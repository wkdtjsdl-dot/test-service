package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.RequestDocumentCommandMapper
import com.idrsys.ailis.tst.application.mapper.RequestDocumentMapper
import com.idrsys.ailis.tst.application.required.RequestDocumentRepository
import com.idrsys.ailis.tst.domain.command.RequestDocumentCreateCommand
import com.idrsys.ailis.tst.domain.command.RequestDocumentUpdateCommand
import com.idrsys.ailis.tst.domain.model.RequestDocument
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class RequestDocumentServiceTest {

    @Mock
    lateinit var repository: RequestDocumentRepository

    @Mock
    lateinit var mapper: RequestDocumentMapper

    @Mock
    lateinit var commandMapper: RequestDocumentCommandMapper

    @InjectMocks
    lateinit var service: RequestDocumentService

    // --- RequestDocument Tests ---

    @Test
    fun `registerDocument should save and return response`() = runTest {
        // Given
        val request = RequestDocumentRegisterRequest(
            docCd = "DOC01",
            docDivCd = "DIV01",
            docNm = "Document Name",
            docEngNm = "Document Eng",
            docFileId = "FILE01",
            docEngFileId = "FILE_ENG01"
        )
        val domain = RequestDocument(
            docCd = "DOC01",
            docDivCd = "DIV01",
            docNm = "Document Name",
            docEngNm = "Document Eng",
            docFileId = "FILE01",
            docEngFileId = "FILE_ENG01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = RequestDocumentResponse(
            docCd = "DOC01",
            docDivCd = "DIV01",
            docNm = "Document Name",
            docEngNm = "Document Eng",
            docFileId = "FILE01",
            docEngFileId = "FILE_ENG01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        val command = RequestDocumentCreateCommand(
            docCd = "DOC01",
            docDivCd = "DIV01",
            docNm = "Document Name",
            docEngNm = "Document Eng",
            docFileId = "FILE01",
            docEngFileId = "FILE_ENG01"
        )

        `when`(commandMapper.toCreateCommand(request)).thenReturn(command)
        `when`(repository.save(any())).thenReturn(domain)
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.registerDocument(request, "admin")

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `updateDocument should update and return response`() = runTest {
        // Given
        val id = "DOC01"
        val request = RequestDocumentUpdateRequest(
            docDivCd = "DIV02",
            docNm = "Updated Name",
            docEngNm = "Updated Eng",
            docFileId = "FILE02",
            docEngFileId = "FILE_ENG02"
        )
        val existing = RequestDocument(
            docCd = id,
            docDivCd = "DIV01",
            docNm = "Document Name",
            docEngNm = "Document Eng",
            docFileId = "FILE01",
            docEngFileId = "FILE_ENG01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = RequestDocumentResponse(
            docCd = id,
            docDivCd = "DIV02",
            docNm = "Updated Name",
            docEngNm = "Updated Eng",
            docFileId = "FILE02",
            docEngFileId = "FILE_ENG02",
            creator = "admin",
            createDtime = existing.createDtime,
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        val command = RequestDocumentUpdateCommand(
            docDivCd = "DIV02",
            docNm = "Updated Name",
            docEngNm = "Updated Eng",
            docFileId = "FILE02",
            docEngFileId = "FILE_ENG02"
        )

        `when`(repository.findById(id)).thenReturn(existing)
        `when`(commandMapper.toUpdateCommand(request)).thenReturn(command)
        `when`(repository.save(any())).thenReturn(existing)
        `when`(mapper.toResponse(existing)).thenReturn(response)

        // When
        val result = service.updateDocument(id, request, "admin")

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `getDocument should return response`() = runTest {
        // Given
        val id = "DOC01"
        val domain = RequestDocument(
            docCd = id,
            docDivCd = "DIV01",
            docNm = "Document Name",
            docEngNm = "Document Eng",
            docFileId = "FILE01",
            docEngFileId = "FILE_ENG01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = RequestDocumentResponse(
            docCd = id,
            docDivCd = "DIV01",
            docNm = "Document Name",
            docEngNm = "Document Eng",
            docFileId = "FILE01",
            docEngFileId = "FILE_ENG01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        `when`(repository.findById(id)).thenReturn(domain)
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.getDocument(id)

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `deleteDocument should call repository delete`() = runTest {
        // Given
        val id = "DOC01"

        // When
        service.deleteDocument(id, "admin")

        // Then
        Mockito.verify(repository).deleteById(id)
    }

    @Test
    fun `getDocuments should return flow of responses`() = runTest {
        // Given
        val domain = RequestDocument(
            docCd = "DOC01",
            docDivCd = "DIV01",
            docNm = "Document Name",
            docEngNm = "Document Eng",
            docFileId = "FILE01",
            docEngFileId = "FILE_ENG01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = RequestDocumentResponse(
            docCd = "DOC01",
            docDivCd = "DIV01",
            docNm = "Document Name",
            docEngNm = "Document Eng",
            docFileId = "FILE01",
            docEngFileId = "FILE_ENG01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        `when`(repository.findAllByDocDivCd(null)).thenReturn(flowOf(domain))
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.getDocuments(null).toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(response, result[0])
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T
}
