package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.dto.request.DepartmentTestItemSearchParam
import com.idrsys.ailis.tst.application.mapper.DepartmentTestItemCommandMapper
import com.idrsys.ailis.tst.application.mapper.DepartmentTestItemMapper
import com.idrsys.ailis.tst.application.required.DepartmentTestItemRepository
import com.idrsys.ailis.tst.application.usecase.DepartmentTestItemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.idrsys.ailis.tst.domain.model.DepartmentGroup
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItem
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItemTest
import com.idrsys.ailis.tst.domain.model.DepartmentTestItem
import java.time.LocalDateTime

@Service
@Transactional
class DepartmentTestItemService(
    private val repository: DepartmentTestItemRepository,
    private val mapper: DepartmentTestItemMapper,
    private val commandMapper: DepartmentTestItemCommandMapper
) : DepartmentTestItemUseCase {

    // --- DepartmentGroup ---

    override suspend fun registerGroup(request: DepartmentGroupRegisterRequest, adminId: String): DepartmentGroupResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = LocalDateTime.now()
        val domain = DepartmentGroup.create(command, adminId, now)
        val saved = repository.saveGroup(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun getGroup(id: String): DepartmentGroupResponse {
        val domain = repository.findGroupById(id) ?: throw RuntimeException("DepartmentGroup not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun updateGroup(id: String, request: DepartmentGroupUpdateRequest, adminId: String): DepartmentGroupResponse {
        val existing = repository.findGroupById(id) ?: throw RuntimeException("DepartmentGroup not found with id: $id")
        val command = commandMapper.toUpdateCommand(request)
        val now = LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.saveGroup(existing)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteGroup(id: String, adminId: String) {
        repository.deleteGroupById(id)
    }

    override suspend fun getAllGroups(): Flow<DepartmentGroupResponse> {
        return repository.findAllGroups().map { mapper.toResponse(it) }
    }

    // --- DepartmentGroupItem ---

    override suspend fun registerGroupItem(request: DepartmentGroupItemRegisterRequest, adminId: String): DepartmentGroupItemResponse {
        val now = LocalDateTime.now()
        val domain = mapper.toDomain(request, adminId, now)
        domain.setAsNew()
        val saved = repository.saveGroupItem(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun getGroupItem(id: String): DepartmentGroupItemResponse {
        val domain = repository.findGroupItemById(id) ?: throw RuntimeException("DepartmentGroupItem not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun updateGroupItem(id: String, request: DepartmentGroupItemUpdateRequest, adminId: String): DepartmentGroupItemResponse {
        val existing = repository.findGroupItemById(id) ?: throw RuntimeException("DepartmentGroupItem not found with id: $id")
        val now = LocalDateTime.now()
        existing.update(request, adminId, now)
        val saved = repository.saveGroupItem(existing)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteGroupItem(id: String, adminId: String) {
        repository.deleteGroupItemById(id)
    }

    override suspend fun getGroupItemsByDept(deptCd: String): Flow<DepartmentGroupItemResponse> {
        return repository.findGroupItemsByDeptCd(deptCd).map { mapper.toResponse(it) }
    }

    // --- DepartmentGroupItemTest ---

    override suspend fun registerGroupItemTest(request: DepartmentGroupItemTestRegisterRequest, adminId: String): DepartmentGroupItemTestResponse {
        val now = LocalDateTime.now()
        val domain = mapper.toDomain(request, adminId, now)
        domain.setAsNew()
        val saved = repository.saveGroupItemTest(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun getGroupItemTest(id: String): DepartmentGroupItemTestResponse {
        val domain = repository.findGroupItemTestById(id) ?: throw RuntimeException("DepartmentGroupItemTest not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun deleteGroupItemTest(id: String, adminId: String) {
        repository.deleteGroupItemTestById(id)
    }

    override suspend fun getGroupItemTestsByDept(deptCd: String): Flow<DepartmentGroupItemTestResponse> {
        return repository.findGroupItemTestsByDeptCd(deptCd).map { mapper.toResponse(it) }
    }

    // --- DepartmentTestItem ---

    override suspend fun registerTestItem(request: DepartmentTestItemRegisterRequest, adminId: String): DepartmentTestItemResponse {
        val command = commandMapper.toCreateCommand(request)
        val now = LocalDateTime.now()
        val domain = DepartmentTestItem.create(command, adminId, now)
        val saved = repository.saveTestItem(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun getTestItem(id: String): DepartmentTestItemResponse {
        val domain = repository.findTestItemById(id) ?: throw RuntimeException("DepartmentTestItem not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun updateTestItem(id: String, request: DepartmentTestItemUpdateRequest, adminId: String): DepartmentTestItemResponse {
        val existing = repository.findTestItemById(id) ?: throw RuntimeException("DepartmentTestItem not found with id: $id")
        val command = commandMapper.toUpdateCommand(request)
        val now = java.time.LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.saveTestItem(existing)
        return mapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteTestItem(id: String, adminId: String) {
        repository.deleteTestItemById(id)
    }

    override suspend fun getTestItemsByDept(searchParam: DepartmentTestItemSearchParam): Flow<DepartmentTestItemResponse> {
        return repository.findTestItemsByDeptCd(searchParam).map { mapper.toResponse(it) }
    }
}
