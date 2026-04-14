package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.WorkListItemRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListItemResponse
import com.idrsys.ailis.tst.application.dto.WorkListRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListResponse
import com.idrsys.ailis.tst.application.mapper.WorkListCommandMapper
import com.idrsys.ailis.tst.application.mapper.WorkListMapper
import com.idrsys.ailis.tst.application.required.repository.WorkListRepository
import com.idrsys.ailis.tst.domain.command.WorkListCreateCommand
import com.idrsys.ailis.tst.domain.command.WorkListItemCreateCommand
import com.idrsys.ailis.tst.domain.model.WorkList
import com.idrsys.ailis.tst.domain.model.WorkListItem
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class WorkListServiceTest {

    @Mock
    lateinit var repository: WorkListRepository

    @Mock
    lateinit var mapper: WorkListMapper

    @Mock
    lateinit var commandMapper: WorkListCommandMapper

    @InjectMocks
    lateinit var service: WorkListService

    @Test
    fun `registerWorkList should save and return response`() = runTest {
        val request = WorkListRegisterRequest(
            wrklistCd = "WRK001",
            useYn = true,
            startDt = LocalDate.of(2026, 4, 14),
            endDt = LocalDate.of(9999, 12, 31),
            wrklistNm = "Work List 1"
        )
        val command = WorkListCreateCommand(
            wrklistCd = "WRK001",
            useYn = true,
            startDt = LocalDate.of(2026, 4, 14),
            endDt = LocalDate.of(9999, 12, 31),
            wrklistNm = "Work List 1"
        )
        val saved = WorkList(
            wrklistCd = "WRK001",
            useYn = true,
            startDt = LocalDate.of(2026, 4, 14),
            endDt = LocalDate.of(9999, 12, 31),
            wrklistNm = "Work List 1",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = WorkListResponse(
            wrklistCd = "WRK001",
            useYn = true,
            startDt = LocalDate.of(2026, 4, 14),
            endDt = LocalDate.of(9999, 12, 31),
            wrklistNm = "Work List 1",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        `when`(repository.findById(request.wrklistCd)).thenReturn(null)
        `when`(commandMapper.toCreateCommand(request)).thenReturn(command)
        `when`(repository.save(any())).thenReturn(saved)
        `when`(mapper.toResponse(saved)).thenReturn(response)

        val result = service.registerWorkList(request, "admin")

        assertEquals(response, result)
    }

    @Test
    fun `registerWorkList should fail when wrklistCd is invalid`() = runTest {
        val request = WorkListRegisterRequest(
            wrklistCd = "WRK-001",
            useYn = true,
            startDt = LocalDate.of(2026, 4, 14),
            endDt = LocalDate.of(9999, 12, 31),
            wrklistNm = "Work List 1"
        )
        val command = WorkListCreateCommand(
            wrklistCd = "WRK-001",
            useYn = true,
            startDt = LocalDate.of(2026, 4, 14),
            endDt = LocalDate.of(9999, 12, 31),
            wrklistNm = "Work List 1"
        )

        `when`(repository.findById(request.wrklistCd)).thenReturn(null)
        `when`(commandMapper.toCreateCommand(request)).thenReturn(command)

        assertFailsWith<IllegalArgumentException> {
            service.registerWorkList(request, "admin")
        }
    }

    @Test
    fun `registerWorkListItem should save and return response`() = runTest {
        val request = WorkListItemRegisterRequest(
            tstCd = "TST001",
            spcmCd = "SPCM001",
            tstOption = "OPT",
            wrklistCd = "WRK001"
        )
        val workList = WorkList(
            wrklistCd = "WRK001",
            useYn = true,
            startDt = LocalDate.of(2026, 4, 14),
            endDt = LocalDate.of(9999, 12, 31),
            wrklistNm = "Work List 1",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val command = WorkListItemCreateCommand(
            tstCd = "TST001",
            spcmCd = "SPCM001",
            tstOption = "OPT",
            wrklistCd = "WRK001"
        )
        val saved = WorkListItem(
            wrklistItmId = "item-1",
            wrklistCd = "WRK001",
            tstCd = "TST001",
            spcmCd = "SPCM001",
            tstOption = "OPT",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val response = WorkListItemResponse(
            wrklistItmId = "item-1",
            wrklistCd = "WRK001",
            tstCd = "TST001",
            spcmCd = "SPCM001",
            tstOption = "OPT",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        `when`(repository.findById(request.wrklistCd)).thenReturn(workList)
        `when`(repository.findItemByWrklistCdAndTstCd(request.wrklistCd, request.tstCd)).thenReturn(null)
        `when`(commandMapper.toCreateItemCommand(request)).thenReturn(command)
        `when`(repository.saveItem(any())).thenReturn(saved)
        `when`(mapper.toResponse(saved)).thenReturn(response)

        val result = service.registerWorkListItem(request, "admin")

        assertEquals(response, result)
    }

    @Test
    fun `registerWorkListItem should fail when duplicate exists`() = runTest {
        val request = WorkListItemRegisterRequest(
            tstCd = "TST001",
            spcmCd = "SPCM001",
            tstOption = "OPT",
            wrklistCd = "WRK001"
        )
        val workList = WorkList(
            wrklistCd = "WRK001",
            useYn = true,
            startDt = LocalDate.of(2026, 4, 14),
            endDt = LocalDate.of(9999, 12, 31),
            wrklistNm = "Work List 1",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        val existingItem = WorkListItem(
            wrklistItmId = "item-1",
            wrklistCd = "WRK001",
            tstCd = "TST001",
            spcmCd = "SPCM001",
            tstOption = "OPT",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        `when`(repository.findById(request.wrklistCd)).thenReturn(workList)
        `when`(repository.findItemByWrklistCdAndTstCd(request.wrklistCd, request.tstCd)).thenReturn(existingItem)

        assertFailsWith<IllegalStateException> {
            service.registerWorkListItem(request, "admin")
        }
    }

    @Test
    fun `deleteWorkListItem should call repository delete`() = runTest {
        val item = WorkListItem(
            wrklistItmId = "item-1",
            wrklistCd = "WRK001",
            tstCd = "TST001",
            spcmCd = "SPCM001",
            tstOption = "OPT",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        `when`(repository.findItemByIdAndWrklistCd("item-1", "WRK001")).thenReturn(item)

        service.deleteWorkListItem("item-1", "WRK001", "admin")

        Mockito.verify(repository).deleteItemById("item-1")
    }

    @Test
    fun `deleteWorkListItem should fail when item is not found`() = runTest {
        `when`(repository.findItemByIdAndWrklistCd("item-1", "WRK001")).thenReturn(null)

        assertFailsWith<RuntimeException> {
            service.deleteWorkListItem("item-1", "WRK001", "admin")
        }
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T
}
