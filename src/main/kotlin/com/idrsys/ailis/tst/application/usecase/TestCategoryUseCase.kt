package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.TestCategoryRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestCategoryResponse
import com.idrsys.ailis.tst.application.dto.TestCategoryUpdateRequest
import kotlinx.coroutines.flow.Flow

interface TestCategoryUseCase {
    suspend fun getCategoriesByLargeCategory(largeCateCd: String): Flow<TestCategoryResponse>
    suspend fun registerCategory(request: TestCategoryRegisterRequest, adminId: String): TestCategoryResponse
    suspend fun updateCategory(cateId: String, request: TestCategoryUpdateRequest, adminId: String): TestCategoryResponse
    suspend fun deleteCategory(cateId: String, adminId: String)
}
