package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.TestItemCommandMapper
import com.idrsys.ailis.tst.application.mapper.TestItemMapper
import com.idrsys.ailis.tst.application.required.TestItemRepository
import com.idrsys.ailis.tst.domain.command.TestItemCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemUpdateCommand
import com.idrsys.ailis.tst.domain.model.TestItem
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
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class TestItemServiceTest {

    @Mock
    lateinit var repository: TestItemRepository

    @Mock
    lateinit var mapper: TestItemMapper

    @Mock
    lateinit var commandMapper: TestItemCommandMapper

    @InjectMocks
    lateinit var service: TestItemService

    // --- TestItem Tests ---

    @Test
    fun `registerItem should save and return response`() = runTest {
        // Given
        val request = TestItemRegisterRequest(
            tstCd = "uuid",
            tstLargeCateCd = "L01",
            tstMediumCateCd = "M01",
            startDt = LocalDate.of(2023, 1, 1),
            endDt = LocalDate.of(9999, 12, 31),
            useYn = true,
            reqPossYn = true,
            webKorYn = true,
            webEngYn = true,
            tstNm = "Test Item",
            tstAbbrNm = "Test",
            tstEngNm = "Test Item Eng",
            tstEngAbbrNm = "Test Eng",
            tstIntNm = "Internal Name",
            rstTypeShortYn = true,
            rstTypeLongYn = false,
            rstTypeFileYn = false,
            rstTypeUrlYn = false,
            diseaseCd = "D01",
            tstMethodCd = "METHOD01",
            refVal = "Ref Val",
            engRefVal = "Eng Ref Val",
            clncSgnf = "Significance",
            engClncSgnf = "Eng Significance",
            tstDesc = "Description",
            tstEngDesc = "Eng Description",
            tstDayweek = "Mon",
            tstTatday = 1,
            insuApplyCd = "APPLY01",
            insuCd = "INSU01",
            insuCateNo = "CATE01"
        )
        val domain = TestItem(
            tstCd = "uuid",
            tstLargeCateCd = "L01",
            tstMediumCateCd = "M01",
            startDt = LocalDate.of(2023, 1, 1),
            endDt = LocalDate.of(9999, 12, 31),
            useYn = true,
            reqPossYn = true,
            webKorYn = true,
            webEngYn = true,
            tstNm = "Test Item",
            tstAbbrNm = "Test",
            tstEngNm = "Test Item Eng",
            tstEngAbbrNm = "Test Eng",
            tstIntNm = "Internal Name",
            rstTypeShortYn = true,
            rstTypeLongYn = false,
            rstTypeFileYn = false,
            rstTypeUrlYn = false,
            diseaseCd = "D01",
            tstMethodCd = "METHOD01",
            refVal = "Ref Val",
            engRefVal = "Eng Ref Val",
            clncSgnf = "Significance",
            engClncSgnf = "Eng Significance",
            tstDesc = "Description",
            tstEngDesc = "Eng Description",
            tstDayweek = "Mon",
            tstTatday = 1,
            insuApplyCd = "APPLY01",
            insuCd = "INSU01",
            insuCateNo = "CATE01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = TestItemResponse(
            tstCd = "uuid",
            tstLargeCateCd = "L01",
            tstMediumCateCd = "M01",
            startDt = LocalDate.of(2023, 1, 1),
            endDt = LocalDate.of(9999, 12, 31),
            useYn = true,
            reqPossYn = true,
            webKorYn = true,
            webEngYn = true,
            tstNm = "Test Item",
            tstAbbrNm = "Test",
            tstEngNm = "Test Item Eng",
            tstEngAbbrNm = "Test Eng",
            tstIntNm = "Internal Name",
            rstTypeShortYn = true,
            rstTypeLongYn = false,
            rstTypeFileYn = false,
            rstTypeUrlYn = false,
            diseaseCd = "D01",
            tstMethodCd = "METHOD01",
            refVal = "Ref Val",
            engRefVal = "Eng Ref Val",
            clncSgnf = "Significance",
            engClncSgnf = "Eng Significance",
            tstDesc = "Description",
            tstEngDesc = "Eng Description",
            tstDayweek = "Mon",
            tstTatday = 1,
            insuApplyCd = "APPLY01",
            insuCd = "INSU01",
            insuCateNo = "CATE01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        val command = TestItemCreateCommand(
            tstCd = "uuid",
            tstLargeCateCd = "L01",
            tstMediumCateCd = "M01",
            startDt = LocalDate.of(2023, 1, 1),
            endDt = LocalDate.of(9999, 12, 31),
            useYn = true,
            reqPossYn = true,
            webKorYn = true,
            webEngYn = true,
            tstNm = "Test Item",
            tstAbbrNm = "Test",
            tstEngNm = "Test Item Eng",
            tstEngAbbrNm = "Test Eng",
            tstIntNm = "Internal Name",
            rstTypeShortYn = true,
            rstTypeLongYn = false,
            rstTypeFileYn = false,
            rstTypeUrlYn = false,
            diseaseCd = "D01",
            tstMethodCd = "METHOD01",
            refVal = "Ref Val",
            engRefVal = "Eng Ref Val",
            clncSgnf = "Significance",
            engClncSgnf = "Eng Significance",
            tstDesc = "Description",
            tstEngDesc = "Eng Description",
            tstDayweek = "Mon",
            tstTatday = 1,
            insuApplyCd = "APPLY01",
            insuCd = "INSU01",
            insuCateNo = "CATE01"
        )

        `when`(commandMapper.toCreateCommand(request)).thenReturn(command)
        `when`(repository.save(any())).thenReturn(domain)
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.registerItem(request, "admin")

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `updateItem should update and return response`() = runTest {
        // Given
        val id = "uuid"
        val request = TestItemUpdateRequest(
            tstLargeCateCd = "L02",
            tstMediumCateCd = "M02",
            startDt = LocalDate.of(2023, 1, 2),
            endDt = LocalDate.of(9999, 12, 31),
            useYn = false,
            reqPossYn = false,
            webKorYn = false,
            webEngYn = false,
            tstNm = "Updated Item",
            tstAbbrNm = "Updated",
            tstEngNm = "Updated Item Eng",
            tstEngAbbrNm = "Updated Eng",
            tstIntNm = "Updated Internal",
            rstTypeShortYn = false,
            rstTypeLongYn = true,
            rstTypeFileYn = true,
            rstTypeUrlYn = true,
            diseaseCd = "D02",
            tstMethodCd = "METHOD02",
            refVal = "Updated Ref Val",
            engRefVal = "Updated Eng Ref Val",
            clncSgnf = "Updated Significance",
            engClncSgnf = "Updated Eng Significance",
            tstDesc = "Updated Description",
            tstEngDesc = "Updated Eng Description",
            tstDayweek = "Tue",
            tstTatday = 2,
            insuApplyCd = "APPLY02",
            insuCd = "INSU02",
            insuCateNo = "CATE02",
            updateReason = ""
        )
        val existing = TestItem(
            tstCd = id,
            tstLargeCateCd = "L01",
            tstMediumCateCd = "M01",
            startDt = LocalDate.of(2023, 1, 1),
            endDt = LocalDate.of(9999, 12, 31),
            useYn = true,
            reqPossYn = true,
            webKorYn = true,
            webEngYn = true,
            tstNm = "Test Item",
            tstAbbrNm = "Test",
            tstEngNm = "Test Item Eng",
            tstEngAbbrNm = "Test Eng",
            tstIntNm = "Internal Name",
            rstTypeShortYn = true,
            rstTypeLongYn = false,
            rstTypeFileYn = false,
            rstTypeUrlYn = false,
            diseaseCd = "D01",
            tstMethodCd = "METHOD01",
            refVal = "Ref Val",
            engRefVal = "Eng Ref Val",
            clncSgnf = "Significance",
            engClncSgnf = "Eng Significance",
            tstDesc = "Description",
            tstEngDesc = "Eng Description",
            tstDayweek = "Mon",
            tstTatday = 1,
            insuApplyCd = "APPLY01",
            insuCd = "INSU01",
            insuCateNo = "CATE01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = TestItemResponse(
            tstCd = id,
            tstLargeCateCd = "L02",
            tstMediumCateCd = "M02",
            startDt = LocalDate.of(2023, 1, 2),
            endDt = LocalDate.of(9999, 12, 31),
            useYn = false,
            reqPossYn = false,
            webKorYn = false,
            webEngYn = false,
            tstNm = "Updated Item",
            tstAbbrNm = "Updated",
            tstEngNm = "Updated Item Eng",
            tstEngAbbrNm = "Updated Eng",
            tstIntNm = "Updated Internal",
            rstTypeShortYn = false,
            rstTypeLongYn = true,
            rstTypeFileYn = true,
            rstTypeUrlYn = true,
            diseaseCd = "D02",
            tstMethodCd = "METHOD02",
            refVal = "Updated Ref Val",
            engRefVal = "Updated Eng Ref Val",
            clncSgnf = "Updated Significance",
            engClncSgnf = "Updated Eng Significance",
            tstDesc = "Updated Description",
            tstEngDesc = "Updated Eng Description",
            tstDayweek = "Tue",
            tstTatday = 2,
            insuApplyCd = "APPLY02",
            insuCd = "INSU02",
            insuCateNo = "CATE02",
            creator = "admin",
            createDtime = existing.createDtime,
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        val command = TestItemUpdateCommand(
            tstLargeCateCd = "L02",
            tstMediumCateCd = "M02",
            startDt = LocalDate.of(2023, 1, 2),
            endDt = LocalDate.of(9999, 12, 31),
            useYn = false,
            reqPossYn = false,
            webKorYn = false,
            webEngYn = false,
            tstNm = "Updated Item",
            tstAbbrNm = "Updated",
            tstEngNm = "Updated Item Eng",
            tstEngAbbrNm = "Updated Eng",
            tstIntNm = "Updated Internal",
            rstTypeShortYn = false,
            rstTypeLongYn = true,
            rstTypeFileYn = true,
            rstTypeUrlYn = true,
            diseaseCd = "D02",
            tstMethodCd = "METHOD02",
            refVal = "Updated Ref Val",
            engRefVal = "Updated Eng Ref Val",
            clncSgnf = "Updated Significance",
            engClncSgnf = "Updated Eng Significance",
            tstDesc = "Updated Description",
            tstEngDesc = "Updated Eng Description",
            tstDayweek = "Tue",
            tstTatday = 2,
            insuApplyCd = "APPLY02",
            insuCd = "INSU02",
            insuCateNo = "CATE02"
        )

        `when`(repository.findById(id)).thenReturn(existing)
        `when`(commandMapper.toUpdateCommand(request)).thenReturn(command)
        `when`(repository.save(any())).thenReturn(existing)
        `when`(mapper.toResponse(existing)).thenReturn(response)

        // When
        val result = service.updateItem(id, request, "admin")

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `getItem should return response`() = runTest {
        // Given
        val id = "uuid"
        val domain = TestItem(
            tstCd = id,
            tstLargeCateCd = "L01",
            tstMediumCateCd = "M01",
            startDt = LocalDate.of(2023, 1, 1),
            endDt = LocalDate.of(9999, 12, 31),
            useYn = true,
            reqPossYn = true,
            webKorYn = true,
            webEngYn = true,
            tstNm = "Test Item",
            tstAbbrNm = "Test",
            tstEngNm = "Test Item Eng",
            tstEngAbbrNm = "Test Eng",
            tstIntNm = "Internal Name",
            rstTypeShortYn = true,
            rstTypeLongYn = false,
            rstTypeFileYn = false,
            rstTypeUrlYn = false,
            diseaseCd = "D01",
            tstMethodCd = "METHOD01",
            refVal = "Ref Val",
            engRefVal = "Eng Ref Val",
            clncSgnf = "Significance",
            engClncSgnf = "Eng Significance",
            tstDesc = "Description",
            tstEngDesc = "Eng Description",
            tstDayweek = "Mon",
            tstTatday = 1,
            insuApplyCd = "APPLY01",
            insuCd = "INSU01",
            insuCateNo = "CATE01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = TestItemResponse(
            tstCd = id,
            tstLargeCateCd = "L01",
            tstMediumCateCd = "M01",
            startDt = LocalDate.of(2023, 1, 1),
            endDt = LocalDate.of(9999, 12, 31),
            useYn = true,
            reqPossYn = true,
            webKorYn = true,
            webEngYn = true,
            tstNm = "Test Item",
            tstAbbrNm = "Test",
            tstEngNm = "Test Item Eng",
            tstEngAbbrNm = "Test Eng",
            tstIntNm = "Internal Name",
            rstTypeShortYn = true,
            rstTypeLongYn = false,
            rstTypeFileYn = false,
            rstTypeUrlYn = false,
            diseaseCd = "D01",
            tstMethodCd = "METHOD01",
            refVal = "Ref Val",
            engRefVal = "Eng Ref Val",
            clncSgnf = "Significance",
            engClncSgnf = "Eng Significance",
            tstDesc = "Description",
            tstEngDesc = "Eng Description",
            tstDayweek = "Mon",
            tstTatday = 1,
            insuApplyCd = "APPLY01",
            insuCd = "INSU01",
            insuCateNo = "CATE01",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        `when`(repository.findById(id)).thenReturn(domain)
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.getItem(id)

        // Then
        assertEquals(response, result)
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T
}
