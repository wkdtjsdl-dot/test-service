package com.idrsys.ailis.tst.application.required

import com.idrsys.ailis.tst.domain.model.TestCategory
import kotlinx.coroutines.flow.Flow

interface TestCategoryRepository {
    // Basic CRUD (Delegated to Spring Data)
    suspend fun save(testCategory: TestCategory): TestCategory
    suspend fun findById(id: String): TestCategory?
    suspend fun deleteById(id: String)

    // Custom Queries (Implemented with jOOQ)
    suspend fun findByLargeCateCd(largeCateCd: String): Flow<TestCategory>
}
