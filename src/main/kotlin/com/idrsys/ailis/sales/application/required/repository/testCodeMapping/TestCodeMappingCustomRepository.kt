package com.idrsys.ailis.sales.application.required.repository.testCodeMapping

import com.idrsys.ailis.sales.application.dto.query.TestCodeMappingQuery
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.InnerTestCodeSearchParam
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingSearchParam
import com.idrsys.ailis.sales.application.dto.response.InnerTestCodeMappingResponse
import com.idrsys.ailis.sales.domain.model.CustTestCodeMapping
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface TestCodeMappingCustomRepository {
    suspend fun countTestCodeMapping(searchParam: TestCodeMappingSearchParam): Long
    fun findTestCodeMappings(searchParam: TestCodeMappingSearchParam, pageable: Pageable): Flow<TestCodeMappingQuery>
    fun searchTestCodeMappingList(searchParam: TestCodeMappingSearchParam): Flow<TestCodeMappingQuery>
    suspend fun innerSearchTestCodeMappingList(searchParam: InnerTestCodeSearchParam): List<InnerTestCodeMappingResponse>
    suspend fun deleteById(id: String)
    suspend fun findById(id: String): CustTestCodeMapping?
    /**
     * 고객코드 + 고객검사코드로 내부 검사코드(tstCd) 조회
     * req-service의 엑셀 업로드 시 고객검사코드 → 내부검사코드 변환에 사용
     *
     * @param custCd 고객코드
     * @param custTstCd 고객검사코드
     * @return 내부 검사코드 (없으면 null)
     */
    suspend fun findTstCdByCustCdAndCustTstCd(custCd: String, custTstCd: String): String?

    /**
     * 주어진 고객코드(custCd)에 대해 주어진 검사코드(tstCds) 목록 중 존재하는 코드들을 조회합니다.
     *
     * @param custCd 고객코드
     * @param tstCds 검사코드 목록
     * @return DB에 존재하는 검사코드 목록
     */
    suspend fun findExistingTstCdsByCustCd(custCd: String, tstCds: List<String>): List<String?>
}