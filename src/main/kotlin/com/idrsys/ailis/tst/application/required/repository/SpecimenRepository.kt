package com.idrsys.ailis.tst.application.required.repository

import com.idrsys.ailis.tst.domain.model.Specimen
import kotlinx.coroutines.flow.Flow

interface SpecimenRepository {
    // Basic CRUD
    suspend fun save(specimen: Specimen): Specimen
    suspend fun findById(id: String): Specimen?
    suspend fun deleteById(id: String)

    // Custom Queries
    suspend fun findAll(spcmNm: String?, spcmCateCd: String?): Flow<Specimen>
}
