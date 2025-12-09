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

@Service
@Transactional
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

    override suspend fun getItem(tstCd: String): TestItemResponse {
        val domain = repository.findById(tstCd) ?: throw RuntimeException("TestItem not found with id: $tstCd")
        return mapper.toResponse(domain)
    }

    override suspend fun updateItem(tstCd: String, request: TestItemUpdateRequest, adminId: String): TestItemResponse {
        val existing = repository.findById(tstCd) ?: throw RuntimeException("TestItem not found with id: $tstCd")
        val command = commandMapper.toUpdateCommand(request)
        val now = java.time.LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.save(existing)
        return mapper.toResponse(saved)
    }

    override fun getItems(searchParam: TestItemSearchParam): Flow<TestItemResponse> {
        return repository.getItems(searchParam).map { mapper.toResponse(it) }
    }

    override fun autoCompleteItems(searchParam: TestItemAutoCompleteParam): Flow<TestItemAutoCompleteResponse> {
        return repository.autoCompleteItems(searchParam)
    }

    // --- StandardCharge ---

    override suspend fun registerCharge(request: StandardChargeRegisterRequest, adminId: String): StandardChargeResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = java.time.LocalDateTime.now()
        val domain = StandardCharge.create(command, adminId, now)
        val saved = repository.saveCharge(domain)
        return mapper.toResponse(saved)
    }

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

    override suspend fun getSpecimen(spcmId: String): TestItemSpecimenResponse {
        val domain = repository.findSpecimenById(spcmId) ?: throw RuntimeException("TestItemSpecimen not found with id: $spcmId")
        return mapper.toResponse(domain)
    }

    override suspend fun deleteSpecimen(id: String, adminId: String) {
        repository.deleteSpecimenById(id)
    }

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

    override suspend fun getRefItem(refItemId: String): TestItemRefItemResponse {
        val domain = repository.findRefItemById(refItemId) ?: throw RuntimeException("TestItemRefItem not found with id: $refItemId")
        return mapper.toResponse(domain)
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

    override fun getRefItemsByTstCd(searchParam: TestItemRefRequest): Flow<TestItemRefResponse> {
        return repository.findRefItemsByTstCd(searchParam)
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

    override fun getGenesByTest(tstCd: String): Flow<TestItemGeneResponse> {
        return repository.findGenesByTestCd(tstCd).map { mapper.toResponse(it) }
    }

    // --- TestItemEssentialDoc ---

    override suspend fun registerEssentialDoc(request: TestItemEssentialDocRegisterRequest, adminId: String): TestItemEssentialDocResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = java.time.LocalDateTime.now()
        val domain = TestItemEssentialDoc.create(command, adminId, now)
        val saved = repository.saveEssentialDoc(domain)
        return mapper.toResponse(saved)
    }

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

    override fun getEssentialDocsByTest(tstCd: String): Flow<TestItemEssentialDocResponse> {
        return repository.findEssentialDocsByTstCd(tstCd).map { mapper.toResponse(it) }
    }
}
