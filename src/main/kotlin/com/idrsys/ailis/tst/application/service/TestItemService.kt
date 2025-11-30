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
import com.idrsys.ailis.tst.domain.model.TestItemSpecimen
import java.util.*

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

    override suspend fun getItem(id: String): TestItemResponse {
        val domain = repository.findById(id) ?: throw RuntimeException("TestItem not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun updateItem(id: String, request: TestItemUpdateRequest, adminId: String): TestItemResponse {
        val existing = repository.findById(id) ?: throw RuntimeException("TestItem not found with id: $id")
        val command = commandMapper.toUpdateCommand(request)
        val now = java.time.LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.save(existing)
        return mapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteItem(id: String, adminId: String) {
        val item = repository.findById(id)
            ?: throw NoSuchElementException("TestItem not found: $id")

        val now = java.time.LocalDateTime.now()
        item.delete(updater = adminId, updateDetime = now)

        repository.save(item)
    }

    override suspend fun getAllItems(): Flow<TestItemResponse> {
        return repository.findAll().map { mapper.toResponse(it) }
    }

    override suspend fun getItemsByLargeCate(code: String): Flow<TestItemResponse> {
        return repository.findByLargeCateCd(code).map { mapper.toResponse(it) }
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

    override suspend fun getSpecimen(id: String): TestItemSpecimenResponse {
        val domain = repository.findSpecimenById(id) ?: throw RuntimeException("TestItemSpecimen not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun deleteSpecimen(id: String, adminId: String) {
        repository.deleteSpecimenById(id)
    }

    override suspend fun getSpecimensByTest(tstCd: String): Flow<TestItemSpecimenResponse> {
        return repository.findSpecimensByTestCd(tstCd).map { mapper.toResponse(it) }
    }
}
