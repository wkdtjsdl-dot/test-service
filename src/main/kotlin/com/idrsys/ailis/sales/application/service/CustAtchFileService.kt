package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.custatchfile.CustAtchFileCommand
import com.idrsys.ailis.sales.application.dto.request.custatchfile.CustAtchFileSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustAtchFileResponse
import com.idrsys.ailis.sales.application.required.repository.custaddinfo.CustAddInfoRepository
import com.idrsys.ailis.sales.application.required.repository.custatchfile.CustAtchFileCustomRepository
import com.idrsys.ailis.sales.application.required.repository.custatchfile.CustAtchFileRepository
import com.idrsys.ailis.sales.application.usecase.custatchfile.CustAtchFileUseCase
import com.idrsys.ailis.sales.shared.mapper.CustAtchFileMapper
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CustAtchFileService(
    private val custAtchFileRepository: CustAtchFileRepository,
    private val custAtchFileCustomRepository: CustAtchFileCustomRepository,
    private val custAddInfoRepository: CustAddInfoRepository,
    private val custAtchFileMapper: CustAtchFileMapper
) : CustAtchFileUseCase {

    override fun getFiles(searchParam: CustAtchFileSearchParam): Flow<CustAtchFileResponse> {
        return flow {
            val custAddInfo = custAddInfoRepository.findById(searchParam.custAddInfoId)
                ?: throw UserDefinedException("NOT_FOUND", "고객 추가정보를 찾을 수 없습니다.")
            emit(custAddInfo.custMstId)
        }.flatMapConcat { custMstId ->
            custAtchFileCustomRepository.findAllByCustMstId(custMstId)
                .map(custAtchFileMapper::toResponseFromQuery)
        }
    }

    @Transactional
    override suspend fun saveFile(command: CustAtchFileCommand, creator: String): CustAtchFileResponse {
        val now = LocalDateTime.now()
        val custAtchFile = custAtchFileMapper.toDomain(command, creator, now)
        custAtchFile.setAsNew()
        val saved = custAtchFileRepository.save(custAtchFile)
        return custAtchFileMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteFile(custAtchFileId: String) {
        custAtchFileRepository.deleteById(custAtchFileId)
    }
}
