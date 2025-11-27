package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.TestCategoryRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestCategoryResponse
import com.idrsys.ailis.tst.application.dto.TestCategoryUpdateRequest
import com.idrsys.ailis.tst.application.mapper.TestCategoryMapper
import com.idrsys.ailis.tst.application.required.TestCategoryRepository
import com.idrsys.ailis.tst.application.usecase.TestCategoryUseCase
import com.idrsys.ailis.tst.domain.model.TestCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class TestCategoryService(
    private val testCategoryRepository: TestCategoryRepository,
    private val testCategoryMapper: TestCategoryMapper
) : TestCategoryUseCase {

    @Transactional(readOnly = true)
    override suspend fun getCategoriesByLargeCategory(largeCateCd: String): Flow<TestCategoryResponse> {
        return testCategoryRepository.findByLargeCateCd(largeCateCd)
            .map { testCategoryMapper.toResponse(it) }
    }

    @Transactional
    override suspend fun registerCategory(request: TestCategoryRegisterRequest, adminId: String): TestCategoryResponse {
        val category = TestCategory(
            tstCateId = UUID.randomUUID().toString(),
            tstLargeCateCd = request.tstLargeCateCd,
            tstMediumCateCd = request.tstMediumCateCd,
            cateNm = request.cateNm,
            cateAbbrNm = request.cateAbbrNm,
            cateEngNm = request.cateEngNm,
            cateEngAbbrNm = request.cateEngAbbrNm,
            useYn = request.useYn,
            sortOrder = request.sortOrder,
            creator = adminId,
            createDtime = LocalDateTime.now(),
            updater = adminId,
            updateDetime = LocalDateTime.now()
        ).apply { setAsNew() }

        val saved = testCategoryRepository.save(category)
        return testCategoryMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun updateCategory(cateId: String, request: TestCategoryUpdateRequest, adminId: String): TestCategoryResponse {
        val category = testCategoryRepository.findById(cateId)
            ?: throw NoSuchElementException("Category not found: $cateId")

        category.update(
            cateNm = request.cateNm,
            cateAbbrNm = request.cateAbbrNm,
            cateEngNm = request.cateEngNm,
            cateEngAbbrNm = request.cateEngAbbrNm,
            useYn = request.useYn,
            sortOrder = request.sortOrder,
            updater = adminId
        )

        val saved = testCategoryRepository.save(category)
        return testCategoryMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteCategory(cateId: String, adminId: String) {
        val category = testCategoryRepository.findById(cateId)
            ?: throw NoSuchElementException("Category not found: $cateId")

        category.delete(updater = adminId)

        testCategoryRepository.save(category)
    }
}
