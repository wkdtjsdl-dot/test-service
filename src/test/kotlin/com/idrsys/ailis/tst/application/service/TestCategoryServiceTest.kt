package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.TestCategoryRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestCategoryResponse
import com.idrsys.ailis.tst.application.mapper.TestCategoryMapper
import com.idrsys.ailis.tst.application.required.TestCategoryRepository
import com.idrsys.ailis.tst.domain.model.TestCategory
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
class TestCategoryServiceTest {

    @Mock
    lateinit var testCategoryRepository: TestCategoryRepository

    @Mock
    lateinit var testCategoryMapper: TestCategoryMapper

    @InjectMocks
    lateinit var testCategoryService: TestCategoryService

    @Test
    fun `registerCategory should save and return response`() = runTest {
        // Given
        val request = TestCategoryRegisterRequest(
            tstLargeCateCd = "L01",
            tstMediumCateCd = "M01",
            cateNm = "Test Category",
            cateAbbrNm = "Test",
            cateEngNm = "Test Category Eng",
            cateEngAbbrNm = "Test Eng",
            useYn = true,
            sortOrder = 1
        )

        val savedDomain = TestCategory(
            tstCateId = "uuid",
            tstLargeCateCd = "L01",
            tstMediumCateCd = "M01",
            cateNm = "Test Category",
            cateAbbrNm = "Test",
            cateEngNm = "Test Category Eng",
            cateEngAbbrNm = "Test Eng",
            useYn = true,
            sortOrder = 1,
            creator = "system",
            createDtime = LocalDateTime.now(),
            updater = "system",
            updateDetime = LocalDateTime.now()
        )
        
        val response = TestCategoryResponse(
            tstCateId = "uuid",
            tstLargeCateCd = "L01",
            tstMediumCateCd = "M01",
            cateNm = "Test Category",
            cateAbbrNm = "Test",
            cateEngNm = "Test Category Eng",
            cateEngAbbrNm = "Test Eng",
            useYn = true,
            sortOrder = 1,
            creator = "system",
            createDtime = LocalDateTime.now(),
            updater = "system",
            updateDetime = LocalDateTime.now()
        )

        // Use helper to avoid NPE with Mockito.any() in Kotlin
        `when`(testCategoryRepository.save(any())).thenReturn(savedDomain)
        `when`(testCategoryMapper.toResponse(savedDomain)).thenReturn(response)

        // When
        val result = testCategoryService.registerCategory(request)

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
