package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.ifCustInfo.IfCustInfoCommand
import com.idrsys.ailis.sales.application.dto.request.ifCustInfo.IfCustInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.IfCustInfoResponse
import com.idrsys.ailis.sales.application.required.repository.ifConfInfo.IfConfInfoCustomRepository
import com.idrsys.ailis.sales.application.required.repository.ifConfInfo.IfConfInfoRepository
import com.idrsys.ailis.sales.application.required.repository.ifCustInfo.IfCustInfoCustomRepository
import com.idrsys.ailis.sales.application.required.repository.ifCustInfo.IfCustInfoRepository
import com.idrsys.ailis.sales.application.usecase.ifCustInfo.IfCustInfoUseCase
import com.idrsys.ailis.sales.domain.model.IfConfInfo
import com.idrsys.ailis.sales.shared.constant.IfCustInfoErrorCode
import com.idrsys.ailis.sales.shared.mapper.IfConfInfoMapper
import com.idrsys.ailis.sales.shared.mapper.IfCustInfoMapper
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = false)
class IfCustInfoService(
    private val ifCustInfoRepository: IfCustInfoRepository,
    private val ifCustInfoCustomRepository: IfCustInfoCustomRepository,
    private val ifConfInfoRepository: IfConfInfoRepository,
    private val ifConfInfoCustomRepository: IfConfInfoCustomRepository,
    private val ifCustInfoMapper: IfCustInfoMapper,
    private val ifConfInfoMapper: IfConfInfoMapper,
) : IfCustInfoUseCase {

    override suspend fun getIfCustInfoPage(
        searchParam: IfCustInfoSearchParam,
        pageable: Pageable
    ): Page<IfCustInfoResponse> {
        val total = ifCustInfoCustomRepository.countIfCustInfos(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val ifCustInfos = ifCustInfoCustomRepository.findIfCustInfos(searchParam, pageable).map { custInfoQuery ->
            // 1. 부모 객체를 Response DTO로 변환
            val response = ifCustInfoMapper.toResponseFromQuery(custInfoQuery)

            // 2. 자식(컬럼 매핑) 목록을 조회 (이미 필드명 포함)
            val confInfoList = ifConfInfoCustomRepository.findByIfCustInfoId(custInfoQuery.ifCustInfoId!!)
                .map { confInfoQuery -> ifConfInfoMapper.toResponseFromQuery(confInfoQuery) }
                .toList()

            // 3. 부모 DTO에 자식 목록을 결합하여 반환
            response.copy(confInfoList = confInfoList)
        }.toList()

        return PageImpl(ifCustInfos, pageable, total)
    }

    override suspend fun getIfCustInfoDetail(ifCustInfoId: String): IfCustInfoResponse {
        // 1. 마스터 조회
        val custInfoQuery = ifCustInfoCustomRepository.findIfCustInfoById(ifCustInfoId)
            ?: throw UserDefinedException(
                IfCustInfoErrorCode.NOT_FOUND_CODE,
                IfCustInfoErrorCode.NOT_FOUND_MESSAGE
            )

        // 2. Query → Response 변환
        val response = ifCustInfoMapper.toResponseFromQuery(custInfoQuery)

        // 3. 컬럼 매핑 조회 및 변환
        val confInfoList = ifConfInfoCustomRepository.findByIfCustInfoId(ifCustInfoId)
            .map { ifConfInfoMapper.toResponseFromQuery(it) }
            .toList()

        // 4. Response에 confInfoList 추가
        return response.copy(confInfoList = confInfoList)
    }

    @Transactional
    override suspend fun createIfCustInfo(
        command: IfCustInfoCommand,
        adminId: String
    ): IfCustInfoResponse {
        validateCommand(command)
        val now = LocalDateTime.now()

        // 1. 기존 데이터 삭제 (custMstId 기준)
        ifCustInfoRepository.deleteByCustMstId(command.custMstId)

        // 2. 기존 매핑도 함께 삭제될 수 있도록 처리 (FK 제약조건에 따라)
        // 또는 명시적으로 삭제 필요 시 아래 로직 추가
        // val existingConfig = ifCustInfoCustomRepository.findByCustMstId(command.custMstId)
        // if (existingConfig != null) {
        //     ifConfInfoRepository.deleteByIfCustInfoId(existingConfig.ifCustInfoId!!)
        // }

        // 3. 신규 데이터 INSERT
        val ifCustInfo = ifCustInfoMapper.toDomain(command, adminId, now)
            .apply { setAsNew() }

        val savedIfCustInfo = ifCustInfoRepository.save(ifCustInfo)

        // 4. 컬럼 매핑 저장
        saveConfInfoList(savedIfCustInfo.ifCustInfoId!!, command.confInfoList, adminId, now)

        // 5. 조회 후 반환
        return getIfCustInfoDetail(savedIfCustInfo.ifCustInfoId)
    }

    @Transactional
    override suspend fun updateIfCustInfo(
        ifCustInfoId: String,
        command: IfCustInfoCommand,
        adminId: String
    ): IfCustInfoResponse {
        // 단순 DELETE → INSERT 방식이므로 createIfCustInfo와 동일
        return createIfCustInfo(command, adminId)
    }

    @Transactional
    override suspend fun deleteIfCustInfo(ifCustInfoId: String) {
        // 1. 매핑 먼저 삭제 (FK 제약조건)
        ifConfInfoRepository.deleteByIfCustInfoId(ifCustInfoId)

        // 2. 마스터 삭제
        ifCustInfoRepository.deleteById(ifCustInfoId)
    }

    // === Private Helper Methods ===

    private fun validateCommand(command: IfCustInfoCommand) {
        // 컬럼 매핑이 비어있는지 검증
        if (command.confInfoList.isEmpty()) {
            throw UserDefinedException(
                IfCustInfoErrorCode.EMPTY_CONF_LIST_CODE,
                IfCustInfoErrorCode.EMPTY_CONF_LIST_MESSAGE
            )
        }
    }

    private suspend fun saveConfInfoList(
        ifCustInfoId: String,
        confInfoList: List<com.idrsys.ailis.sales.application.dto.request.ifCustInfo.IfConfInfoCommand>,
        adminId: String,
        now: LocalDateTime
    ) {
        confInfoList.forEach { confCmd ->
            val ifConfInfo = IfConfInfo(
                ifConfInfoId = null,
                ifCustInfoId = ifCustInfoId,
                ifFieldInfoId = confCmd.ifFieldInfoId,
                colIdx = confCmd.colIdx,
                creator = adminId,
                createDtime = now,
                updater = adminId,
                updateDtime = now
            ).apply { setAsNew() }

            try {
                ifConfInfoRepository.save(ifConfInfo)
            } catch (ex: Exception) {
                handleUniqueConstraintViolation(ex, confCmd)
            }
        }
    }

    private fun handleUniqueConstraintViolation(
        ex: Exception,
        confCmd: com.idrsys.ailis.sales.application.dto.request.ifCustInfo.IfConfInfoCommand
    ) {
        val errorMessage = ex.message?.lowercase() ?: ""

        when {
            errorMessage.contains("uk_if_conf_field") || errorMessage.contains("duplicate") -> {
                throw UserDefinedException(
                    IfCustInfoErrorCode.DUPLICATE_FIELD_CODE,
                    "${IfCustInfoErrorCode.DUPLICATE_FIELD_MESSAGE} (필드ID: ${confCmd.ifFieldInfoId})"
                )
            }
            errorMessage.contains("uk_if_conf_col_idx") -> {
                throw UserDefinedException(
                    IfCustInfoErrorCode.DUPLICATE_COL_IDX_CODE,
                    "${IfCustInfoErrorCode.DUPLICATE_COL_IDX_MESSAGE} (컬럼 인덱스: ${confCmd.colIdx})"
                )
            }
            else -> throw ex
        }
    }
}
