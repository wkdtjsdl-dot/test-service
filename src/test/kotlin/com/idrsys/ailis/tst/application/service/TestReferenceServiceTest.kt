package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.TestReferenceCommandMapper
import com.idrsys.ailis.tst.application.mapper.TestReferenceMapper
import com.idrsys.ailis.tst.application.required.TestReferenceRepository
import com.idrsys.ailis.tst.domain.command.TestReferenceCreateCommand
import com.idrsys.ailis.tst.domain.command.TestReferenceUpdateCommand
import com.idrsys.ailis.tst.domain.model.TestReference
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
class TestReferenceServiceTest {

    @Mock
    lateinit var repository: TestReferenceRepository

    @Mock
    lateinit var mapper: TestReferenceMapper

    @Mock
    lateinit var commandMapper: TestReferenceCommandMapper

    @InjectMocks
    lateinit var service: TestReferenceService

    // --- TestReference Tests ---

    @Test
    fun `registerReference should save and return response`() = runTest {
        // Given
        val request = TestReferenceRegisterRequest(
            refCateCd = "CATE01",
            useYn = true,
            refNm = "Reference Name",
            refAbbrNm = "Ref",
            refEngNm = "Reference Eng",
            refEngAbbrNm = "Ref Eng",
            sortOrder = 1,
            refType = "TYPE01",
            refSize = 10,
            rangeChkYn = true,
            refMinVal = 1,
            refMaxVal = 10,
            dataFormat = "FORMAT01",
            dftData = "Default",
            dftEngData = "Default Eng"
        )
        val domain = TestReference(
            refCd = "uuid",
            refCateCd = "CATE01",
            useYn = true,
            refNm = "Reference Name",
            refAbbrNm = "Ref",
            refEngNm = "Reference Eng",
            refEngAbbrNm = "Ref Eng",
            sortOrder = 1,
            refType = "TYPE01",
            refSize = 10,
            rangeChkYn = true,
            refMinVal = 1,
            refMaxVal = 10,
            dataFormat = "FORMAT01",
            dftData = "Default",
            dftEngData = "Default Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val response = TestReferenceResponse(
            refCd = "uuid",
            refCateCd = "CATE01",
            useYn = true,
            refNm = "Reference Name",
            refAbbrNm = "Ref",
            refEngNm = "Reference Eng",
            refEngAbbrNm = "Ref Eng",
            sortOrder = 1,
            refType = "TYPE01",
            refSize = 10,
            rangeChkYn = true,
            refMinVal = 1,
            refMaxVal = 10,
            dataFormat = "FORMAT01",
            dftData = "Default",
            dftEngData = "Default Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        val command = TestReferenceCreateCommand(
            refCateCd = "CATE01",
            useYn = true,
            refNm = "Reference Name",
            refAbbrNm = "Ref",
            refEngNm = "Reference Eng",
            refEngAbbrNm = "Ref Eng",
            sortOrder = 1,
            refType = "TYPE01",
            refSize = 10,
            rangeChkYn = true,
            refMinVal = 1,
            refMaxVal = 10,
            dataFormat = "FORMAT01",
            dftData = "Default",
            dftEngData = "Default Eng"
        )

        `when`(commandMapper.toCreateCommand(request)).thenReturn(command)
        `when`(repository.save(any())).thenReturn(domain)
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.registerReference(request, "admin")

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `updateReference should update and return response`() = runTest {
        // Given
        val refCd = "uuid"
        val request = TestReferenceUpdateRequest(
            refCateCd = "CATE02",
            useYn = false,
            refNm = "Updated Name",
            refAbbrNm = "Updated",
            refEngNm = "Updated Eng",
            refEngAbbrNm = "Updated Eng",
            sortOrder = 2,
            refType = "TYPE02",
            refSize = 20,
            rangeChkYn = false,
            refMinVal = 2,
            refMaxVal = 20,
            dataFormat = "FORMAT02",
            dftData = "Updated Default",
            dftEngData = "Updated Default Eng"
        )
        val existing = TestReference(
            refCd = refCd,
            refCateCd = "CATE01",
            useYn = true,
            refNm = "Reference Name",
            refAbbrNm = "Ref",
            refEngNm = "Reference Eng",
            refEngAbbrNm = "Ref Eng",
            sortOrder = 1,
            refType = "TYPE01",
            refSize = 10,
            rangeChkYn = true,
            refMinVal = 1,
            refMaxVal = 10,
            dataFormat = "FORMAT01",
            dftData = "Default",
            dftEngData = "Default Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val response = TestReferenceResponse(
            refCd = refCd,
            refCateCd = "CATE02",
            useYn = false,
            refNm = "Updated Name",
            refAbbrNm = "Updated",
            refEngNm = "Updated Eng",
            refEngAbbrNm = "Updated Eng",
            sortOrder = 2,
            refType = "TYPE02",
            refSize = 20,
            rangeChkYn = false,
            refMinVal = 2,
            refMaxVal = 20,
            dataFormat = "FORMAT02",
            dftData = "Updated Default",
            dftEngData = "Updated Default Eng",
            creator = "admin",
            createDtime = existing.createDtime,
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        val command = TestReferenceUpdateCommand(
            refCateCd = "CATE02",
            useYn = false,
            refNm = "Updated Name",
            refAbbrNm = "Updated",
            refEngNm = "Updated Eng",
            refEngAbbrNm = "Updated Eng",
            sortOrder = 2,
            refType = "TYPE02",
            refSize = 20,
            rangeChkYn = false,
            refMinVal = 2,
            refMaxVal = 20,
            dataFormat = "FORMAT02",
            dftData = "Updated Default",
            dftEngData = "Updated Default Eng"
        )

        `when`(repository.findById(refCd)).thenReturn(existing)
        `when`(commandMapper.toUpdateCommand(request)).thenReturn(command)
        `when`(repository.save(any())).thenReturn(existing)
        `when`(mapper.toResponse(existing)).thenReturn(response)

        // When
        val result = service.updateReference(refCd, request, "admin")

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `getReference should return response`() = runTest {
        // Given
        val refCd = "uuid"
        val domain = TestReference(
            refCd = refCd,
            refCateCd = "CATE01",
            useYn = true,
            refNm = "Reference Name",
            refAbbrNm = "Ref",
            refEngNm = "Reference Eng",
            refEngAbbrNm = "Ref Eng",
            sortOrder = 1,
            refType = "TYPE01",
            refSize = 10,
            rangeChkYn = true,
            refMinVal = 1,
            refMaxVal = 10,
            dataFormat = "FORMAT01",
            dftData = "Default",
            dftEngData = "Default Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val response = TestReferenceResponse(
            refCd = refCd,
            refCateCd = "CATE01",
            useYn = true,
            refNm = "Reference Name",
            refAbbrNm = "Ref",
            refEngNm = "Reference Eng",
            refEngAbbrNm = "Ref Eng",
            sortOrder = 1,
            refType = "TYPE01",
            refSize = 10,
            rangeChkYn = true,
            refMinVal = 1,
            refMaxVal = 10,
            dataFormat = "FORMAT01",
            dftData = "Default",
            dftEngData = "Default Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        `when`(repository.findById(refCd)).thenReturn(domain)
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.getReference(refCd)

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `deleteReference should call repository delete`() = runTest {
        // Given
        val refCd = "uuid"
        val domain = TestReference(
            refCd = refCd,
            refCateCd = "CATE01",
            useYn = true,
            refNm = "Reference Name",
            refAbbrNm = "Ref",
            refEngNm = "Reference Eng",
            refEngAbbrNm = "Ref Eng",
            sortOrder = 1,
            refType = "TYPE01",
            refSize = 10,
            rangeChkYn = true,
            refMinVal = 1,
            refMaxVal = 10,
            dataFormat = "FORMAT01",
            dftData = "Default",
            dftEngData = "Default Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        `when`(repository.findById(refCd)).thenReturn(domain)

        // When
        service.deleteReference(refCd, "admin")

        // Then
        Mockito.verify(repository).save(domain)
    }

    @Test
    fun `getAllReferences should return flow of responses`() = runTest {
        // Given
        val domain = TestReference(
            refCd = "uuid",
            refCateCd = "CATE01",
            useYn = true,
            refNm = "Reference Name",
            refAbbrNm = "Ref",
            refEngNm = "Reference Eng",
            refEngAbbrNm = "Ref Eng",
            sortOrder = 1,
            refType = "TYPE01",
            refSize = 10,
            rangeChkYn = true,
            refMinVal = 1,
            refMaxVal = 10,
            dataFormat = "FORMAT01",
            dftData = "Default",
            dftEngData = "Default Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val response = TestReferenceResponse(
            refCd = "uuid",
            refCateCd = "CATE01",
            useYn = true,
            refNm = "Reference Name",
            refAbbrNm = "Ref",
            refEngNm = "Reference Eng",
            refEngAbbrNm = "Ref Eng",
            sortOrder = 1,
            refType = "TYPE01",
            refSize = 10,
            rangeChkYn = true,
            refMinVal = 1,
            refMaxVal = 10,
            dataFormat = "FORMAT01",
            dftData = "Default",
            dftEngData = "Default Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        `when`(repository.findAll()).thenReturn(flowOf(domain))
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.getAllReferences().toList()

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
