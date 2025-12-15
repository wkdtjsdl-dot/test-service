package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.TestItemCommandMapper
import com.idrsys.ailis.tst.application.mapper.TestItemMapper
import com.idrsys.ailis.tst.application.required.TestItemRepository
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
import kotlinx.coroutines.flow.toList
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
        val now = java.time.LocalDateTime.now()
        val domain = TestItem.create(command, adminId, now)
        val saved = repository.save(domain)
        return mapper.toResponse(saved)
    }

    @Transactional(readOnly = true)
    override suspend fun getItem(tstCd: String): TestItemResponse {
        val domain = repository.findById(tstCd) ?: throw RuntimeException("TestItem not found with id: $tstCd")
        return mapper.toResponse(domain)
    }

    override suspend fun updateItem(tstCd: String, request: TestItemUpdateRequest, adminId: String): TestItemResponse {
        val existing = repository.findById(tstCd) ?: throw RuntimeException("TestItem not found with id: $tstCd")

        // Save history
        val hist = mapper.toDomain(existing, request.updateReason ?: "").apply { setAsNew() }
        repository.saveTestItemHistory(hist)

        val command = commandMapper.toUpdateCommand(request)
        val now = java.time.LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.save(existing)

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

    // --- StandardCharge ---

    override suspend fun registerCharge(request: StandardChargeRegisterRequest, adminId: String): StandardChargeResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = java.time.LocalDateTime.now()
        val domain = StandardCharge.create(command, adminId, now)
        val saved = repository.saveCharge(domain)
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
        val now = java.time.LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.saveCharge(existing)
        return mapper.toResponse(saved)
    }

    @Transactional(readOnly = true)
    override suspend fun getChargesByTest(tstCd: String): Flow<StandardChargeResponse> {
        return repository.findChargesByTestCd(tstCd).map { mapper.toResponse(it) }
    }

    // --- TestItemSpecimen ---

    override suspend fun registerSpecimen(request: TestItemSpecimenRegisterRequest, adminId: String): TestItemSpecimenResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = java.time.LocalDateTime.now()
        val domain = TestItemSpecimen.create(command, adminId, now)
        val saved = repository.saveSpecimen(domain)
        return mapper.toResponse(saved)
    }

    @Transactional(readOnly = true)
    override suspend fun getSpecimen(spcmId: String): TestItemSpecimenResponse {
        val domain = repository.findSpecimenById(spcmId) ?: throw RuntimeException("TestItemSpecimen not found with id: $spcmId")
        return mapper.toResponse(domain)
    }

    override suspend fun deleteSpecimen(id: String, adminId: String) {
        repository.deleteSpecimenById(id)
    }

    @Transactional(readOnly = true)
    override fun getSpecimensByTest(tstCd: String): Flow<TestItemSpecimenResponse> {
        return repository.findSpecimensByTestCd(tstCd).map { mapper.toResponse(it) }
    }

    // --- TestItemRefItem ---

    override suspend fun registerRefItem(request: TestItemRefItemRegisterRequest, adminId: String): TestItemRefItemResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = java.time.LocalDateTime.now()
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
        val now = java.time.LocalDateTime.now()
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

    // --- TestGene ---
    override fun getGenes(request: TestGeneRequest): Flow<TestGeneResponse> {
        return repository.getGenes(request).map { mapper.toResponse(it) }
    }

    // --- TestItemGene ---

    override suspend fun registerGene(request: TestItemGeneRegisterRequest, adminId: String): TestItemGeneResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = java.time.LocalDateTime.now()
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
        val now = java.time.LocalDateTime.now()
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
        val now = java.time.LocalDateTime.now()
        domain.update(command, adminId, now)
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
        if (logs.size < 2) return emptyList()

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

        return logs.windowed(size = 2, step = 1).map { (newLog, oldLog) ->
            val diffs = StringBuilder()
            val propertiesToIgnore = setOf("itemHstId", "tstCd", "updater", "updateDtime", "creator", "createDtime", "isNew", "hstDesc")

            TestItemHst::class.memberProperties.forEach { prop ->
                prop.isAccessible = true // private backing field 강제 오픈
                if (prop.name !in propertiesToIgnore) {
                    val getter = prop.getter
                    val oldValue = getter.call(oldLog)
                    val newValue = getter.call(newLog)
                    if (oldValue != newValue) {
                        val koreanName = propertyNameMap[prop.name] ?: prop.name
                        diffs.append("${koreanName}: '${oldValue ?: ""}' -> '${newValue ?: ""}'\n")
                    }
                }
            }

            val diffString = diffs.toString().trimEnd()
            mapper.toLogsEditResponse(oldLog, newLog, diffString)
        }
    }
}
