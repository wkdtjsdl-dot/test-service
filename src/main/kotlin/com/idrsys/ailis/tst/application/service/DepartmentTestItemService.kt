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

    override suspend fun getGroup(deptGroupId: String): DepartmentGroupResponse {
        val domain = repository.findGroupById(deptGroupId) ?: throw RuntimeException("DepartmentGroup not found with id: $deptGroupId")
        return mapper.toResponse(domain)
    }

    override suspend fun updateGroup(deptGroupId: String, request: DepartmentGroupUpdateRequest, adminId: String): DepartmentGroupResponse {
        val existing = repository.findGroupById(deptGroupId) ?: throw RuntimeException("DepartmentGroup not found with id: $deptGroupId")
        val command = commandMapper.toUpdateCommand(request)
        val now = LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.saveGroup(existing)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteGroup(deptGroupId: String, adminId: String) {
        repository.deleteGroupById(deptGroupId)
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

    override suspend fun getGroupItem(deptGrpItmId: String): DepartmentGroupItemResponse {
        val domain = repository.findGroupItemById(deptGrpItmId) ?: throw RuntimeException("DepartmentGroupItem not found with id: $deptGrpItmId")
        return mapper.toResponse(domain)
    }

    override suspend fun updateGroupItem(deptGrpItmId: String, request: DepartmentGroupItemUpdateRequest, adminId: String): DepartmentGroupItemResponse {
        val existing = repository.findGroupItemById(deptGrpItmId) ?: throw RuntimeException("DepartmentGroupItem not found with id: $deptGrpItmId")
        val now = LocalDateTime.now()
        existing.update(request, adminId, now)
        val saved = repository.saveGroupItem(existing)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteGroupItem(deptGrpItmId: String, adminId: String) {
        repository.deleteGroupItemById(deptGrpItmId)
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

    override suspend fun deleteGroupItemTest(deptGrpItmTstId: String, adminId: String) {
        repository.deleteGroupItemTestById(deptGrpItmTstId)
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

    override suspend fun updateTestItem(deptTstItemId: String, request: DepartmentTestItemUpdateRequest, adminId: String): DepartmentTestItemResponse {
        val existing = repository.findTestItemById(deptTstItemId) ?: throw RuntimeException("DepartmentTestItem not found with id: $deptTstItemId")
        val command = commandMapper.toUpdateCommand(request)
        val now = java.time.LocalDateTime.now()
        existing.update(command, adminId, now)
        val saved = repository.saveTestItem(existing)
        return mapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteTestItem(deptTstItemId: String, adminId: String) {
        repository.deleteTestItemById(deptTstItemId)
    }

    override suspend fun getTestItemsByDept(searchParam: DepartmentTestItemSearchParam): Flow<DeptTestItemCategoryResponse> {
        return repository.findTestItemsByDeptCd(searchParam)
    }
}
