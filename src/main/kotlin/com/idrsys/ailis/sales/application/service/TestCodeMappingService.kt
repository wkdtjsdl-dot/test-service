package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.InnerTestCodeSearchParam
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingCommand
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingSearchParam
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.ValidateTstMappingRequest
import com.idrsys.ailis.sales.application.dto.response.InnerTestCodeMappingResponse
import com.idrsys.ailis.sales.application.dto.response.TestCodeMappingExcelValidResponse
import com.idrsys.ailis.sales.application.dto.response.TestCodeMappingResponse
import com.idrsys.ailis.sales.application.dto.response.ValidateTstMappingResponse
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
import com.idrsys.ailis.sales.application.required.repository.testCodeMapping.TestCodeMappingCustomRepository
import com.idrsys.ailis.sales.application.required.repository.testCodeMapping.TestCodeMappingRepository
import com.idrsys.ailis.sales.application.usecase.testCodeMapping.TestCodeMappingUseCase
import com.idrsys.ailis.sales.domain.model.CustTestCodeMapping
import com.idrsys.ailis.sales.shared.mapper.TestCodeMappingMapper
import kotlinx.coroutines.flow.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class TestCodeMappingService(
    private val testCodeMappingCustomRepository: TestCodeMappingCustomRepository,
    private val testCodeMappingRepository: TestCodeMappingRepository,
    private val custCustomRepository: CustCustomRepository,
    private val testCodeMappingMapper: TestCodeMappingMapper,
) : TestCodeMappingUseCase {
    override suspend fun getTestCodeMappingPage(searchParam: TestCodeMappingSearchParam, pageable: Pageable
    ): Page<TestCodeMappingResponse> {
        val total = testCodeMappingCustomRepository.countTestCodeMapping(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val testCodeMappings = testCodeMappingCustomRepository.findTestCodeMappings(searchParam, pageable)
            .map { dto -> testCodeMappingMapper.toResponse(dto) }.toList()

        return PageImpl(testCodeMappings, pageable, total)
    }

    override suspend fun createTestCodeMapping(command: TestCodeMappingCommand, adminId: String): TestCodeMappingResponse {
        val custCd = command.custCd
        val custMstId = custCustomRepository.findCustMstIdByCustCd(custCd)
            ?: throw NoSuchElementException("고객을 찾을 수 없습니다: $custCd")
        val now = LocalDateTime.now()

        command.apply {
            custTstCdMpgId = UUID.randomUUID()
            this.custMstId = custMstId
        }

        val newTestCodeMapping = testCodeMappingMapper.toDomain(command, adminId, now)
        newTestCodeMapping.setAsNew()
        val savedTestCodeMapping = testCodeMappingRepository.save(newTestCodeMapping)

        return testCodeMappingMapper.toResponse(savedTestCodeMapping)
    }

    override suspend fun updateTestCodeMapping(custTstCdMpgId: String, command: TestCodeMappingCommand, adminId: String): TestCodeMappingResponse {
        val mappingToUpdate = testCodeMappingCustomRepository.findById(custTstCdMpgId)
            ?: throw NoSuchElementException("매핑 코드를 찾을 수 없습니다: $custTstCdMpgId")

        mappingToUpdate.update(command, adminId)

        val savedMapping = testCodeMappingRepository.save(mappingToUpdate)
        return testCodeMappingMapper.toResponse(savedMapping)
    }

    override suspend fun deleteTestCodeMapping(custTstCdMpgId: String) {
        return testCodeMappingCustomRepository.deleteById(custTstCdMpgId)
    }

    override suspend fun validTestCodeMappingByExcel(commands: List<TestCodeMappingCommand>): Flow<TestCodeMappingExcelValidResponse> {
        return flow {
            for (command in commands) {
                val custExists = custCustomRepository.findCustMstIdByCustCd(command.custCd) != null
                val tstCdExists = true   // 검사 코드 테이블 나오면 추가 예정
                emit(
                    TestCodeMappingExcelValidResponse(
                        custCd = command.custCd,
                        tstCd = command.tstCd!!,
                        validCustCd = custExists,
                        validTstCd = tstCdExists
                    )
                )
            }
        }
    }

    override suspend fun createTestCodeMappingByExcel(commands: List<TestCodeMappingCommand>, adminId: String): Flow<TestCodeMappingResponse> {
        val now = LocalDateTime.now()
        val newMappings = mutableListOf<CustTestCodeMapping>()

        for (command in commands) {
            val custCd = command.custCd
            val custMstId = custCustomRepository.findCustMstIdByCustCd(custCd)
                ?: throw NoSuchElementException("고객을 찾을 수 없습니다: $custCd")

            command.apply {
                custTstCdMpgId = UUID.randomUUID()
                this.custMstId = custMstId
            }

            val newTestCodeMapping = testCodeMappingMapper.toDomain(command, adminId, now)
            newTestCodeMapping.setAsNew()
            newMappings.add(newTestCodeMapping)
        }

        return testCodeMappingRepository.saveAll(newMappings)
            .asFlow()
            .map { savedMapping -> testCodeMappingMapper.toResponse(savedMapping) }
    }

    override suspend fun searchTestCodeMappingList(searchParam: TestCodeMappingSearchParam): Flow<TestCodeMappingResponse> =
        testCodeMappingCustomRepository.searchTestCodeMappingList(searchParam)
            .map { testCodeMapping ->
                testCodeMappingMapper.toResponse(testCodeMapping)
            }

    override suspend fun innerSearchTestCodeMappingList(searchParam: InnerTestCodeSearchParam): List<InnerTestCodeMappingResponse> {
        return testCodeMappingCustomRepository.innerSearchTestCodeMappingList(searchParam)
    }

    override suspend fun getTstCdByCustCdAndCustTstCd(custCd: String, custTstCd: String): String? =
        testCodeMappingCustomRepository.findTstCdByCustCdAndCustTstCd(custCd, custTstCd)

    override suspend fun validateCustTstMappings(request: ValidateTstMappingRequest): ValidateTstMappingResponse {
        val existingTstCds = testCodeMappingCustomRepository.findExistingTstCdsByCustCd(request.custCd, request.tstCds)
        val invalidCodes = request.tstCds.toSet() - existingTstCds.toSet()
        return ValidateTstMappingResponse(invalidCodes = invalidCodes.toList())
    }
}
