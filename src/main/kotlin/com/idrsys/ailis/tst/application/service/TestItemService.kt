package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.TestItemCommandMapper
import com.idrsys.ailis.tst.application.mapper.TestItemMapper
import com.idrsys.ailis.tst.application.required.repository.TestItemRepository
import com.idrsys.ailis.tst.application.usecase.TestItemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.idrsys.ailis.tst.domain.model.StandardCharge
import com.idrsys.ailis.tst.domain.model.TestItem
import com.idrsys.ailis.tst.domain.model.TestItemEssentialDoc
import com.idrsys.ailis.tst.domain.model.TestItemSpecimen
import com.idrsys.ailis.tst.domain.model.TestItemRefItem
import com.idrsys.ailis.tst.domain.model.TestItemGene
import com.idrsys.ailis.tst.domain.model.TestItemHst
import com.idrsys.ailis.tst.domain.model.TestItemSpecimenHst
import kotlinx.coroutines.flow.toList
import java.time.LocalDateTime
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Service
@Transactional(readOnly = false)
class TestItemService(
    private val repository: TestItemRepository,
    private val mapper: TestItemMapper,
    private val commandMapper: TestItemCommandMapper
) : TestItemUseCase {

    // --- TestItem ---

    override suspend fun registerItem(request: TestItemRegisterRequest, adminId: String): TestItemResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = LocalDateTime.now()
        val domain = TestItem.create(command, adminId, now)
        val saved = repository.save(domain)
        val hist = mapper.toDomain(saved,  "신규 생성").apply { setAsNew() }
        repository.saveTestItemHistory(hist)
        return mapper.toResponse(saved)

    }

    @Transactional(readOnly = true)
    override suspend fun getItem(tstCd: String): TestItemResponse {
        val domain = repository.findById(tstCd) ?: throw RuntimeException("TestItem not found with id: $tstCd")
        return mapper.toResponse(domain)
    }

    override suspend fun updateItem(tstCd: String, request: TestItemUpdateRequest, adminId: String): TestItemResponse {
        val existing = repository.findById(tstCd) ?: throw RuntimeException("TestItem not found with id: $tstCd")



        val command = commandMapper.toUpdateCommand(request)
        val now = LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.save(existing)
        // Save history
        val hist = mapper.toDomain(existing, request.updateReason ?: "").apply { setAsNew() }
        repository.saveTestItemHistory(hist)
        return mapper.toResponse(saved)
    }

    @Transactional(readOnly = true)
    override fun getItems(searchParam: TestItemSearchParam): Flow<TestItemResponse> {
        return repository.getItems(searchParam).map { mapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun autoCompleteItems(searchParam: TestItemAutoCompleteParam): Flow<TestItemSimpleResponse> {
        return repository.autoCompleteItems(searchParam)
    }

    override suspend fun findSimpleItemByTstCd(tstCds: List<String>): Flow<TestItemSimpleResponse> {
        return repository.findSimpleItemByTstCd(tstCds)
    }

    override suspend fun findSimpleItemAll(): Flow<TestItemSimpleResponse> {
        return repository.findAll().map { mapper.toSimpleResponse(it) }
    }

    // --- StandardCharge ---

    override suspend fun registerCharge(request: StandardChargeRegisterRequest, adminId: String): StandardChargeResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = java.time.LocalDateTime.now()
        val newDomain = StandardCharge.create(command, adminId, now)
        val overlapped = repository.getEqualDate(newDomain).toList()

        require(overlapped.isEmpty()) {
            "이미 해당 기간과 겹치는 요금 데이터가 존재합니다."
        }

        val saved = repository.saveCharge(newDomain)
        return mapper.toResponse(saved)
    }

    @Transactional(readOnly = true)
    override suspend fun getCharge(id: String): StandardChargeResponse {
        val domain = repository.findChargeById(id) ?: throw RuntimeException("StandardCharge not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun deleteCharge(id: String, adminId: String) {
        repository.deleteChargeById(id)
    }

    override suspend fun updateCharge(id: String, request: StandardChargeUpdateRequest,adminId: String): StandardChargeResponse {
        val existing = repository.findChargeById(id) ?: throw RuntimeException("StandardCharge not found with id: $id")
        val command = commandMapper.toUpdateCommand(request)
        val now = LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.saveCharge(existing)
        return mapper.toResponse(saved)
    }

    @Transactional(readOnly = true)
    override suspend fun getChargesByTest(tstCd: String): Flow<StandardChargeResponse> {
        return repository.findChargesByTestCd(tstCd).map { mapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    suspend fun getEqualDate(request: StandardChargeRegisterRequest, adminId: String): Flow<StandardCharge> {
        val command = commandMapper.toCreateCommand(request)
        val now = java.time.LocalDateTime.now()
        val domain = StandardCharge.create(command, adminId, now)
        return repository.getEqualDate(domain)
    }

    // --- TestItemSpecimen ---

    override suspend fun registerSpecimen(request: TestItemSpecimenRegisterRequest, adminId: String): TestItemSpecimenDetailResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = LocalDateTime.now()
        val domain = TestItemSpecimen.create(command, adminId, now)
        val saved = repository.saveSpecimen(domain)

        val hist = mapper.toDomain(saved,  "신규 생성").apply { setAsNew() }
        repository.saveTestItemSpecimenHistory(hist)
        return  repository.getSpecimenDetailById(saved.spcmId!!) ?: throw RuntimeException("TestItemSpecimen not found after save")

    }

    @Transactional(readOnly = true)
    override suspend fun getSpecimen(spcmId: String): TestItemSpecimenDetailResponse {
        return repository.getSpecimenDetailById(spcmId) ?: throw RuntimeException("TestItemSpecimen not found with id: $spcmId")
    }

    @Transactional
    override suspend fun updateSpecimen(
        spcmId: String,
        request: TestItemSpecimenUpdateRequest,
        adminId: String
    ): TestItemSpecimenDetailResponse {
        val existing = repository.findSpecimenById(spcmId) ?: throw RuntimeException("TestItemSpecimen not found with id: $spcmId")

        val command = commandMapper.toUpdateCommand(request)
        val now = LocalDateTime.now()
        existing.update(command, adminId, now)
        val updated = repository.saveSpecimen(existing)
        // 히스토리 저장
        val hist = mapper.toDomain(existing, request.updateReason ?: "").apply { setAsNew() }
        repository.saveTestItemSpecimenHistory(hist)
        return repository.getSpecimenDetailById(updated.spcmId!!) ?: throw RuntimeException("TestItemSpecimen not found after update")
    }

    override suspend fun deleteSpecimen(id: String, adminId: String) {
        repository.deleteSpecimenById(id)
    }

    @Transactional(readOnly = true)
    override fun getSpecimensByTest(tstCd: String): Flow<TestItemSpecimenListResponse> {
        return repository.getSpecimenDetailsByTestCd(tstCd)
    }

    @Transactional(readOnly = true)
    override suspend fun getSpecimensByTstCds(tstCds: List<String>): Flow<TestItemSpecimensResponse> {
        return repository.findSpecimensByTstCds(tstCds)
    }

    // --- TestItemRefItem ---

    override suspend fun registerRefItem(request: TestItemRefItemRegisterRequest, adminId: String): TestItemRefItemResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = LocalDateTime.now()
        val domain = TestItemRefItem.create(command, adminId, now)
        val saved = repository.saveRefItem(domain)
        return mapper.toResponse(saved)
    }

    @Transactional(readOnly = true)
    override suspend fun getRefItem(refItemId: String): TestItemRefDetailResponse {
        return repository.getDetailRefItemById(refItemId) ?: throw RuntimeException("TestItemRefItem not found with id: $refItemId")
    }

    override suspend fun updateRefItem(refItemId: String, request: TestItemRefItemUpdateRequest, adminId: String): TestItemRefItemResponse {
        val existing = repository.findRefItemById(refItemId) ?: throw RuntimeException("TestItemRefItem not found with id: $refItemId")
        val command = commandMapper.toUpdateCommand(request)
        val now = LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.saveRefItem(existing)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteRefItem(refItemId: String, adminId: String) {
        repository.deleteRefItemById(refItemId)
    }

    @Transactional(readOnly = true)
    override fun getRefItemsByTstCd(searchParam: TestItemRefRequest): Flow<TestItemRefResponse> {
        return repository.findRefItemsByTstCd(searchParam)
    }

    @Transactional(readOnly = true)
    override suspend fun getRefItemsByTstCds(tstCds: List<String>): Flow<TestItemRefItemsResponse> {
        return repository.findRefItemsByTstCds(tstCds)
    }

    // --- TestGene ---
    override fun getGenes(request: TestGeneRequest): Flow<TestGeneResponse> {
        return repository.getGenes(request).map { mapper.toResponse(it) }
    }

    // --- TestItemGene ---

    override suspend fun registerGene(request: TestItemGeneRegisterRequest, adminId: String): TestItemGeneResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = LocalDateTime.now()
        val domain = TestItemGene.create(command, adminId, now)
        val saved = repository.saveGene(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteGene(itemGeneId: String, adminId: String) {
        repository.deleteGeneById(itemGeneId)
    }

    @Transactional(readOnly = true)
    override fun getGenesByTest(tstCd: String): Flow<TestItemGeneResponse> {
        return repository.findGenesByTestCd(tstCd)
    }

    // --- TestItemEssentialDoc ---

    override suspend fun registerEssentialDoc(request: TestItemEssentialDocRegisterRequest, adminId: String): TestItemEssentialDocResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = LocalDateTime.now()
        val domain = TestItemEssentialDoc.create(command, adminId, now)
        val saved = repository.saveEssentialDoc(domain)
        return mapper.toResponse(saved)
    }

    @Transactional(readOnly = true)
    override suspend fun getEssentialDoc(itemEstlDocId: String): TestItemEssentialDocResponse {
        val domain = repository.findEssentialDocById(itemEstlDocId)
            ?: throw IllegalArgumentException("TestItemEssentialDoc not found: $itemEstlDocId")
        return mapper.toResponse(domain)
    }

    override suspend fun updateEssentialDoc(itemEstlDocId: String, request: TestItemEssentialDocUpdateRequest, adminId: String): TestItemEssentialDocResponse {
        val domain = repository.findEssentialDocById(itemEstlDocId)
            ?: throw IllegalArgumentException("TestItemEssentialDoc not found: $itemEstlDocId")
        val command = commandMapper.toUpdateCommand(request)
        val now = LocalDateTime.now()
        domain.update(command)
        val saved = repository.saveEssentialDoc(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteEssentialDoc(itemEstlDocId: String, adminId: String) {
        repository.deleteEssentialDocById(itemEstlDocId)
    }

    @Transactional(readOnly = true)
    override fun getEssentialDocsByTest(tstCd: String): Flow<TestItemEssentialDocListResponse> {
        return repository.findEssentialDocsByTstCd(tstCd)
    }

    @Transactional(readOnly = true)
    override suspend fun getDetailEssentialDocById(itemEstlDocId: String): TestItemEssentialDocDetailResponse? {
        return repository.getDetailEssentialDocById(itemEstlDocId)
    }

    @Transactional(readOnly = true)
    override suspend fun getTestItemHistoryLogList(searchParam: TestItemLogsSearchParam): List<TestItemLogsResponse> {
        val logs = repository.findTestItemHistoryByTstCd(searchParam.tstCd).toList()
        if (logs.isEmpty()) return emptyList()

        val propertyNameMap: Map<String, String> = mapOf(
            "itemHstId" to "검사종목 변경이력 아이디",
            "tstCd" to "검사코드",
            "tstLargeCateCd" to "검사 대분류 코드",
            "tstMediumCateCd" to "검사 중분류 코드",
            "startDt" to "시작일자",
            "endDt" to "종료일자",
            "useYn" to "사용여부",
            "reqPossYn" to "의뢰가능여부",
            "webKorYn" to "편람적용여부(한글)",
            "webEngYn" to "편람적용여부(영문)",
            "tstNm" to "검사명(한글)",
            "tstAbbrNm" to "약어(한글)",
            "tstEngNm" to "검사명(영문)",
            "tstEngAbbrNm" to "약어(영문)",
            "tstIntNm" to "GC명",
            "rstTypeShortYn" to "결과전달 형태 단문",
            "rstTypeLongYn" to "결과전달 형태 장문",
            "rstTypeFileYn" to "결과전달 형태 파일",
            "rstTypeUrlYn" to "결과전달 형태 URL",
            "diseaseCd" to "질병코드",
            "tstMethodCd" to "검사방법코드",
            "refVal" to "참고치(한글)",
            "engRefVal" to "참고치(영문)",
            "clncSgnf" to "임상의의(한글)",
            "engClncSgnf" to "임상의의(영문)",
            "tstDesc" to "설명(한글)",
            "tstEngDesc" to "설명(영문)",
            "tstDayweek" to "검사일",
            "tstTatday" to "검사소요일",
            "insuApplyCd" to "급여구분",
            "insuCd" to "보험코드",
            "insuCateNo" to "보험분류",
            "creator" to "생성자",
            "createDtime" to "생성일시",
            "updater" to "수정자",
            "updateDtime" to "수정일시"
        )
        val propertiesToIgnore = setOf("itemHstId", "tstCd", "updater", "updateDtime", "creator", "createDtime", "isNew", "hstDesc")

        return logs.mapIndexed { index, currentLog ->
            if (index == logs.size - 1) {
                // 가장 오래된 로그 (신규 생성) - 수정항목 및 내용은 빈 값
                mapper.toLogsEditResponse(currentLog, currentLog, "")
            } else {
                // 이전 로그와 비교
                val previousLog = logs[index + 1]
                val diffs = StringBuilder()

                TestItemHst::class.memberProperties.forEach { prop ->
                    prop.isAccessible = true
                    if (prop.name !in propertiesToIgnore) {
                        val getter = prop.getter
                        val oldValue = getter.call(previousLog)
                        val newValue = getter.call(currentLog)
                        if (oldValue != newValue) {
                            val koreanName = propertyNameMap[prop.name] ?: prop.name
                            diffs.append("${koreanName}: '${oldValue ?: ""}' -> '${newValue ?: ""}'\n")
                        }
                    }
                }

                mapper.toLogsEditResponse(previousLog, currentLog, diffs.toString().trimEnd())
            }
        }
    }

    @Transactional(readOnly = true)
    override suspend fun getTestItemSpecimenHistoryLogList(searchParam: TestItemSpecimenLogsSearchParam): List<TestItemSpecimenLogsResponse> {
        val logs = repository.findTestItemSpecimenHistoryByTstCdAndSpcmCd(searchParam.tstCd, searchParam.spcmCd).toList()
//        if (logs.size < 2) return emptyList()
        if(logs.isEmpty()) return emptyList()

        val propertyNameMap: Map<String, String> = mapOf(
            "spcmHstId" to "검체 변경이력 아이디",
            "tstCd" to "검사코드",
            "spcmCd" to "검체코드",
            "sortOrder" to "정렬순서",
            "estlYn" to "필수여부",
            "takeQnty" to "채취량(한글)",
            "engTakeQnty" to "채취량(영문)",
            "useQnty" to "사용량(한글)",
            "engUseQnty" to "사용량(영문)",
            "strgMethod" to "보관방법(한글)",
            "engStrgMethod" to "보관방법(영문)",
            "spcmStbl" to "안정성(한글)",
            "engSpcmStbl" to "안정성(영문)",
            "takeMethod" to "채취방법(한글)",
            "engTakeMethod" to "채취방법(영문)",
            "spcmDesc" to "설명(한글)",
            "engDesc" to "설명(영문)",
            "caution" to "주의사항(한글)",
            "engCaution" to "주의사항(영문)",
            "spcmCntnCd" to "검체용기코드",
            "creator" to "생성자",
            "createDtime" to "생성일시",
            "updater" to "수정자",
            "updateDtime" to "수정일시"
        )
        val propertiesToIgnore = setOf(
            "spcmHstId", "tstCd", "spcmCd",
            "updater", "updateDtime", "creator", "createDtime",
            "isNew", "hstDesc"
        )

        return logs.mapIndexed { index, currentLog ->
            if (index == logs.size - 1) {
                // 가장 오래된 로그 (신규 생성)
                mapper.toSpecimenLogsResponse(currentLog, currentLog, "")
            } else {
                // 이전 로그와 비교
                val previousLog = logs[index + 1]
                val diffs = StringBuilder()

                TestItemSpecimenHst::class.memberProperties.forEach { prop ->
                    prop.isAccessible = true
                    if (prop.name !in propertiesToIgnore) {
                        val getter = prop.getter
                        val oldValue = getter.call(previousLog)
                        val newValue = getter.call(currentLog)
                        if (oldValue != newValue) {
                            val koreanName = propertyNameMap[prop.name] ?: prop.name
                            diffs.append("${koreanName}: '${oldValue ?: ""}' -> '${newValue ?: ""}'\n")
                        }
                    }
                }

                mapper.toSpecimenLogsResponse(previousLog, currentLog, diffs.toString().trimEnd())
            }
        }
}
}
