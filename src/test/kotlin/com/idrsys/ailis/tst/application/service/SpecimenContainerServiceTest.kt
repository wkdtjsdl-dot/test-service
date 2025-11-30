package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.SpecimenContainerCommandMapper
import com.idrsys.ailis.tst.application.mapper.SpecimenContainerMapper
import com.idrsys.ailis.tst.application.required.SpecimenContainerRepository
import com.idrsys.ailis.tst.domain.command.SpecimenContainerCreateCommand
import com.idrsys.ailis.tst.domain.command.SpecimenContainerUpdateCommand
import com.idrsys.ailis.tst.domain.model.SpecimenContainer
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
class SpecimenContainerServiceTest {

    @Mock
    lateinit var repository: SpecimenContainerRepository

    @Mock
    lateinit var mapper: SpecimenContainerMapper

    @Mock
    lateinit var commandMapper: SpecimenContainerCommandMapper

    @InjectMocks
    lateinit var service: SpecimenContainerService

    // --- SpecimenContainer Tests ---

    @Test
    fun `registerContainer should save and return response`() = runTest {
        // Given
        val request = SpecimenContainerRegisterRequest(
            spcmCntnCd = "CNTN01",
            cntnNm = "Container Name",
            cntnEngNm = "Container Eng",
            cntnFileId = "FILE01"
        )
        val domain = SpecimenContainer(
            spcmCntnCd = "CNTN01",
            cntnNm = "Container Name",
            cntnEngNm = "Container Eng",
            cntnFileId = "FILE01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val response = SpecimenContainerResponse(
            spcmCntnCd = "CNTN01",
            cntnNm = "Container Name",
            cntnEngNm = "Container Eng",
            cntnFileId = "FILE01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        val command = SpecimenContainerCreateCommand(
            spcmCntnCd = "CNTN01",
            cntnNm = "Container Name",
            cntnEngNm = "Container Eng",
            cntnFileId = "FILE01"
        )

        `when`(commandMapper.toCreateCommand(request)).thenReturn(command)
        `when`(repository.save(any())).thenReturn(domain)
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.registerContainer(request, "admin")

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `updateContainer should update and return response`() = runTest {
        // Given
        val id = "CNTN01"
        val request = SpecimenContainerUpdateRequest(
            cntnNm = "Updated Name",
            cntnEngNm = "Updated Eng",
            cntnFileId = "FILE02"
        )
        val existing = SpecimenContainer(
            spcmCntnCd = id,
            cntnNm = "Container Name",
            cntnEngNm = "Container Eng",
            cntnFileId = "FILE01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val response = SpecimenContainerResponse(
            spcmCntnCd = id,
            cntnNm = "Updated Name",
            cntnEngNm = "Updated Eng",
            cntnFileId = "FILE02",
            creator = "admin",
            createDtime = existing.createDtime,
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        val command = SpecimenContainerUpdateCommand(
            cntnNm = "Updated Name",
            cntnEngNm = "Updated Eng",
            cntnFileId = "FILE02"
        )

        `when`(repository.findById(id)).thenReturn(existing)
        `when`(commandMapper.toUpdateCommand(request)).thenReturn(command)
        `when`(repository.save(any())).thenReturn(existing)
        `when`(mapper.toResponse(existing)).thenReturn(response)

        // When
        val result = service.updateContainer(id, request, "admin")

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `getContainer should return response`() = runTest {
        // Given
        val id = "CNTN01"
        val domain = SpecimenContainer(
            spcmCntnCd = id,
            cntnNm = "Container Name",
            cntnEngNm = "Container Eng",
            cntnFileId = "FILE01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val response = SpecimenContainerResponse(
            spcmCntnCd = id,
            cntnNm = "Container Name",
            cntnEngNm = "Container Eng",
            cntnFileId = "FILE01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        `when`(repository.findById(id)).thenReturn(domain)
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.getContainer(id)

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `deleteContainer should call repository delete`() = runTest {
        // Given
        val id = "CNTN01"

        // When
        service.deleteContainer(id, "admin")

        // Then
        Mockito.verify(repository).deleteById(id)
    }

    @Test
    fun `getContainers should return flow of responses`() = runTest {
        // Given
        val domain = SpecimenContainer(
            spcmCntnCd = "CNTN01",
            cntnNm = "Container Name",
            cntnEngNm = "Container Eng",
            cntnFileId = "FILE01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val response = SpecimenContainerResponse(
            spcmCntnCd = "CNTN01",
            cntnNm = "Container Name",
            cntnEngNm = "Container Eng",
            cntnFileId = "FILE01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        `when`(repository.findAllByCntnNm(null)).thenReturn(flowOf(domain))
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.getContainers(null).toList()

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
