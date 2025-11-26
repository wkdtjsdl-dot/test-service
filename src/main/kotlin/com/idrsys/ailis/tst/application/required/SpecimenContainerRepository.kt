package com.idrsys.ailis.tst.application.required

import com.idrsys.ailis.tst.domain.model.SpecimenContainer
import kotlinx.coroutines.flow.Flow

interface SpecimenContainerRepository {
    // Basic CRUD
    suspend fun save(specimenContainer: SpecimenContainer): SpecimenContainer
    suspend fun findById(id: String): SpecimenContainer?
    suspend fun deleteById(id: String)

    // Custom Queries
    suspend fun findAllByCntnNm(cntnNm: String?): Flow<SpecimenContainer>
}
