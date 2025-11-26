package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import kotlinx.coroutines.flow.Flow

interface SpecimenContainerUseCase {
    suspend fun getContainers(cntnNm: String?): Flow<SpecimenContainerResponse>
    suspend fun getContainer(spcmCntnCd: String): SpecimenContainerResponse
    suspend fun registerContainer(request: SpecimenContainerRegisterRequest): SpecimenContainerResponse
    suspend fun updateContainer(spcmCntnCd: String, request: SpecimenContainerUpdateRequest): SpecimenContainerResponse
    suspend fun deleteContainer(spcmCntnCd: String)
}

interface SpecimenUseCase {
    suspend fun getSpecimens(spcmNm: String?, spcmCateCd: String?): Flow<SpecimenResponse>
    suspend fun getSpecimen(spcmCd: String): SpecimenResponse
    suspend fun registerSpecimen(request: SpecimenRegisterRequest): SpecimenResponse
    suspend fun updateSpecimen(spcmCd: String, request: SpecimenUpdateRequest): SpecimenResponse
    suspend fun deleteSpecimen(spcmCd: String)
}
