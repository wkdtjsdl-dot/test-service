package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.TestCategoryRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestCategoryResponse
import com.idrsys.ailis.tst.application.dto.TestCategoryUpdateRequest
import com.idrsys.ailis.tst.application.mapper.TestCategoryCommandMapper
import com.idrsys.ailis.tst.application.mapper.TestCategoryMapper
import com.idrsys.ailis.tst.application.required.repository.TestCategoryRepository
import com.idrsys.ailis.tst.application.usecase.TestCategoryUseCase
import com.idrsys.ailis.tst.domain.model.TestCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TestCategoryService(
    private val testCategoryRepository: TestCategoryRepository,
    private val testCategoryMapper: TestCategoryMapper,
    private val commandMapper: TestCategoryCommandMapper
) : TestCategoryUseCase {

    @Transactional(readOnly = true)
    override fun getCategoriesByLargeCategory(largeCateCd: String?, useYn: Boolean?): Flow<TestCategoryResponse> {
        return testCategoryRepository.findByLargeCateCd(largeCateCd, useYn)
            .map { testCategoryMapper.toResponse(it) }
    }

    @Transactional
    override suspend fun registerCategory(request: TestCategoryRegisterRequest, adminId: String): TestCategoryResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = LocalDateTime.now()
        val category = TestCategory.create(command, adminId, now)
        val saved = testCategoryRepository.save(category)
        return testCategoryMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun updateCategory(cateId: String, request: TestCategoryUpdateRequest, adminId: String): TestCategoryResponse {
        val category = testCategoryRepository.findById(cateId)
            ?: throw NoSuchElementException("Category not found: $cateId")

        val command = commandMapper.toUpdateCommand(request)
        val now = LocalDateTime.now()
        category.update(command, adminId, now)

        val saved = testCategoryRepository.save(category)
        return testCategoryMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteCategory(cateId: String, adminId: String) {
        val category = testCategoryRepository.findById(cateId)
            ?: throw NoSuchElementException("Category not found: $cateId")

//        val now = LocalDateTime.now()
//        category.delete(updater = adminId, updateDtime = now)
//
//        testCategoryRepository.save(category)
        testCategoryRepository.deleteById(cateId)
    }
}
