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

    override suspend fun getReference(id: String): TestReferenceResponse {
        val domain = repository.findById(id) ?: throw RuntimeException("TestReference not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun updateReference(id: String, request: TestReferenceUpdateRequest, adminId: String): TestReferenceResponse {
        val existing = repository.findById(id) ?: throw RuntimeException("TestReference not found with id: $id")
        val command = commandMapper.toUpdateCommand(request)
        val now = java.time.LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.save(existing)
        return mapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteReference(id: String, adminId: String) {
        val reference = repository.findById(id)
            ?: throw NoSuchElementException("TestReference not found: $id")

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

    override suspend fun getGroup(id: String): TestReferenceGroupResponse {
        val domain = repository.findGroupById(id) ?: throw RuntimeException("TestReferenceGroup not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun updateGroup(id: String, request: TestReferenceGroupUpdateRequest, adminId: String): TestReferenceGroupResponse {
        val existing = repository.findGroupById(id) ?: throw RuntimeException("TestReferenceGroup not found with id: $id")
        val command = commandMapper.toUpdateCommand(request)
        val now = java.time.LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.saveGroup(existing)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteGroup(id: String, adminId: String) {
        repository.deleteGroupById(id)
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

    override suspend fun getGroupItem(id: String): TestReferenceGroupItemResponse {
        val domain = repository.findGroupItemById(id) ?: throw RuntimeException("TestReferenceGroupItem not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun updateGroupItem(id: String, request: TestReferenceGroupItemUpdateRequest, adminId: String): TestReferenceGroupItemResponse {
        val existing = repository.findGroupItemById(id) ?: throw RuntimeException("TestReferenceGroupItem not found with id: $id")
        val command = commandMapper.toUpdateCommand(request)
        val now = java.time.LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.saveGroupItem(existing)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteGroupItem(id: String, adminId: String) {
        repository.deleteGroupItemById(id)
    }

    override suspend fun getGroupItemsByGroup(groupCd: String): Flow<TestReferenceGroupItemResponse> {
        return repository.findGroupItemsByGroupCd(groupCd).map { mapper.toResponse(it) }
    }
}
