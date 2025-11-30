package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.DepartmentTestItemCommandMapper
import com.idrsys.ailis.tst.application.mapper.DepartmentTestItemMapper
import com.idrsys.ailis.tst.application.required.DepartmentTestItemRepository
import com.idrsys.ailis.tst.domain.command.DepartmentGroupCreateCommand
import com.idrsys.ailis.tst.domain.command.DepartmentGroupUpdateCommand
import com.idrsys.ailis.tst.domain.model.DepartmentGroup
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class DepartmentTestItemServiceTest {

    @Mock
    lateinit var repository: DepartmentTestItemRepository

    @Mock
    lateinit var mapper: DepartmentTestItemMapper

    @Mock
    lateinit var commandMapper: DepartmentTestItemCommandMapper

    @InjectMocks
    lateinit var service: DepartmentTestItemService

    // --- DepartmentGroup Tests ---

    @Test
    fun `registerGroup should save and return response`() = runTest {
        // Given
        val request = DepartmentGroupRegisterRequest(
            deptCd = "DEPT01",
            tstCateCd = "CATE01",
            tstCateNm = "Category Name",
            updateAuthCd = "AUTH01",
            dupAllowYn = true,
            sortOrder = 1
        )
        val domain = DepartmentGroup(
            deptGroupId = "uuid",
            deptCd = "DEPT01",
            tstCateCd = "CATE01",
            tstCateNm = "Category Name",
            updateAuthCd = "AUTH01",
            dupAllowYn = true,
            sortOrder = 1,
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = DepartmentGroupResponse(
            deptGroupId = "uuid",
            deptCd = "DEPT01",
            tstCateCd = "CATE01",
            tstCateNm = "Category Name",
            updateAuthCd = "AUTH01",
            dupAllowYn = true,
            sortOrder = 1,
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        val command = DepartmentGroupCreateCommand(
            deptCd = "DEPT01",
            tstCateCd = "CATE01",
            tstCateNm = "Category Name",
            updateAuthCd = "AUTH01",
            dupAllowYn = true,
            sortOrder = 1
        )

        `when`(commandMapper.toCreateCommand(request)).thenReturn(command)
        `when`(repository.saveGroup(any())).thenReturn(domain)
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.registerGroup(request, "admin")

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `updateGroup should update and return response`() = runTest {
        // Given
        val id = "uuid"
        val request = DepartmentGroupUpdateRequest(
            deptCd = "DEPT02",
            tstCateCd = "CATE02",
            tstCateNm = "Updated Name",
            updateAuthCd = "AUTH02",
            dupAllowYn = false,
            sortOrder = 2
        )
        val existing = DepartmentGroup(
            deptGroupId = id,
            deptCd = "DEPT01",
            tstCateCd = "CATE01",
            tstCateNm = "Category Name",
            updateAuthCd = "AUTH01",
            dupAllowYn = true,
            sortOrder = 1,
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = DepartmentGroupResponse(
            deptGroupId = id,
            deptCd = "DEPT02",
            tstCateCd = "CATE02",
            tstCateNm = "Updated Name",
            updateAuthCd = "AUTH02",
            dupAllowYn = false,
            sortOrder = 2,
            creator = "admin",
            createDtime = existing.createDtime,
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        val command = DepartmentGroupUpdateCommand(
            deptCd = "DEPT02",
            tstCateCd = "CATE02",
            tstCateNm = "Updated Name",
            updateAuthCd = "AUTH02",
            dupAllowYn = false,
            sortOrder = 2
        )

        `when`(repository.findGroupById(id)).thenReturn(existing)
        `when`(commandMapper.toUpdateCommand(request)).thenReturn(command)
        `when`(repository.saveGroup(any())).thenReturn(existing)
        `when`(mapper.toResponse(existing)).thenReturn(response)

        // When
        val result = service.updateGroup(id, request, "admin")

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `getGroup should return response`() = runTest {
        // Given
        val id = "uuid"
        val domain = DepartmentGroup(
            deptGroupId = id,
            deptCd = "DEPT01",
            tstCateCd = "CATE01",
            tstCateNm = "Category Name",
            updateAuthCd = "AUTH01",
            dupAllowYn = true,
            sortOrder = 1,
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = DepartmentGroupResponse(
            deptGroupId = id,
            deptCd = "DEPT01",
            tstCateCd = "CATE01",
            tstCateNm = "Category Name",
            updateAuthCd = "AUTH01",
            dupAllowYn = true,
            sortOrder = 1,
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        `when`(repository.findGroupById(id)).thenReturn(domain)
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.getGroup(id)

        // Then
        assertEquals(response, result)
    }

    @Test
    fun `deleteGroup should call repository delete`() = runTest {
        // Given
        val id = "uuid"

        // When
        service.deleteGroup(id, "admin")

        // Then
        Mockito.verify(repository).deleteGroupById(id)
    }

    @Test
    fun `getAllGroups should return flow of responses`() = runTest {
        // Given
        val domain = DepartmentGroup(
            deptGroupId = "uuid",
            deptCd = "DEPT01",
            tstCateCd = "CATE01",
            tstCateNm = "Category Name",
            updateAuthCd = "AUTH01",
            dupAllowYn = true,
            sortOrder = 1,
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = DepartmentGroupResponse(
            deptGroupId = "uuid",
            deptCd = "DEPT01",
            tstCateCd = "CATE01",
            tstCateNm = "Category Name",
            updateAuthCd = "AUTH01",
            dupAllowYn = true,
            sortOrder = 1,
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        `when`(repository.findAllGroups()).thenReturn(flowOf(domain))
        `when`(mapper.toResponse(domain)).thenReturn(response)

        // When
        val result = service.getAllGroups().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(response, result[0])
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T
}
