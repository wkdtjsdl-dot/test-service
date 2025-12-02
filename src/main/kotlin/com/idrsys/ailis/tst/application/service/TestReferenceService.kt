package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.TestReferenceCommandMapper
import com.idrsys.ailis.tst.application.mapper.TestReferenceMapper
import com.idrsys.ailis.tst.application.required.TestReferenceRepository
import com.idrsys.ailis.tst.application.usecase.TestReferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.idrsys.ailis.tst.domain.model.TestReference
import com.idrsys.ailis.tst.domain.model.TestReferenceGroup
import com.idrsys.ailis.tst.domain.model.TestReferenceGroupItem
import java.util.*

@Service
@Transactional
class TestReferenceService(
    private val repository: TestReferenceRepository,
    private val mapper: TestReferenceMapper,
    private val commandMapper: TestReferenceCommandMapper
) : TestReferenceUseCase {

    // --- TestReference ---

    override suspend fun registerReference(request: TestReferenceRegisterRequest, adminId: String): TestReferenceResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = java.time.LocalDateTime.now()
        val domain = TestReference.create(command, adminId, now)
        val saved = repository.save(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun getReference(refCd: String): TestReferenceResponse {
        val domain = repository.findById(refCd) ?: throw RuntimeException("TestReference not found with id: $refCd")
        return mapper.toResponse(domain)
    }

    override suspend fun updateReference(refCd: String, request: TestReferenceUpdateRequest, adminId: String): TestReferenceResponse {
        val existing = repository.findById(refCd) ?: throw RuntimeException("TestReference not found with id: $refCd")
        val command = commandMapper.toUpdateCommand(request)
        val now = java.time.LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.save(existing)
        return mapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteReference(refCd: String, adminId: String) {
        val reference = repository.findById(refCd)
            ?: throw NoSuchElementException("TestReference not found: $refCd")

        val now = java.time.LocalDateTime.now()
        reference.delete(updater = adminId, updateDetime = now)

        repository.save(reference)
    }

    override suspend fun getAllReferences(): Flow<TestReferenceResponse> {
        return repository.findAll().map { mapper.toResponse(it) }
    }

    // --- TestReferenceGroup ---

    override suspend fun registerGroup(request: TestReferenceGroupRegisterRequest, adminId: String): TestReferenceGroupResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = java.time.LocalDateTime.now()
        val domain = TestReferenceGroup.create(command, adminId, now)
        val saved = repository.saveGroup(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun getGroup(refGroupCd: String): TestReferenceGroupResponse {
        val domain = repository.findGroupById(refGroupCd) ?: throw RuntimeException("TestReferenceGroup not found with id: $refGroupCd")
        return mapper.toResponse(domain)
    }

    override suspend fun updateGroup(refGroupCd: String, request: TestReferenceGroupUpdateRequest, adminId: String): TestReferenceGroupResponse {
        val existing = repository.findGroupById(refGroupCd) ?: throw RuntimeException("TestReferenceGroup not found with id: $refGroupCd")
        val command = commandMapper.toUpdateCommand(request)
        val now = java.time.LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.saveGroup(existing)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteGroup(refGroupCd: String, adminId: String) {
        repository.deleteGroupById(refGroupCd)
    }

    override suspend fun getAllGroups(): Flow<TestReferenceGroupResponse> {
        return repository.findAllGroups().map { mapper.toResponse(it) }
    }

    // --- TestReferenceGroupItem ---

    override suspend fun registerGroupItem(request: TestReferenceGroupItemRegisterRequest, adminId: String): TestReferenceGroupItemResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = java.time.LocalDateTime.now()
        val domain = TestReferenceGroupItem.create(command, adminId, now)
        val saved = repository.saveGroupItem(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun getGroupItem(tstRefGroupItemId: String): TestReferenceGroupItemResponse {
        val domain = repository.findGroupItemById(tstRefGroupItemId) ?: throw RuntimeException("TestReferenceGroupItem not found with id: $tstRefGroupItemId")
        return mapper.toResponse(domain)
    }

    override suspend fun updateGroupItem(tstRefGroupItemId: String, request: TestReferenceGroupItemUpdateRequest, adminId: String): TestReferenceGroupItemResponse {
        val existing = repository.findGroupItemById(tstRefGroupItemId) ?: throw RuntimeException("TestReferenceGroupItem not found with id: $tstRefGroupItemId")
        val command = commandMapper.toUpdateCommand(request)
        val now = java.time.LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.saveGroupItem(existing)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteGroupItem(tstRefGroupItemId: String, adminId: String) {
        repository.deleteGroupItemById(tstRefGroupItemId)
    }

    override fun getGroupItemsByGroup(refGroupCd: String): Flow<TestReferenceGroupItemResponse> {
        return repository.findGroupItemsByGroupCd(refGroupCd).map { mapper.toResponse(it) }
    }
}
