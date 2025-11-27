package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.TestReferenceMapper
import com.idrsys.ailis.tst.application.required.TestReferenceRepository
import com.idrsys.ailis.tst.application.usecase.TestReferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class TestReferenceService(
    private val repository: TestReferenceRepository,
    private val mapper: TestReferenceMapper
) : TestReferenceUseCase {

    // --- TestReference ---

    override suspend fun registerReference(request: TestReferenceRegisterRequest, adminId: String): TestReferenceResponse {
        val domain = mapper.toDomain(request)
        // ID generation or handling if needed, assuming UUID for now or DB generated if configured
        // But since we are using manual ID assignment in other services, let's generate UUID here if not present
        // However, looking at other services, we might need to set ID manually.
        // Let's use UUID for now as per previous patterns.
        val entityWithId = com.idrsys.ailis.tst.domain.model.TestReference(
            refCd = UUID.randomUUID().toString(),
            refCateCd = domain.refCateCd,
            useYn = domain.useYn,
            refNm = domain.refNm,
            refAbbrNm = domain.refAbbrNm,
            refEngNm = domain.refEngNm,
            refEngAbbrNm = domain.refEngAbbrNm,
            sortOrder = domain.sortOrder,
            refType = domain.refType,
            refSize = domain.refSize,
            rangeChkYn = domain.rangeChkYn,
            refMinVal = domain.refMinVal,
            refMaxVal = domain.refMaxVal,
            dataFormat = domain.dataFormat,
            dftData = domain.dftData,
            dftEngData = domain.dftEngData,
            creator = adminId, // TODO: Get from context
            createDtime = java.time.LocalDateTime.now(),
            updater = adminId,
            updateDetime = java.time.LocalDateTime.now()
        )
        entityWithId.setAsNew()
        val saved = repository.save(entityWithId)
        return mapper.toResponse(saved)
    }

    override suspend fun getReference(id: String): TestReferenceResponse {
        val domain = repository.findById(id) ?: throw RuntimeException("TestReference not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun updateReference(id: String, request: TestReferenceUpdateRequest, adminId: String): TestReferenceResponse {
        val existing = repository.findById(id) ?: throw RuntimeException("TestReference not found with id: $id")
        // Update logic - creating new instance with updated fields
        // Since domain models are immutable-ish (val constructor params), we might need a copy or manual update
        // But wait, the domain models have vars for properties and an update method?
        // Let's check TestReference.kt... It doesn't have an update method yet.
        // I should have added update method in domain model refactoring step.
        // For now, I will construct a new object with updated values and same ID.
        
        val updated = com.idrsys.ailis.tst.domain.model.TestReference(
            refCd = existing.refCd,
            refCateCd = request.refCateCd,
            useYn = request.useYn,
            refNm = request.refNm,
            refAbbrNm = request.refAbbrNm,
            refEngNm = request.refEngNm,
            refEngAbbrNm = request.refEngAbbrNm,
            sortOrder = request.sortOrder,
            refType = request.refType,
            refSize = request.refSize,
            rangeChkYn = request.rangeChkYn,
            refMinVal = request.refMinVal,
            refMaxVal = request.refMaxVal,
            dataFormat = request.dataFormat,
            dftData = request.dftData,
            dftEngData = request.dftEngData,
            creator = existing.creator,
            createDtime = existing.createDtime,
            updater = adminId, // TODO: context
            updateDetime = java.time.LocalDateTime.now()
        )
        // Not setting as new, so it will be an update
        val saved = repository.save(updated)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteReference(id: String, adminId: String) {
        repository.deleteById(id)
    }

    override suspend fun getAllReferences(): Flow<TestReferenceResponse> {
        return repository.findAll().map { mapper.toResponse(it) }
    }

    // --- TestReferenceGroup ---

    override suspend fun registerGroup(request: TestReferenceGroupRegisterRequest, adminId: String): TestReferenceGroupResponse {
        val domain = mapper.toDomain(request)
        val entityWithId = com.idrsys.ailis.tst.domain.model.TestReferenceGroup(
            refGroupCd = UUID.randomUUID().toString(),
            refNm = domain.refNm,
            refAbbrNm = domain.refAbbrNm,
            refEngNm = domain.refEngNm,
            refEngAbbrNm = domain.refEngAbbrNm,
            sortOrder = domain.sortOrder,
            creator = adminId,
            createDtime = java.time.LocalDateTime.now(),
            updater = adminId,
            updateDetime = java.time.LocalDateTime.now()
        )
        entityWithId.setAsNew()
        val saved = repository.saveGroup(entityWithId)
        return mapper.toResponse(saved)
    }

    override suspend fun getGroup(id: String): TestReferenceGroupResponse {
        val domain = repository.findGroupById(id) ?: throw RuntimeException("TestReferenceGroup not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun updateGroup(id: String, request: TestReferenceGroupUpdateRequest, adminId: String): TestReferenceGroupResponse {
        val existing = repository.findGroupById(id) ?: throw RuntimeException("TestReferenceGroup not found with id: $id")
        val updated = com.idrsys.ailis.tst.domain.model.TestReferenceGroup(
            refGroupCd = existing.refGroupCd,
            refNm = request.refNm,
            refAbbrNm = request.refAbbrNm,
            refEngNm = request.refEngNm,
            refEngAbbrNm = request.refEngAbbrNm,
            sortOrder = request.sortOrder,
            creator = existing.creator,
            createDtime = existing.createDtime,
            updater = adminId,
            updateDetime = java.time.LocalDateTime.now()
        )
        val saved = repository.saveGroup(updated)
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
        val domain = mapper.toDomain(request)
        val entityWithId = com.idrsys.ailis.tst.domain.model.TestReferenceGroupItem(
            tstRefGroupItemId = UUID.randomUUID().toString(),
            refGroupCd = domain.refGroupCd,
            refCd = domain.refCd,
            sortOrder = domain.sortOrder,
            creator = adminId,
            createDtime = java.time.LocalDateTime.now(),
            updater = adminId,
            updateDetime = java.time.LocalDateTime.now()
        )
        entityWithId.setAsNew()
        val saved = repository.saveGroupItem(entityWithId)
        return mapper.toResponse(saved)
    }

    override suspend fun getGroupItem(id: String): TestReferenceGroupItemResponse {
        val domain = repository.findGroupItemById(id) ?: throw RuntimeException("TestReferenceGroupItem not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun updateGroupItem(id: String, request: TestReferenceGroupItemUpdateRequest, adminId: String): TestReferenceGroupItemResponse {
        val existing = repository.findGroupItemById(id) ?: throw RuntimeException("TestReferenceGroupItem not found with id: $id")
        val updated = com.idrsys.ailis.tst.domain.model.TestReferenceGroupItem(
            tstRefGroupItemId = existing.tstRefGroupItemId,
            refGroupCd = request.refGroupCd,
            refCd = request.refCd,
            sortOrder = request.sortOrder,
            creator = existing.creator,
            createDtime = existing.createDtime,
            updater = adminId,
            updateDetime = java.time.LocalDateTime.now()
        )
        val saved = repository.saveGroupItem(updated)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteGroupItem(id: String, adminId: String) {
        repository.deleteGroupItemById(id)
    }

    override suspend fun getGroupItemsByGroup(groupCd: String): Flow<TestReferenceGroupItemResponse> {
        return repository.findGroupItemsByGroupCd(groupCd).map { mapper.toResponse(it) }
    }
}
