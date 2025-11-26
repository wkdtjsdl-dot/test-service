package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.TestCategoryRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestCategoryResponse
import com.idrsys.ailis.tst.application.dto.TestCategoryUpdateRequest
import kotlinx.coroutines.flow.Flow

interface TestCategoryUseCase {
    suspend fun getCategoriesByLargeCategory(largeCateCd: String): Flow<TestCategoryResponse>
    suspend fun registerCategory(request: TestCategoryRegisterRequest): TestCategoryResponse
    suspend fun updateCategory(mediumCateCd: String, request: TestCategoryUpdateRequest): TestCategoryResponse
    suspend fun deleteCategory(mediumCateCd: String)
}
