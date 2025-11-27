package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import kotlinx.coroutines.flow.Flow

interface SpecimenContainerUseCase {
    suspend fun getContainers(cntnNm: String?): Flow<SpecimenContainerResponse>
    suspend fun getContainer(spcmCntnCd: String): SpecimenContainerResponse
    suspend fun registerContainer(request: SpecimenContainerRegisterRequest, adminId: String): SpecimenContainerResponse
    suspend fun updateContainer(spcmCntnCd: String, request: SpecimenContainerUpdateRequest, adminId: String): SpecimenContainerResponse
    suspend fun deleteContainer(spcmCntnCd: String, adminId: String)
}
