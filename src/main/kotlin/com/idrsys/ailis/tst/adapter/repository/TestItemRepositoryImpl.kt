package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.dto.request.UnspecifiedDepartmentTestItemSearchParam
import com.idrsys.ailis.tst.application.required.repository.TestItemRepository
import com.idrsys.ailis.tst.domain.model.*
import com.idrsys.ailis.tst.generated.jooq.Tables.BTS_ITEM
import com.idrsys.ailis.tst.generated.jooq.tables.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.conf.ParamType
import org.jooq.impl.DSL
import org.jooq.impl.DSL.notExists
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
interface TestItemDataRepository : CoroutineCrudRepository<TestItem, String>

@Repository
interface StandardChargeDataRepository : CoroutineCrudRepository<StandardCharge, String>

@Repository
interface TestItemSpecimenDataRepository : CoroutineCrudRepository<TestItemSpecimen, String>

@Repository
interface TestItemRefItemDataRepository : CoroutineCrudRepository<TestItemRefItem, String>

@Repository
interface TestItemGeneDataRepository : CoroutineCrudRepository<TestItemGene, String>

@Repository
interface TestItemEssentialDocDataRepository : CoroutineCrudRepository<TestItemEssentialDoc, String>

@Repository
interface TestItemHstDataRepository : CoroutineCrudRepository<TestItemHst, String>

@Repository
interface TestItemSpecimenHstDataRepository : CoroutineCrudRepository<TestItemSpecimenHst, String>

@Repository
class TestItemRepositoryImpl(
    private val itemDataRepo: TestItemDataRepository,
    private val chargeDataRepo: StandardChargeDataRepository,
    private val specimenDataRepo: TestItemSpecimenDataRepository,
    private val refItemDataRepo: TestItemRefItemDataRepository,
    private val geneDataRepo: TestItemGeneDataRepository,
    private val essentialDocDataRepo: TestItemEssentialDocDataRepository,
    private val testItemHstDataRepo: TestItemHstDataRepository,
    private val testItemSpecimenHstDataRepo: TestItemSpecimenHstDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : TestItemRepository {

    // --- TestItem ---
    override suspend fun save(entity: TestItem): TestItem = itemDataRepo.save(entity)
    override suspend fun findById(tstCd: String): TestItem? = itemDataRepo.findById(tstCd)
//    override suspend fun findAll(): Flow<TestItem> = itemDataRepo.findAll()
    override suspend fun findAll(): Flow<TestItem> {
        val query = dslContext
            .select(BTS_ITEM.asterisk())
            .from(BTS_ITEM)
            .orderBy(BTS_ITEM.TST_CD.asc())

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toTestItem(row) }
            .asFlow()
    }

    override fun getItems(searchParam: TestItemSearchParam): Flow<TestItem> {
        val deptTestItem = BbsDeptTstItem.BBS_DEPT_TST_ITEM
        val tstItem = BtsItem.BTS_ITEM

        var condition: Condition = DSL.trueCondition()

        searchParam.tstLargeCateCd?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(tstItem.TST_LARGE_CATE_CD.eq(it))
        }
        searchParam.tstMediumCateCd?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(tstItem.TST_MEDIUM_CATE_CD.eq(it))
        }
        searchParam.useYn?.let {
            condition = condition.and(tstItem.USE_YN.eq(it))
        }

        var selectFrom = dslContext
            .select(tstItem.fields().toList())
            .from(tstItem)

        // deptCd가 존재하면 LEFT OUTER JOIN 추가
        searchParam.deptCd?.takeIf { it.isNotBlank() }?.let { deptCd ->
            selectFrom = selectFrom.innerJoin(deptTestItem)
                .on(
                    tstItem.TST_CD.eq(deptTestItem.TST_CD)
                        .and(deptTestItem.DEPT_CD.eq(deptCd))
                )
        }

        val query = selectFrom.where(condition).orderBy(tstItem.TST_CD.asc())

        // SQL과 바인딩 값 준비
        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toTestItem(row) }
            .asFlow()
    }

    override fun findSimpleItems(searchParam: TestItemSearchParam): Flow<TestItemSimpleResponse> {
        val tstItem = BtsItem.BTS_ITEM

        var condition: Condition = DSL.trueCondition()

        searchParam.tstLargeCateCd?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(tstItem.TST_LARGE_CATE_CD.eq(it))
        }
        searchParam.tstMediumCateCd?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(tstItem.TST_MEDIUM_CATE_CD.eq(it))
        }
        searchParam.useYn?.let {
            condition = condition.and(tstItem.USE_YN.eq(it))
        }

        val selectFrom = dslContext
            .select(
                tstItem.TST_CD,  // Only select 2 columns instead of 38
                tstItem.TST_NM
            )
            .from(tstItem)

        val query = selectFrom.where(condition).orderBy(tstItem.TST_CD.asc())

        // SQL과 바인딩 값 준비
        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .map { row, _ ->
                TestItemSimpleResponse(
                    tstCd = row.get(tstItem.TST_CD.name, String::class.java)!!,
                    tstNm = row.get(tstItem.TST_NM.name, String::class.java)!!
                )
            }
            .all()
            .asFlow()
    }

    override fun findUnspecifiedDeptItems(searchParam: UnspecifiedDepartmentTestItemSearchParam): Flow<TestItem> {
        if (searchParam.deptCd.isBlank()) {
            return flowOf()
        }

        val deptTestItem = BbsDeptTstItem.BBS_DEPT_TST_ITEM
        val tstItem = BtsItem.BTS_ITEM
        val useYn = searchParam.useYn ?: true

        // 서브쿼리
        val notExistsCondition = notExists(
            dslContext.selectOne()
                .from(deptTestItem)
                .where(
                    deptTestItem.TST_CD.eq(tstItem.TST_CD)
                        .and(deptTestItem.DEPT_CD.eq(searchParam.deptCd))
                )
        )

        var condition = notExistsCondition

        searchParam.tstLargeCateCd?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(tstItem.TST_LARGE_CATE_CD.eq(it))
        }
        searchParam.tstMediumCateCd?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(tstItem.TST_MEDIUM_CATE_CD.eq(it))
        }
        useYn.let {
            condition = condition.and(tstItem.USE_YN.eq(it))
        }

        val query = dslContext.select(tstItem.fields().toList())
            .from(tstItem)
            .where(condition)

        // SQL과 바인딩 값 준비
        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toTestItem(row) }
            .asFlow()
    }

    override fun autoCompleteItems(searchParam: TestItemAutoCompleteParam): Flow<TestItemSimpleResponse> {
        val tstItem = BtsItem.BTS_ITEM
        val keyword = "%${searchParam.keyword}%"

        val condition = tstItem.TST_CD.likeIgnoreCase(keyword).or(tstItem.TST_NM.likeIgnoreCase(keyword))

        val query = dslContext.select(
            tstItem.TST_CD,
            tstItem.TST_NM
        )
            .from(tstItem)
            .where(condition)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .map { row, _ ->
                TestItemSimpleResponse(
                    tstCd = row.get(tstItem.TST_CD.name, String::class.java)!!,
                    tstNm = row.get(tstItem.TST_NM.name, String::class.java)!!
                )
            }
            .all()
            .asFlow()
    }

    override fun findSimpleItemByTstCd(tstCds: List<String>): Flow<TestItemSimpleResponse> {
        val tstItem = BtsItem.BTS_ITEM

        val condition = mutableListOf<Condition>()

        tstCds.takeIf { it.isNotEmpty() }?.let {
            condition.add(tstItem.TST_CD.`in`(it))
        }

        val query = dslContext.select(
            tstItem.TST_CD,
            tstItem.TST_NM
        )
            .from(tstItem)
            .where(condition)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .map { row, _ ->
                TestItemSimpleResponse(
                    tstCd = row.get(tstItem.TST_CD.name, String::class.java)!!,
                    tstNm = row.get(tstItem.TST_NM.name, String::class.java)!!
                )
            }
            .all()
            .asFlow()
    }

    // --- StandardCharge ---
    override suspend fun saveCharge(entity: StandardCharge): StandardCharge = chargeDataRepo.save(entity)
    override suspend fun findChargeById(id: String): StandardCharge? = chargeDataRepo.findById(id)
    override suspend fun deleteChargeById(id: String) = chargeDataRepo.deleteById(id)

    override suspend fun findChargesByTestCd(tstCd: String, sort: String?): Flow<StandardCharge> {
        val table = BtsStndCharge.BTS_STND_CHARGE

        var selectQuery = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(table.TST_CD.eq(tstCd))

        // Parse and apply multiple ORDER BY clauses if sort parameter is provided
        val query = if (!sort.isNullOrBlank()) {
            val sortFields = sort.split(",").mapNotNull { sortPart ->
                val parts = sortPart.trim().split(":")
                val fieldName = parts[0].trim().lowercase()
                val direction = if (parts.size > 1) parts[1].trim().uppercase() else "ASC"

                // GET /api/bts/item/stnd-charge?tstCd=TEST001&sort=apply_start_dt:DESC,stnd_price:ASC
                val jooqField = when (fieldName) {
                    "apply_start_dt" -> table.APPLY_START_DT
                    "apply_end_dt" -> table.APPLY_END_DT
                    "stnd_price" -> table.STND_PRICE
                    "insure_price" -> table.INSURE_PRICE
                    "qlad_charge" -> table.QLAD_CHARGE
                    else -> null
                }

                jooqField?.let {
                    if (direction == "DESC") it.desc() else it.asc()
                }
            }

            if (sortFields.isNotEmpty()) {
                selectQuery.orderBy(sortFields)
            } else {
                selectQuery
            }
        } else {
            selectQuery
        }

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toStandardCharge(row) }
            .asFlow()
    }

    override suspend fun getEqualDate(entity: StandardCharge): Flow<StandardCharge> {
        val table = BtsStndCharge.BTS_STND_CHARGE

        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(
                table.TST_CD.eq(entity.tstCd)
                    .and(table.APPLY_START_DT.le(entity.applyEndDt))
                    .and(table.APPLY_END_DT.ge(entity.applyStartDt))
            )

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
        .fetch()
        .all()
        .map { row -> toStandardCharge(row) }
        .asFlow()
    }

    override suspend fun findLatestChargeByTestCdAndDate(tstCd: String, currentDate: LocalDate): StandardCharge? {
        val table = BtsStndCharge.BTS_STND_CHARGE
        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(
                table.TST_CD.eq(tstCd)
                    .and(table.APPLY_START_DT.le(currentDate))
                    .and(table.APPLY_END_DT.ge(currentDate))
            )
            .orderBy(table.APPLY_END_DT.desc())
            .limit(1)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        val row = executeSpec
            .fetch()
            .one()
            .awaitSingleOrNull()

        return row?.let { toStandardCharge(it) }
    }

    // --- TestItemSpecimen ---
    override suspend fun saveSpecimen(entity: TestItemSpecimen): TestItemSpecimen = specimenDataRepo.save(entity)
    override suspend fun findSpecimenById(spcmId: String): TestItemSpecimen? = specimenDataRepo.findById(spcmId)
    override suspend fun deleteSpecimenById(id: String) = specimenDataRepo.deleteById(id)

    override fun findSpecimensByTestCd(tstCd: String): Flow<TestItemSpecimen> {
        val table = BtsSpcm.BTS_SPCM
        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(table.TST_CD.eq(tstCd))

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toTestItemSpecimen(row) }
            .asFlow()
    }

    override suspend fun getSpecimenDetailById(spcmId: String): TestItemSpecimenDetailResponse? {
        val btsSpcm = BtsSpcm.BTS_SPCM
        val bbsSpcm = BbsSpcm.BBS_SPCM

        val query = dslContext
            .select(
                btsSpcm.SPCM_ID,
                btsSpcm.TST_CD,
                btsSpcm.SPCM_CD,
                bbsSpcm.SPCM_NM,
                bbsSpcm.SPCM_CATE_CD,
                btsSpcm.SORT_ORDER,
                btsSpcm.ESTL_YN,
                btsSpcm.TAKE_QNTY,
                btsSpcm.ENG_TAKE_QNTY,
                btsSpcm.USE_QNTY,
                btsSpcm.ENG_USE_QNTY,
                btsSpcm.STRG_METHOD,
                btsSpcm.ENG_STRG_METHOD,
                btsSpcm.SPCM_STBL,
                btsSpcm.ENG_SPCM_STBL,
                btsSpcm.TAKE_METHOD,
                btsSpcm.ENG_TAKE_METHOD,
                btsSpcm.SPCM_DESC,
                btsSpcm.ENG_DESC,
                btsSpcm.CAUTION,
                btsSpcm.ENG_CAUTION,
                btsSpcm.SPCM_CNTN_CD,
                btsSpcm.CREATOR,
                btsSpcm.CREATE_DTIME,
                btsSpcm.UPDATER,
                btsSpcm.UPDATE_DTIME
            )
            .from(btsSpcm)
            .leftJoin(bbsSpcm).on(btsSpcm.SPCM_CD.eq(bbsSpcm.SPCM_CD))
            .where(btsSpcm.SPCM_ID.eq(spcmId))

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        val row = executeSpec
            .fetch()
            .one()
            .awaitSingleOrNull()

        return row?.let { toTestItemSpecimenResponse(it, btsSpcm, bbsSpcm) }
    }

    override fun getSpecimenDetailsByTestCd(tstCd: String): Flow<TestItemSpecimenListResponse> {
        val btsSpcm = BtsSpcm.BTS_SPCM
        val bbsSpcm = BbsSpcm.BBS_SPCM

        val query = dslContext
            .select(
                btsSpcm.SPCM_ID,
                btsSpcm.TST_CD,
                btsSpcm.SPCM_CD,
                bbsSpcm.SPCM_NM,
                btsSpcm.SORT_ORDER,
                btsSpcm.ESTL_YN,
                btsSpcm.CREATOR,
                btsSpcm.CREATE_DTIME,
                btsSpcm.UPDATER,
                btsSpcm.UPDATE_DTIME
            )
            .from(btsSpcm)
            .leftJoin(bbsSpcm).on(btsSpcm.SPCM_CD.eq(bbsSpcm.SPCM_CD))
            .where(btsSpcm.TST_CD.eq(tstCd))

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toTestItemSpecimenListResponse(row, btsSpcm, bbsSpcm) }
            .asFlow()
    }

    override suspend fun findSpecimensByTstCds(tstCds: List<String>): Flow<TestItemSpecimensResponse> {
        if (tstCds.isEmpty()) {
            return emptyFlow()
        }

        val btsSpcm = BtsSpcm.BTS_SPCM
        val bbsSpcm = BbsSpcm.BBS_SPCM

        val query = dslContext
            .select(
                btsSpcm.TST_CD,
                btsSpcm.SPCM_CD,
                bbsSpcm.SPCM_NM
            )
            .from(btsSpcm)
            .leftJoin(bbsSpcm).on(btsSpcm.SPCM_CD.eq(bbsSpcm.SPCM_CD))
            .where(btsSpcm.TST_CD.`in`(tstCds))
            .orderBy(btsSpcm.TST_CD, btsSpcm.SORT_ORDER)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        val list = executeSpec
            .fetch()
            .all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

        val grouped = list.groupBy(
            { it[btsSpcm.TST_CD.name] as String },
            { SpecimenSimple(
                spcmCd = it[btsSpcm.SPCM_CD.name] as String,
                spcmNm = it[bbsSpcm.SPCM_NM.name] as String?
            )}
        )

        return kotlinx.coroutines.flow.flow {
            grouped.forEach { (tstCd, specimens) ->
                emit(TestItemSpecimensResponse(
                    tstCd = tstCd,
                    specimens = specimens
                ))
            }
        }
    }

    private fun toTestItemSpecimenListResponse(row: Map<String, Any>, btsSpcm: BtsSpcm, bbsSpcm: BbsSpcm): TestItemSpecimenListResponse {
        return TestItemSpecimenListResponse(
            spcmId = row[btsSpcm.SPCM_ID.name] as String,
            tstCd = row[btsSpcm.TST_CD.name] as String,
            spcmCd = row[btsSpcm.SPCM_CD.name] as String,
            spcmNm = row[bbsSpcm.SPCM_NM.name] as String?,
            sortOrder = (row[btsSpcm.SORT_ORDER.name] as Number).toInt(),
            estlYn = row[btsSpcm.ESTL_YN.name] as Boolean,
            creator = row[btsSpcm.CREATOR.name] as String,
            createDtime = row[btsSpcm.CREATE_DTIME.name] as LocalDateTime,
            updater = row[btsSpcm.UPDATER.name] as String?,
            updateDtime = row[btsSpcm.UPDATE_DTIME.name] as LocalDateTime?
        )
    }

    private fun toTestItemSpecimenResponse(row: Map<String, Any>, btsSpcm: BtsSpcm, bbsSpcm: BbsSpcm): TestItemSpecimenDetailResponse {
        return TestItemSpecimenDetailResponse(
            spcmId = row[btsSpcm.SPCM_ID.name] as String,
            tstCd = row[btsSpcm.TST_CD.name] as String,
            spcmCd = row[btsSpcm.SPCM_CD.name] as String,
            spcmNm = row[bbsSpcm.SPCM_NM.name] as String,
            spcmCateCd = row[bbsSpcm.SPCM_CATE_CD.name] as String?,
            sortOrder = (row[btsSpcm.SORT_ORDER.name] as Number).toInt(),
            estlYn = row[btsSpcm.ESTL_YN.name] as Boolean,
            takeQnty = row[btsSpcm.TAKE_QNTY.name] as String,
            engTakeQnty = row[btsSpcm.ENG_TAKE_QNTY.name] as String,
            useQnty = row[btsSpcm.USE_QNTY.name] as String,
            engUseQnty = row[btsSpcm.ENG_USE_QNTY.name] as String,
            strgMethod = row[btsSpcm.STRG_METHOD.name] as String,
            engStrgMethod = row[btsSpcm.ENG_STRG_METHOD.name] as String,
            spcmStbl = row[btsSpcm.SPCM_STBL.name] as String?,
            engSpcmStbl = row[btsSpcm.ENG_SPCM_STBL.name] as String?,
            takeMethod = row[btsSpcm.TAKE_METHOD.name] as String?,
            engTakeMethod = row[btsSpcm.ENG_TAKE_METHOD.name] as String?,
            spcmDesc = row[btsSpcm.SPCM_DESC.name] as String,
            engDesc = row[btsSpcm.ENG_DESC.name] as String?,
            caution = row[btsSpcm.CAUTION.name] as String,
            engCaution = row[btsSpcm.ENG_CAUTION.name] as String,
            spcmCntnCd = row[btsSpcm.SPCM_CNTN_CD.name] as String,
            creator = row[btsSpcm.CREATOR.name] as String,
            createDtime = row[btsSpcm.CREATE_DTIME.name] as LocalDateTime,
            updater = row[btsSpcm.UPDATER.name] as String?,
            updateDtime = row[btsSpcm.UPDATE_DTIME.name] as LocalDateTime?
        )
    }

    private fun toTestItem(row: Map<String, Any>): TestItem {
        return TestItem(
            tstCd = row["tst_cd"] as String?,
            tstLargeCateCd = row["tst_large_cate_cd"] as String,
            tstMediumCateCd = row["tst_medium_cate_cd"] as String,
            startDt = row["start_dt"] as LocalDate,
            endDt = row["end_dt"] as LocalDate,
            useYn = row["use_yn"] as Boolean,
            reqPossYn = row["req_poss_yn"] as Boolean,
            webKorYn = row["web_kor_yn"] as Boolean,
            webEngYn = row["web_eng_yn"] as Boolean,
            tstNm = row["tst_nm"] as String,
            tstAbbrNm = row["tst_abbr_nm"] as String,
            tstEngNm = row["tst_eng_nm"] as String,
            tstEngAbbrNm = row["tst_eng_abbr_nm"] as String,
            tstIntNm = row["tst_int_nm"] as String?,
            rstTypeShortYn = row["rst_type_short_yn"] as Boolean,
            rstTypeLongYn = row["rst_type_long_yn"] as Boolean,
            rstTypeFileYn = row["rst_type_file_yn"] as Boolean,
            rstTypeUrlYn = row["rst_type_url_yn"] as Boolean,
            diseaseCd = row["disease_cd"] as String?,
            tstMethodCd = row["tst_method_cd"] as String?,
            refVal = row["ref_val"] as String?,
            engRefVal = row["eng_ref_val"] as String?,
            clncSgnf = row["clnc_sgnf"] as String?,
            engClncSgnf = row["eng_clnc_sgnf"] as String?,
            tstDesc = row["tst_desc"] as String?,
            tstEngDesc = row["tst_eng_desc"] as String?,
            tstDayweek = row["tst_dayweek"] as String?,
            tstTatday = (row["tst_tatday"] as Number?)?.toInt(),
            insuApplyCd = row["insu_apply_cd"] as String?,
            insuCd = row["insu_cd"] as String?,
            insuCateNo = row["insu_cate_no"] as String?,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_dtime"] as LocalDateTime
        )
    }

    private fun toStandardCharge(row: Map<String, Any>): StandardCharge {
        return StandardCharge(
            stndChargeId = row["stnd_charge_id"] as String?,
            tstCd = row["tst_cd"] as String,
            applyStartDt = row["apply_start_dt"] as LocalDate,
            applyEndDt = row["apply_end_dt"] as LocalDate,
            insuCd = row["insu_cd"] as String?,
            insuCateNo = row["insu_cate_no"] as String?,
            relatValuePoint = (row["relat_value_point"] as Number?)?.toDouble(),
            insurePrice = (row["insure_price"] as Number).toDouble(),
            qladCharge = (row["qlad_charge"] as Number).toDouble(),
            stndPrice = (row["stnd_price"] as Number).toDouble(),
            lowestCharge = (row["lowest_charge"] as Number).toDouble(),
            qladCd = row["qlad_cd"] as String?,
            relatValueQladPoint = (row["relat_value_qlad_point"] as Number).toDouble(),
            outputInsuCd = row["output_insu_cd"] as String?,
            totalQladCharge = (row["total_qlad_charge"] as Number).toDouble(),
            supval = (row["supval"] as Number).toDouble(),
            addtax = (row["addtax"] as Number).toDouble(),
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime
        )
    }

    private fun toTestItemSpecimen(row: Map<String, Any>): TestItemSpecimen {
        return TestItemSpecimen(
            spcmId = row["spcm_id"] as String?,
            tstCd = row["tst_cd"] as String,
            spcmCd = row["spcm_cd"] as String,
            sortOrder = (row["sort_order"] as Number).toInt(),
            estlYn = row["estl_yn"] as Boolean,
            takeQnty = row["take_qnty"] as String,
            engTakeQnty = row["eng_take_qnty"] as String,
            useQnty = row["use_qnty"] as String,
            engUseQnty = row["eng_use_qnty"] as String,
            strgMethod = row["strg_method"] as String,
            engStrgMethod = row["eng_strg_method"] as String,
            spcmStbl = row["spcm_stbl"] as String?,
            engSpcmStbl = row["eng_spcm_stbl"] as String?,
            takeMethod = row["take_method"] as String?,
            engTakeMethod = row["eng_take_method"] as String?,
            spcmDesc = row["spcm_desc"] as String,
            engDesc = row["eng_desc"] as String?,
            caution = row["caution"] as String,
            engCaution = row["eng_caution"] as String,
            spcmCntnCd = row["spcm_cntn_cd"] as String,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String?,
            updateDtime = row["update_dtime"] as LocalDateTime?
        )
    }

    // --- TestItemRefItem ---
    override suspend fun saveRefItem(entity: TestItemRefItem): TestItemRefItem = refItemDataRepo.save(entity)
    override suspend fun findRefItemById(refItemId: String): TestItemRefItem? = refItemDataRepo.findById(refItemId)
    override suspend fun deleteRefItemById(refItemId: String) = refItemDataRepo.deleteById(refItemId)

    override fun findRefItemsByTstCd(searchParam: TestItemRefRequest): Flow<TestItemRefResponse> {
        val refItem = BtsRefItem.BTS_REF_ITEM
        val tstRef = BbsTstRef.BBS_TST_REF

        val conditions = mutableListOf<Condition>()

        searchParam.tstCd.takeIf { it.isNotBlank() }?.let {
            conditions.add(refItem.TST_CD.eq(it))
        }
        searchParam.refCateCd?.takeIf { it.isNotBlank() }?.let {
            conditions.add(tstRef.REF_CATE_CD.eq(it))
        }

        val query = dslContext
            .select(refItem.REF_ITEM_ID, refItem.TST_CD, refItem.REF_CD, tstRef.REF_CATE_CD,
                    refItem.SORT_ORDER, tstRef.REF_NM, tstRef.REF_ENG_NM, refItem.ESTL_YN,
                    tstRef.REF_TYPE, tstRef.DATA_FORMAT, tstRef.REF_MIN_VAL, tstRef.REF_MAX_VAL)
            .from(tstRef)
            .join(refItem).on(tstRef.REF_CD.eq(refItem.REF_CD))
            .where(conditions)
            .orderBy(refItem.SORT_ORDER)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toTestItemRefResponse(row) }
            .asFlow()
    }

    override suspend fun getDetailRefItemById(refItemId: String): TestItemRefDetailResponse? {
        val refItem = BtsRefItem.BTS_REF_ITEM
        val tstRef = BbsTstRef.BBS_TST_REF

        val conditions = mutableListOf<Condition>()

        refItemId.takeIf { it.isNotBlank() }?.let {
            conditions.add(refItem.REF_ITEM_ID.eq(it))
        }

        val query = dslContext
            .select(refItem.REF_ITEM_ID, tstRef.REF_CATE_CD, refItem.TST_CD, tstRef.REF_CD, tstRef.REF_NM,
                tstRef.REF_TYPE, tstRef.REF_SIZE, refItem.SORT_ORDER, refItem.ESTL_YN,
                tstRef.DATA_FORMAT, tstRef.REF_MIN_VAL, tstRef.REF_MAX_VAL)
            .from(tstRef)
            .join(refItem).on(tstRef.REF_CD.eq(refItem.REF_CD))
            .where(conditions)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        val row = executeSpec
            .fetch()
            .one()
            .awaitSingleOrNull()

        return row?.let { toTestItemRefDetailResponse(it) }
    }

    override suspend fun findRefItemsByTstCds(tstCds: List<String>): Flow<TestItemRefItemsResponse> {
        if (tstCds.isEmpty()) {
            return emptyFlow()
        }

        val refItem = BtsRefItem.BTS_REF_ITEM
        val tstRef = BbsTstRef.BBS_TST_REF

        val query = dslContext
            .select(
                refItem.TST_CD,
                refItem.REF_CD,
                tstRef.REF_NM
            )
            .from(refItem)
            .join(tstRef).on(refItem.REF_CD.eq(tstRef.REF_CD))
            .where(refItem.TST_CD.`in`(tstCds))
            .orderBy(refItem.TST_CD, refItem.SORT_ORDER)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        val list = executeSpec
            .fetch()
            .all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

        val grouped = list.groupBy(
            { it[refItem.TST_CD.name] as String },
            { RefItemSimple(
                refCd = it[refItem.REF_CD.name] as String,
                refNm = it[tstRef.REF_NM.name] as String
            )}
        )

        return kotlinx.coroutines.flow.flow {
            grouped.forEach { (tstCd, refItems) ->
                emit(TestItemRefItemsResponse(
                    tstCd = tstCd,
                    refItems = refItems
                ))
            }
        }
    }

    override suspend fun findRefItemsDetailByTstCds(tstCds: List<String>): Flow<TestItemRefDetailItemsResponse> {
        if (tstCds.isEmpty()) {
            return emptyFlow()
        }

        val refItem = BtsRefItem.BTS_REF_ITEM
        val tstRef = BbsTstRef.BBS_TST_REF

        val query = dslContext
            .select(
                refItem.TST_CD,
                refItem.REF_CD,
                tstRef.REF_NM,
                tstRef.REF_ENG_NM,
                refItem.ESTL_YN,
                tstRef.DATA_FORMAT
            )
            .from(refItem)
            .join(tstRef).on(refItem.REF_CD.eq(tstRef.REF_CD))
            .where(refItem.TST_CD.`in`(tstCds))
            .orderBy(refItem.TST_CD, refItem.SORT_ORDER)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        val list = executeSpec
            .fetch()
            .all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

        val grouped = list.groupBy(
            { it[refItem.TST_CD.name] as String },
            { RefItemDetail(
                refCd = it[refItem.REF_CD.name] as String,
                refNm = it[tstRef.REF_NM.name] as String,
                refEngNm = it[tstRef.REF_ENG_NM.name] as String,
                estlYn = it[refItem.ESTL_YN.name] as Boolean,
                dataFormat = it[tstRef.DATA_FORMAT.name] as String,
            )}
        )

        return kotlinx.coroutines.flow.flow {
            grouped.forEach { (tstCd, refItems) ->
                emit(TestItemRefDetailItemsResponse(
                    tstCd = tstCd,
                    refItems = refItems
                ))
            }
        }
    }

    private fun toTestItemRefItem(row: Map<String, Any>): TestItemRefItem {
        return TestItemRefItem(
            refItemId = row["ref_item_id"] as String?,
            tstCd = row["tst_cd"] as String,
            refCd = row["ref_cd"] as String,
            estlYn = row["estl_yn"] as Boolean,
            sortOrder = (row["sort_order"] as Number).toInt(),
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String?,
            updateDtime = row["update_dtime"] as LocalDateTime?
        )
    }

    private fun toTestItemRefResponse(row: Map<String, Any>): TestItemRefResponse {
        return TestItemRefResponse(
            refItemId = row["ref_item_id"] as String,
            tstCd = row["tst_cd"] as String,
            refCd = row["ref_cd"] as String,
            refCateCd = row["ref_cate_cd"] as String?,
            sortOrder = row["sort_order"] as Int?,
            refNm = row["ref_nm"] as String,
            refEngNm = row["ref_eng_nm"] as String,
            estlYn = row["estl_yn"] as Boolean,
            refType = row["ref_type"] as String?,
            dataFormat = row["data_format"] as String?,
            refMinVal = (row["ref_min_val"] as Number?)?.toInt(),
            refMaxVal = (row["ref_max_val"] as Number?)?.toInt(),
        )
    }

    private fun toTestItemRefDetailResponse(row: Map<String, Any>): TestItemRefDetailResponse {
        return TestItemRefDetailResponse(
            refItemId = row["ref_item_id"] as String,
            refCateCd = row["ref_cate_cd"] as String,
            tstCd = row["tst_cd"] as String,
            refCd = row["ref_cd"] as String,
            refNm = row["ref_nm"] as String,
            refType = row["ref_type"] as String,
            refSize = row["ref_size"] as Int?,
            sortOrder = row["sort_order"] as Int?,
            estlYn = row["estl_yn"] as Boolean,
            dataFormat = row["data_format"] as String?,
            refMinVal = (row["ref_min_val"] as Number?)?.toInt(),
            refMaxVal = (row["ref_max_val"] as Number?)?.toInt(),
        )
    }

    // --- TestGene ---
    override fun getGenes(request: TestGeneRequest): Flow<TestGene> {
        val table = BbsGene.BBS_GENE
        val itemTable = BtsItemGene.BTS_ITEM_GENE
        if (request.geneCd.isBlank() || request.geneCd.length < 3) {
            return emptyFlow()
        }

        val keyword = request.geneCd.uppercase()

        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .leftJoin(itemTable).on(table.GENE_CD.eq(itemTable.GENE_CD))
            .and(itemTable.TST_CD.eq(request.tstCd))
            .where(table.GENE_CD.like("$keyword%"))
            .and(itemTable.GENE_CD.isNull)

        val sql = query.getSQL(ParamType.INLINED)

        return databaseClient.sql(sql)
            .fetch()
            .all()
            .map { row -> toTestGene(row) }
            .asFlow()
    }



    private fun toTestGene(row: Map<String, Any>): TestGene {
        return TestGene(
            geneCd = row["gene_cd"] as String,
            geneNm = row["gene_nm"] as String?,
            sortOrder = row["sort_order"] as Int?,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_dtime"] as LocalDateTime

        )
    }

    // --- TestItemGene ---
    override suspend fun saveGene(entity: TestItemGene): TestItemGene = geneDataRepo.save(entity)
    override suspend fun deleteGeneById(itemGeneId: String) = geneDataRepo.deleteById(itemGeneId)

    override fun findGenesByTestCd(tstCd: String): Flow<TestItemGeneResponse> {
        val ig = BtsItemGene.BTS_ITEM_GENE
        val g = BbsGene.BBS_GENE

        // JOIN QUERY 생성
        val query = dslContext
            .select(
                ig.ITEM_GENE_ID,
                ig.TST_CD,
                ig.GENE_CD,
                ig.CREATOR,
                ig.CREATE_DTIME,
                // bbs_gene 컬럼 추가
                g.GENE_NM,
                g.SORT_ORDER
            )
            .from(ig)
            .join(g).on(ig.GENE_CD.eq(g.GENE_CD))
            .where(ig.TST_CD.eq(tstCd))

        // SQL 생성
        var executeSpec = databaseClient.sql(query.sql)

        // 바인딩 처리
        query.bindValues.forEachIndexed { index, value ->
            executeSpec =
                if (value != null) executeSpec.bind(index, value)
                else executeSpec.bindNull(index, String::class.java)
        }

        // flow 변환
        return executeSpec
            .fetch()
            .all()
            .map { row -> toTestItemGene(row) }
            .asFlow()
    }

    private fun toTestItemGene(row: Map<String, Any>): TestItemGeneResponse {
        return TestItemGeneResponse(
            itemGeneId = row["item_gene_id"] as String,
            tstCd = row["tst_cd"] as String,
            geneCd = row["gene_cd"] as String,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            geneNm = row["gene_nm"] as String?,
        )
    }

    // --- TestItemEssentialDoc ---
    override suspend fun saveEssentialDoc(entity: TestItemEssentialDoc): TestItemEssentialDoc = essentialDocDataRepo.save(entity)
    override suspend fun findEssentialDocById(itemEstlDocId: String): TestItemEssentialDoc? = essentialDocDataRepo.findById(itemEstlDocId)
    override suspend fun deleteEssentialDocById(itemEstlDocId: String) = essentialDocDataRepo.deleteById(itemEstlDocId)

    override fun findEssentialDocsByTstCd(tstCd: String): Flow<TestItemEssentialDocListResponse> {
        val itemEstlDoc = BtsItemEstlDoc.BTS_ITEM_ESTL_DOC
        val tstReqDoc = BbsTstReqDoc.BBS_TST_REQ_DOC

        val query = dslContext
            .select(
                itemEstlDoc.ITEM_ESTL_DOC_ID,
                itemEstlDoc.TST_CD,
                itemEstlDoc.DOC_CD,
                tstReqDoc.DOC_DIV_CD,
                tstReqDoc.DOC_NM
            )
            .from(itemEstlDoc)
            .join(tstReqDoc).on(itemEstlDoc.DOC_CD.eq(tstReqDoc.DOC_CD))
            .where(itemEstlDoc.TST_CD.eq(tstCd))

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .map { row, _ ->
                TestItemEssentialDocListResponse(
                    itemEstlDocId = row.get(itemEstlDoc.ITEM_ESTL_DOC_ID.name, String::class.java)!!,
                    tstCd = row.get(itemEstlDoc.TST_CD.name, String::class.java)!!,
                    docCd = row.get(itemEstlDoc.DOC_CD.name, String::class.java)!!,
                    docDivCd = row.get(tstReqDoc.DOC_DIV_CD.name, String::class.java)!!,
                    docNm = row.get(tstReqDoc.DOC_NM.name, String::class.java)!!,
                )
            }
            .all()
            .asFlow()
    }

    override suspend fun getDetailEssentialDocById(itemEstlDocId: String): TestItemEssentialDocDetailResponse? {
        val itemEstlDoc = BtsItemEstlDoc.BTS_ITEM_ESTL_DOC
        val tstReqDoc = BbsTstReqDoc.BBS_TST_REQ_DOC

        val query = dslContext
            .select(
                itemEstlDoc.ITEM_ESTL_DOC_ID,
                itemEstlDoc.TST_CD,
                itemEstlDoc.DOC_CD,
                tstReqDoc.DOC_DIV_CD,
                tstReqDoc.DOC_NM,
                tstReqDoc.DOC_ENG_NM,
                tstReqDoc.DOC_FILE_ID,
                tstReqDoc.DOC_ENG_FILE_ID
            )
            .from(itemEstlDoc)
            .join(tstReqDoc).on(itemEstlDoc.DOC_CD.eq(tstReqDoc.DOC_CD))
            .where(itemEstlDoc.ITEM_ESTL_DOC_ID.eq(itemEstlDocId))

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .one()
            .map { row ->
                TestItemEssentialDocDetailResponse(
                    itemEstlDocId = row[itemEstlDoc.ITEM_ESTL_DOC_ID.name] as String,
                    tstCd = row[itemEstlDoc.TST_CD.name] as String,
                    docCd = row[itemEstlDoc.DOC_CD.name] as String,
                    docDivCd = row[tstReqDoc.DOC_DIV_CD.name] as String,
                    docNm = row[tstReqDoc.DOC_NM.name] as String,
                    docEngNm = row[tstReqDoc.DOC_ENG_NM.name] as String,
                    docFileId = row[tstReqDoc.DOC_FILE_ID.name] as String,
                    docEngFileId = row[tstReqDoc.DOC_ENG_FILE_ID.name] as String
                )
            }
            .awaitSingleOrNull()
    }

    private fun toTestItemEssentialDoc(row: Map<String, Any>): TestItemEssentialDoc {
        return TestItemEssentialDoc(
            itemEstlDocId = row["item_estl_doc_id"] as String?,
            tstCd = row["tst_cd"] as String,
            docCd = row["doc_cd"] as String,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
        )
    }

    // --- TestItemHst ---
    override suspend fun saveTestItemHistory(entity: TestItemHst): TestItemHst = testItemHstDataRepo.save(entity)
    override suspend fun findTestItemHistoryByTstCd(tstCd: String): Flow<TestItemHst> {
        val table = BtsItemHst.BTS_ITEM_HST
        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(table.TST_CD.eq(tstCd))
            .orderBy(table.UPDATE_DTIME.desc())

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toTestItemHst(row) }
            .asFlow()
    }

    private fun toTestItemHst(row: Map<String, Any>): TestItemHst {
        return TestItemHst(
            itemHstId = row["item_hst_id"] as String,
            hstDesc = row["hst_desc"] as String,
            tstCd = row["tst_cd"] as String,
            tstLargeCateCd = row["tst_large_cate_cd"] as String,
            tstMediumCateCd = row["tst_medium_cate_cd"] as String,
            startDt = row["start_dt"] as LocalDate,
            endDt = row["end_dt"] as LocalDate,
            useYn = row["use_yn"] as Boolean,
            reqPossYn = row["req_poss_yn"] as Boolean,
            webKorYn = row["web_kor_yn"] as Boolean,
            webEngYn = row["web_eng_yn"] as Boolean,
            tstNm = row["tst_nm"] as String,
            tstAbbrNm = row["tst_abbr_nm"] as String,
            tstEngNm = row["tst_eng_nm"] as String,
            tstEngAbbrNm = row["tst_eng_abbr_nm"] as String,
            tstIntNm = row["tst_int_nm"] as String?,
            rstTypeShortYn = row["rst_type_short_yn"] as Boolean,
            rstTypeLongYn = row["rst_type_long_yn"] as Boolean,
            rstTypeFileYn = row["rst_type_file_yn"] as Boolean,
            rstTypeUrlYn = row["rst_type_url_yn"] as Boolean,
            diseaseCd = row["disease_cd"] as String?,
            tstMethodCd = row["tst_method_cd"] as String?,
            refVal = row["ref_val"] as String?,
            engRefVal = row["eng_ref_val"] as String?,
            clncSgnf = row["clnc_sgnf"] as String?,
            engClncSgnf = row["eng_clnc_sgnf"] as String?,
            tstDesc = row["tst_desc"] as String?,
            tstEngDesc = row["tst_eng_desc"] as String?,
            tstDayweek = row["tst_dayweek"] as String?,
            tstTatday = row["tst_tatday"] as Int?,
            insuApplyCd = row["insu_apply_cd"] as String?,
            insuCd = row["insu_cd"] as String?,
            insuCateNo = row["insu_cate_no"] as String?,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_dtime"] as LocalDateTime
        )
    }

    // --- TestItemSpecimenHst ---
    override suspend fun saveTestItemSpecimenHistory(entity: TestItemSpecimenHst): TestItemSpecimenHst =
        testItemSpecimenHstDataRepo.save(entity)

    override suspend fun findTestItemSpecimenHistoryByTstCdAndSpcmCd(tstCd: String, spcmCd: String): Flow<TestItemSpecimenHst> {
        val table = BtsSpcmHst.BTS_SPCM_HST
        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(table.TST_CD.eq(tstCd).and(table.SPCM_CD.eq(spcmCd)))
            .orderBy(table.UPDATE_DTIME.desc())

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toTestItemSpecimenHst(row) }
            .asFlow()
    }

    private fun toTestItemSpecimenHst(row: Map<String, Any>): TestItemSpecimenHst {
        return TestItemSpecimenHst(
            spcmHstId = row["spcm_hst_id"] as String,
            hstDesc = row["hst_desc"] as String,
            tstCd = row["tst_cd"] as String,
            spcmCd = row["spcm_cd"] as String,
            sortOrder = (row["sort_order"] as Number).toInt(),
            estlYn = row["estl_yn"] as Boolean,
            takeQnty = row["take_qnty"] as String,
            engTakeQnty = row["eng_take_qnty"] as String,
            useQnty = row["use_qnty"] as String,
            engUseQnty = row["eng_use_qnty"] as String,
            strgMethod = row["strg_method"] as String,
            engStrgMethod = row["eng_strg_method"] as String,
            spcmStbl = row["spcm_stbl"] as String?,
            engSpcmStbl = row["eng_spcm_stbl"] as String?,
            takeMethod = row["take_method"] as String?,
            engTakeMethod = row["eng_take_method"] as String?,
            spcmDesc = row["spcm_desc"] as String,
            engDesc = row["eng_desc"] as String?,
            caution = row["caution"] as String,
            engCaution = row["eng_caution"] as String,
            spcmCntnCd = row["spcm_cntn_cd"] as String,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_dtime"] as LocalDateTime
        )
    }

}
