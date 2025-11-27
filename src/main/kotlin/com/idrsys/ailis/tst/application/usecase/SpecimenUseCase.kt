package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import kotlinx.coroutines.flow.Flow

interface SpecimenUseCase {
    suspend fun getSpecimens(spcmNm: String?, spcmCateCd: String?): Flow<SpecimenResponse>
    suspend fun getSpecimen(spcmCd: String): SpecimenResponse
    suspend fun registerSpecimen(request: SpecimenRegisterRequest, adminId: String): SpecimenResponse
    suspend fun updateSpecimen(spcmCd: String, request: SpecimenUpdateRequest, adminId: String): SpecimenResponse
    suspend fun deleteSpecimen(spcmCd: String, adminId: String)
}
