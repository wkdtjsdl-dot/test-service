package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.TestItemMapper
import com.idrsys.ailis.tst.application.required.TestItemRepository
import com.idrsys.ailis.tst.application.usecase.TestItemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class TestItemService(
    private val repository: TestItemRepository,
    private val mapper: TestItemMapper
) : TestItemUseCase {

    // --- TestItem ---

    override suspend fun registerItem(request: TestItemRegisterRequest, adminId: String): TestItemResponse {
        val domain = mapper.toDomain(request)
        val entityWithId = com.idrsys.ailis.tst.domain.model.TestItem(
            tstCd = UUID.randomUUID().toString(),
            tstLargeCateCd = domain.tstLargeCateCd,
            tstMediumCateCd = domain.tstMediumCateCd,
            startDt = domain.startDt,
            endDt = domain.endDt,
            useYn = domain.useYn,
            reqPossYn = domain.reqPossYn,
            webYn = domain.webYn,
            tstNm = domain.tstNm,
            tstAbbrNm = domain.tstAbbrNm,
            tstEngNm = domain.tstEngNm,
            tstEngAbbrNm = domain.tstEngAbbrNm,
            tstIntNm = domain.tstIntNm,
            rstTypeShortYn = domain.rstTypeShortYn,
            rstTypeLongYn = domain.rstTypeLongYn,
            rstTypeFileYn = domain.rstTypeFileYn,
            rstTypeUrlYn = domain.rstTypeUrlYn,
            diseaseCd = domain.diseaseCd,
            tstMethodCd = domain.tstMethodCd,
            refVal = domain.refVal,
            engRefVal = domain.engRefVal,
            clncSgnf = domain.clncSgnf,
            engClncSgnf = domain.engClncSgnf,
            tstDesc = domain.tstDesc,
            tstEngDesc = domain.tstEngDesc,
            tstDayweek = domain.tstDayweek,
            tstTatday = domain.tstTatday,
            insuApplyCd = domain.insuApplyCd,
            insuCd = domain.insuCd,
            insuCateNo = domain.insuCateNo,
            creator = adminId,
            createDtime = java.time.LocalDateTime.now(),
            updater = adminId,
            updateDetime = java.time.LocalDateTime.now()
        )
        entityWithId.setAsNew()
        val saved = repository.save(entityWithId)
        return mapper.toResponse(saved)
    }

    override suspend fun getItem(id: String): TestItemResponse {
        val domain = repository.findById(id) ?: throw RuntimeException("TestItem not found with id: $id")
        return mapper.toResponse(domain)
    }

    override suspend fun updateItem(id: String, request: TestItemUpdateRequest, adminId: String): TestItemResponse {
        val existing = repository.findById(id) ?: throw RuntimeException("TestItem not found with id: $id")
        val updated = com.idrsys.ailis.tst.domain.model.TestItem(
            tstCd = existing.tstCd,
            tstLargeCateCd = request.tstLargeCateCd,
            tstMediumCateCd = request.tstMediumCateCd,
            startDt = request.startDt,
            endDt = request.endDt,
            useYn = request.useYn,
            reqPossYn = request.reqPossYn,
            webYn = request.webYn,
            tstNm = request.tstNm,
            tstAbbrNm = request.tstAbbrNm,
            tstEngNm = request.tstEngNm,
            tstEngAbbrNm = request.tstEngAbbrNm,
            tstIntNm = request.tstIntNm,
            rstTypeShortYn = request.rstTypeShortYn,
            rstTypeLongYn = request.rstTypeLongYn,
            rstTypeFileYn = request.rstTypeFileYn,
            rstTypeUrlYn = request.rstTypeUrlYn,
            diseaseCd = request.diseaseCd,
            tstMethodCd = request.tstMethodCd,
            refVal = request.refVal,
            engRefVal = request.engRefVal,
            clncSgnf = request.clncSgnf,
            engClncSgnf = request.engClncSgnf,
            tstDesc = request.tstDesc,
            tstEngDesc = request.tstEngDesc,
            tstDayweek = request.tstDayweek,
            tstTatday = request.tstTatday,
            insuApplyCd = request.insuApplyCd,
            insuCd = request.insuCd,
            insuCateNo = request.insuCateNo,
            creator = existing.creator,
            createDtime = existing.createDtime,
            updater = adminId,
            updateDetime = java.time.LocalDateTime.now()
        )
        val saved = repository.save(updated)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteItem(id: String, adminId: String) {
        repository.deleteById(id)
    }

    override suspend fun getAllItems(): Flow<TestItemResponse> {
        return repository.findAll().map { mapper.toResponse(it) }
    }

    override suspend fun getItemsByLargeCate(code: String): Flow<TestItemResponse> {
        return repository.findByLargeCateCd(code).map { mapper.toResponse(it) }
    }

    // --- StandardCharge ---

    override suspend fun registerCharge(request: StandardChargeRegisterRequest, adminId: String): StandardChargeResponse {
        val domain = mapper.toDomain(request)
        val entityWithId = com.idrsys.ailis.tst.domain.model.StandardCharge(
            stndChargeId = UUID.randomUUID().toString(),
            tstCd = domain.tstCd,
            applyStartDt = domain.applyStartDt,
            applyEndDt = domain.applyEndDt,
            insuCd = domain.insuCd,
            insuCateNo = domain.insuCateNo,
            relatValuePoint = domain.relatValuePoint,
            insuCharge = domain.insuCharge,
            qladCharge = domain.qladCharge,
            stndCharge = domain.stndCharge,
            lowestCharge = domain.lowestCharge,
            qladCd = domain.qladCd,
            relatValueQladPoint = domain.relatValueQladPoint,
            outputInsuCd = domain.outputInsuCd,
            totalQladCharge = domain.totalQladCharge,
            supval = domain.supval,
            addtax = domain.addtax,
            creator = adminId,
            createDtime = java.time.LocalDateTime.now()
        )
        entityWithId.setAsNew()
        val saved = repository.saveCharge(entityWithId)
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
        val domain = mapper.toDomain(request)
        val entityWithId = com.idrsys.ailis.tst.domain.model.TestItemSpecimen(
            spcmId = UUID.randomUUID().toString(),
            tstCd = domain.tstCd,
            spcmCd = domain.spcmCd,
            sortOrder = domain.sortOrder,
            estlYn = domain.estlYn,
            takeQnty = domain.takeQnty,
            engTakeQnty = domain.engTakeQnty,
            useQnty = domain.useQnty,
            engUseQnty = domain.engUseQnty,
            strgMethodCd = domain.strgMethodCd,
            spcmStbl = domain.spcmStbl,
            engSpcmStbl = domain.engSpcmStbl,
            takeMethod = domain.takeMethod,
            engTakeMethod = domain.engTakeMethod,
            spcmDesc = domain.spcmDesc,
            engDesc = domain.engDesc,
            caution = domain.caution,
            engCaution = domain.engCaution,
            spcmCntnCd = domain.spcmCntnCd,
            creator = adminId,
            createDtime = java.time.LocalDateTime.now(),
            updater = adminId,
            updateDetime = java.time.LocalDateTime.now()
        )
        entityWithId.setAsNew()
        val saved = repository.saveSpecimen(entityWithId)
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
