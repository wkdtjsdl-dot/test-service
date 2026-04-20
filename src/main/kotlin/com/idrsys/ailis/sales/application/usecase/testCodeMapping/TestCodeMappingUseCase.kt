package com.idrsys.ailis.sales.application.usecase.testCodeMapping

import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.CustTstCdBulkSearchParam
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.InnerTestCodeSearchParam
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingCommand
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingSearchParam
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.ValidateTstMappingRequest
import com.idrsys.ailis.sales.application.dto.response.InnerTestCodeMappingResponse
import com.idrsys.ailis.sales.application.dto.response.TestCodeMappingExcelValidResponse
import com.idrsys.ailis.sales.application.dto.response.TestCodeMappingResponse
import com.idrsys.ailis.sales.application.dto.response.ValidateTstMappingResponse
import com.idrsys.ailis.sales.application.dto.response.inner.CustTstCdInnerResponse
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TestCodeMappingUseCase {
    suspend fun getTestCodeMappingPage(searchParam: TestCodeMappingSearchParam, pageable: Pageable): Page<TestCodeMappingResponse>

    suspend fun createTestCodeMapping(command: TestCodeMappingCommand, adminId: String): TestCodeMappingResponse

    suspend fun updateTestCodeMapping(custTstCdMpgId:String, command: TestCodeMappingCommand, adminId: String): TestCodeMappingResponse

    suspend fun deleteTestCodeMapping(custTstCdMpgId:String)

    suspend fun validTestCodeMappingByExcel(commands: List<TestCodeMappingCommand>): Flow<TestCodeMappingExcelValidResponse>

    suspend fun createTestCodeMappingByExcel(commands: List<TestCodeMappingCommand>, adminId: String): Flow<TestCodeMappingResponse>

    suspend fun searchTestCodeMappingList(searchParam: TestCodeMappingSearchParam): Flow<TestCodeMappingResponse>

    suspend fun innerSearchTestCodeMappingList(searchParam: InnerTestCodeSearchParam): List<InnerTestCodeMappingResponse>

    /**
     * 고객코드 + 고객검사코드로 내부 검사코드 조회 (Inner API용)
     * @param custCd 고객코드
     * @param custTstCd 고객검사코드
     * @return 내부 검사코드 (없으면 null)
     */
    suspend fun getTstCdByCustCdAndCustTstCd(custCd: String, custTstCd: String): String?

    /**
     * 고객 검사코드 매핑 일괄 유효성 검사
     *
     * @param request 유효성 검사 요청 DTO
     * @return 유효하지 않은 검사코드 목록
     */
    suspend fun validateCustTstMappings(request: ValidateTstMappingRequest): ValidateTstMappingResponse

    /**
     * (custCd, tstCd) 쌍 리스트로 고객검사코드(custTstCd) 벌크 조회 (Inner API용)
     */
    suspend fun getCustTstCdBulk(searchParam: CustTstCdBulkSearchParam): List<CustTstCdInnerResponse>
}
