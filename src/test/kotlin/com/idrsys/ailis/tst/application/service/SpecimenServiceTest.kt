package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.SpecimenMapper
import com.idrsys.ailis.tst.application.required.SpecimenRepository
import com.idrsys.ailis.tst.domain.model.Specimen
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
class SpecimenServiceTest {

    @Mock
    lateinit var repository: SpecimenRepository

    @Mock
    lateinit var mapper: SpecimenMapper

    @InjectMocks
    lateinit var service: SpecimenService

    // --- Specimen Tests ---

    @Test
    fun `registerSpecimen should save and return response`() = runTest {
        // Given
        val request = SpecimenRegisterRequest(
            spcmCd = "SPCM01",
            spcmCateCd = "CATE01",
            useYn = true,
            spcmNm = "Specimen Name",
            spcmAbbrNm = "Specimen",
            spcmEngNm = "Specimen Eng",
            spcmEngAbbrNm = "Specimen Eng",
            collAmt = "10ml",
            engCollAmt = "10ml Eng",
            spcmStrg = "Storage",
            engSpcmStrg = "Storage Eng",
            spcmSafe = "Safe",
            engSpcmSafe = "Safe Eng",
            caution = "Caution",
            engCaution = "Caution Eng",
            ref = "Ref",
            engRef = "Ref Eng"
        )
        val domain = Specimen(
            spcmCd = "SPCM01",
            spcmCateCd = "CATE01",
            useYn = true,
            spcmNm = "Specimen Name",
            spcmAbbrNm = "Specimen",
            spcmEngNm = "Specimen Eng",
            spcmEngAbbrNm = "Specimen Eng",
            collAmt = "10ml",
            engCollAmt = "10ml Eng",
            spcmStrg = "Storage",
            engSpcmStrg = "Storage Eng",
            spcmSafe = "Safe",
            engSpcmSafe = "Safe Eng",
            caution = "Caution",
            engCaution = "Caution Eng",
            ref = "Ref",
            engRef = "Ref Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val response = SpecimenResponse(
            spcmCd = "SPCM01",
            spcmCateCd = "CATE01",
            useYn = true,
            spcmNm = "Specimen Name",
            spcmAbbrNm = "Specimen",
            spcmEngNm = "Specimen Eng",
            spcmEngAbbrNm = "Specimen Eng",
            collAmt = "10ml",
            engCollAmt = "10ml Eng",
            spcmStrg = "Storage",
            engSpcmStrg = "Storage Eng",
            spcmSafe = "Safe",
            engSpcmSafe = "Safe Eng",
            caution = "Caution",
            engCaution = "Caution Eng",
            ref = "Ref",
            engRef = "Ref Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        `when`(repository.save(any())).thenReturn(domain)
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.registerSpecimen(request, "admin")

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `updateSpecimen should update and return response`() = runTest {
        // Given
        val id = "SPCM01"
        val request = SpecimenUpdateRequest(
            spcmCateCd = "CATE02",
            useYn = false,
            spcmNm = "Updated Name",
            spcmAbbrNm = "Updated",
            spcmEngNm = "Updated Eng",
            spcmEngAbbrNm = "Updated Eng",
            collAmt = "20ml",
            engCollAmt = "20ml Eng",
            spcmStrg = "Updated Storage",
            engSpcmStrg = "Updated Storage Eng",
            spcmSafe = "Updated Safe",
            engSpcmSafe = "Updated Safe Eng",
            caution = "Updated Caution",
            engCaution = "Updated Caution Eng",
            ref = "Updated Ref",
            engRef = "Updated Ref Eng"
        )
        val existing = Specimen(
            spcmCd = id,
            spcmCateCd = "CATE01",
            useYn = true,
            spcmNm = "Specimen Name",
            spcmAbbrNm = "Specimen",
            spcmEngNm = "Specimen Eng",
            spcmEngAbbrNm = "Specimen Eng",
            collAmt = "10ml",
            engCollAmt = "10ml Eng",
            spcmStrg = "Storage",
            engSpcmStrg = "Storage Eng",
            spcmSafe = "Safe",
            engSpcmSafe = "Safe Eng",
            caution = "Caution",
            engCaution = "Caution Eng",
            ref = "Ref",
            engRef = "Ref Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val updated = Specimen(
            spcmCd = id,
            spcmCateCd = "CATE02",
            useYn = false,
            spcmNm = "Updated Name",
            spcmAbbrNm = "Updated",
            spcmEngNm = "Updated Eng",
            spcmEngAbbrNm = "Updated Eng",
            collAmt = "20ml",
            engCollAmt = "20ml Eng",
            spcmStrg = "Updated Storage",
            engSpcmStrg = "Updated Storage Eng",
            spcmSafe = "Updated Safe",
            engSpcmSafe = "Updated Safe Eng",
            caution = "Updated Caution",
            engCaution = "Updated Caution Eng",
            ref = "Updated Ref",
            engRef = "Updated Ref Eng",
            creator = "admin",
            createDtime = existing.createDtime,
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val response = SpecimenResponse(
            spcmCd = id,
            spcmCateCd = "CATE02",
            useYn = false,
            spcmNm = "Updated Name",
            spcmAbbrNm = "Updated",
            spcmEngNm = "Updated Eng",
            spcmEngAbbrNm = "Updated Eng",
            collAmt = "20ml",
            engCollAmt = "20ml Eng",
            spcmStrg = "Updated Storage",
            engSpcmStrg = "Updated Storage Eng",
            spcmSafe = "Updated Safe",
            engSpcmSafe = "Updated Safe Eng",
            caution = "Updated Caution",
            engCaution = "Updated Caution Eng",
            ref = "Updated Ref",
            engRef = "Updated Ref Eng",
            creator = "admin",
            createDtime = existing.createDtime,
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        `when`(repository.findById(id)).thenReturn(existing)
        `when`(repository.save(any())).thenReturn(updated)
        `when`(mapper.toResponse(updated)).thenReturn(response)

        // When
        val result = service.updateSpecimen(id, request, "admin")

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `getSpecimen should return response`() = runTest {
        // Given
        val id = "SPCM01"
        val domain = Specimen(
            spcmCd = id,
            spcmCateCd = "CATE01",
            useYn = true,
            spcmNm = "Specimen Name",
            spcmAbbrNm = "Specimen",
            spcmEngNm = "Specimen Eng",
            spcmEngAbbrNm = "Specimen Eng",
            collAmt = "10ml",
            engCollAmt = "10ml Eng",
            spcmStrg = "Storage",
            engSpcmStrg = "Storage Eng",
            spcmSafe = "Safe",
            engSpcmSafe = "Safe Eng",
            caution = "Caution",
            engCaution = "Caution Eng",
            ref = "Ref",
            engRef = "Ref Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val response = SpecimenResponse(
            spcmCd = id,
            spcmCateCd = "CATE01",
            useYn = true,
            spcmNm = "Specimen Name",
            spcmAbbrNm = "Specimen",
            spcmEngNm = "Specimen Eng",
            spcmEngAbbrNm = "Specimen Eng",
            collAmt = "10ml",
            engCollAmt = "10ml Eng",
            spcmStrg = "Storage",
            engSpcmStrg = "Storage Eng",
            spcmSafe = "Safe",
            engSpcmSafe = "Safe Eng",
            caution = "Caution",
            engCaution = "Caution Eng",
            ref = "Ref",
            engRef = "Ref Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        `when`(repository.findById(id)).thenReturn(domain)
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.getSpecimen(id)

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `deleteSpecimen should call repository delete`() = runTest {
        // Given
        val id = "SPCM01"
        val domain = Specimen(
            spcmCd = id,
            spcmCateCd = "CATE01",
            useYn = true,
            spcmNm = "Specimen Name",
            spcmAbbrNm = "Specimen",
            spcmEngNm = "Specimen Eng",
            spcmEngAbbrNm = "Specimen Eng",
            collAmt = "10ml",
            engCollAmt = "10ml Eng",
            spcmStrg = "Storage",
            engSpcmStrg = "Storage Eng",
            spcmSafe = "Safe",
            engSpcmSafe = "Safe Eng",
            caution = "Caution",
            engCaution = "Caution Eng",
            ref = "Ref",
            engRef = "Ref Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        `when`(repository.findById(id)).thenReturn(domain)

        // When
        service.deleteSpecimen(id, "admin")

        // Then
        Mockito.verify(repository).save(domain)
    }

    @Test
    fun `getSpecimens should return flow of responses`() = runTest {
        // Given
        val domain = Specimen(
            spcmCd = "SPCM01",
            spcmCateCd = "CATE01",
            useYn = true,
            spcmNm = "Specimen Name",
            spcmAbbrNm = "Specimen",
            spcmEngNm = "Specimen Eng",
            spcmEngAbbrNm = "Specimen Eng",
            collAmt = "10ml",
            engCollAmt = "10ml Eng",
            spcmStrg = "Storage",
            engSpcmStrg = "Storage Eng",
            spcmSafe = "Safe",
            engSpcmSafe = "Safe Eng",
            caution = "Caution",
            engCaution = "Caution Eng",
            ref = "Ref",
            engRef = "Ref Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )
        val response = SpecimenResponse(
            spcmCd = "SPCM01",
            spcmCateCd = "CATE01",
            useYn = true,
            spcmNm = "Specimen Name",
            spcmAbbrNm = "Specimen",
            spcmEngNm = "Specimen Eng",
            spcmEngAbbrNm = "Specimen Eng",
            collAmt = "10ml",
            engCollAmt = "10ml Eng",
            spcmStrg = "Storage",
            engSpcmStrg = "Storage Eng",
            spcmSafe = "Safe",
            engSpcmSafe = "Safe Eng",
            caution = "Caution",
            engCaution = "Caution Eng",
            ref = "Ref",
            engRef = "Ref Eng",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDetime = LocalDateTime.now()
        )

        `when`(repository.findAll(null, null)).thenReturn(flowOf(domain))
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.getSpecimens(null, null).toList()

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
