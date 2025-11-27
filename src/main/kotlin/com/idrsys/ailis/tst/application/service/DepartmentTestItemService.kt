package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.DepartmentTestItemCommandMapper
import com.idrsys.ailis.tst.application.mapper.DepartmentTestItemMapper
import com.idrsys.ailis.tst.application.required.DepartmentTestItemRepository
import com.idrsys.ailis.tst.application.usecase.DepartmentTestItemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

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
        val now = java.time.LocalDateTime.now()
        val domain = com.idrsys.ailis.tst.domain.model.DepartmentGroup(
            deptGroupId = UUID.randomUUID().toString(),
            deptCd = command.deptCd,
            tstCateCd = command.tstCateCd,
            tstCateNm = command.tstCateNm,
            updateAuthCd = command.updateAuthCd,
            dupAllowYn = command.dupAllowYn,
            sortOrder = command.sortOrder,
            creator = adminId,
            createDtime = now,
            updater = adminId,
            updateDtime = now
        )
        domain.setAsNew()
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
        val now = java.time.LocalDateTime.now()
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
        val domain = mapper.toDomain(request)
        val now = java.time.LocalDateTime.now()
        val entityWithId = com.idrsys.ailis.tst.domain.model.DepartmentGroupItem(
            deptGrpItmId = UUID.randomUUID().toString(),
            deptCd = domain.deptCd,
            tstCateCd = domain.tstCateCd,
            tstCateItemCd = domain.tstCateItemCd,
            tstCateItemNm = domain.tstCateItemNm,
            sortOrder = domain.sortOrder,
            creator = adminId,
            createDtime = now,
            updater = adminId,
            updateDtime = now
        )
        entityWithId.setAsNew()
        val saved = repository.saveGroupItem(entityWithId)
        return mapper.toResponse(saved)
    }

    override suspend fun getGroupItem(id: String): DepartmentGroupItemResponse {
        val domain = repository.findGroupItemById(id) ?: throw RuntimeException("DepartmentGroupItem not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun updateGroupItem(id: String, request: DepartmentGroupItemUpdateRequest, adminId: String): DepartmentGroupItemResponse {
        val existing = repository.findGroupItemById(id) ?: throw RuntimeException("DepartmentGroupItem not found with id: $id")
        val now = java.time.LocalDateTime.now()
        val updated = com.idrsys.ailis.tst.domain.model.DepartmentGroupItem(
            deptGrpItmId = existing.deptGrpItmId,
            deptCd = request.deptCd,
            tstCateCd = request.tstCateCd,
            tstCateItemCd = request.tstCateItemCd,
            tstCateItemNm = request.tstCateItemNm,
            sortOrder = request.sortOrder,
            creator = existing.creator,
            createDtime = existing.createDtime,
            updater = adminId,
            updateDtime = now
        )
        val saved = repository.saveGroupItem(updated)
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
        val domain = mapper.toDomain(request)
        val now = java.time.LocalDateTime.now()
        val entityWithId = com.idrsys.ailis.tst.domain.model.DepartmentGroupItemTest(
            deptGrpItmTstId = UUID.randomUUID().toString(),
            deptCd = domain.deptCd,
            tstCateCd = domain.tstCateCd,
            tstCateItemCd = domain.tstCateItemCd,
            tstCd = domain.tstCd,
            creator = adminId,
            createDtime = now
        )
        entityWithId.setAsNew()
        val saved = repository.saveGroupItemTest(entityWithId)
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
        val now = java.time.LocalDateTime.now()
        val domain = com.idrsys.ailis.tst.domain.model.DepartmentTestItem(
            deptTstItemId = UUID.randomUUID().toString(),
            deptCd = command.deptCd,
            tstCd = command.tstCd,
            tstNm = command.tstNm,
            tstAbbrNm = command.tstAbbrNm,
            tstEngNm = command.tstEngNm,
            tstEngAbbrNm = command.tstEngAbbrNm,
            sortOrder = command.sortOrder,
            useYn = command.useYn,
            creator = adminId,
            createDtime = now,
            updater = adminId,
            updateDtime = now
        )
        domain.setAsNew()
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
        val testItem = repository.findTestItemById(id)
            ?: throw NoSuchElementException("DepartmentTestItem not found: $id")

        val now = java.time.LocalDateTime.now()
        testItem.delete(updater = adminId, updateDtime = now)

        repository.saveTestItem(testItem)
    }

    override suspend fun getTestItemsByDept(deptCd: String): Flow<DepartmentTestItemResponse> {
        return repository.findTestItemsByDeptCd(deptCd).map { mapper.toResponse(it) }
    }
}
