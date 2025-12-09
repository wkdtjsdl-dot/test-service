package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.DeptTestItemCategoryResponse
import com.idrsys.ailis.tst.application.dto.TestItemAutoCompleteParam
import com.idrsys.ailis.tst.application.dto.TestItemAutoCompleteResponse
import com.idrsys.ailis.tst.application.dto.TestItemSearchParam
import com.idrsys.ailis.tst.application.required.TestItemRepository
import com.idrsys.ailis.tst.domain.model.StandardCharge
import com.idrsys.ailis.tst.domain.model.TestItem
import com.idrsys.ailis.tst.domain.model.TestItemEssentialDoc
import com.idrsys.ailis.tst.domain.model.TestItemSpecimen
import com.idrsys.ailis.tst.domain.model.TestItemRefItem
import com.idrsys.ailis.tst.domain.model.TestItemGene
import com.idrsys.ailis.tst.generated.jooq.tables.BbsDeptTstItem
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItem
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItemEstlDoc
import com.idrsys.ailis.tst.generated.jooq.tables.BtsStndCharge
import com.idrsys.ailis.tst.generated.jooq.tables.BtsSpcm
import com.idrsys.ailis.tst.generated.jooq.tables.BtsRefItem
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItemGene
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
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
class TestItemRepositoryImpl(
    private val itemDataRepo: TestItemDataRepository,
    private val chargeDataRepo: StandardChargeDataRepository,
    private val specimenDataRepo: TestItemSpecimenDataRepository,
    private val refItemDataRepo: TestItemRefItemDataRepository,
    private val geneDataRepo: TestItemGeneDataRepository,
    private val essentialDocDataRepo: TestItemEssentialDocDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : TestItemRepository {

    // --- TestItem ---
    override suspend fun save(entity: TestItem): TestItem = itemDataRepo.save(entity)
    override suspend fun findById(tstCd: String): TestItem? = itemDataRepo.findById(tstCd)

    override fun getItems(searchParam: TestItemSearchParam): Flow<TestItem> {
        if (searchParam.deptCd.isNullOrBlank()) {
            return flowOf()
        }

        val deptTestItem = BbsDeptTstItem.BBS_DEPT_TST_ITEM
        val tstItem = BtsItem.BTS_ITEM

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
        searchParam.useYn?.let {
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

    override fun autoCompleteItems(searchParam: TestItemAutoCompleteParam): Flow<TestItemAutoCompleteResponse> {
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
                TestItemAutoCompleteResponse(
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

    override suspend fun findChargesByTestCd(tstCd: String): Flow<StandardCharge> {
        val table = BtsStndCharge.BTS_STND_CHARGE
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
            .map { row -> toStandardCharge(row) }
            .asFlow()
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
            tstIntNm = row["tst_int_nm"] as String,
            rstTypeShortYn = row["rst_type_short_yn"] as Boolean,
            rstTypeLongYn = row["rst_type_long_yn"] as Boolean,
            rstTypeFileYn = row["rst_type_file_yn"] as Boolean,
            rstTypeUrlYn = row["rst_type_url_yn"] as Boolean,
            diseaseCd = row["disease_cd"] as String,
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
            updateDetime = row["update_detime"] as LocalDateTime
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
            insuCharge = (row["insu_charge"] as Number).toDouble(),
            qladCharge = (row["qlad_charge"] as Number).toDouble(),
            stndCharge = (row["stnd_charge"] as Number).toDouble(),
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
            strgMethodCd = row["strg_method_cd"] as String,
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
            updateDetime = row["update_detime"] as LocalDateTime?
        )
    }

    // --- TestItemRefItem ---
    override suspend fun saveRefItem(entity: TestItemRefItem): TestItemRefItem = refItemDataRepo.save(entity)
    override suspend fun findRefItemById(refItemId: String): TestItemRefItem? = refItemDataRepo.findById(refItemId)
    override suspend fun deleteRefItemById(refItemId: String) = refItemDataRepo.deleteById(refItemId)

    override fun findRefItemsByTstCd(tstCd: String): Flow<TestItemRefItem> {
        val table = BtsRefItem.BTS_REF_ITEM
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
            .map { row -> toTestItemRefItem(row) }
            .asFlow()
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
            updateDetime = row["update_detime"] as LocalDateTime?
        )
    }

    // --- TestItemGene ---
    override suspend fun saveGene(entity: TestItemGene): TestItemGene = geneDataRepo.save(entity)
    override suspend fun deleteGeneById(itemGeneId: String) = geneDataRepo.deleteById(itemGeneId)

    override fun findGenesByTestCd(tstCd: String): Flow<TestItemGene> {
        val table = BtsItemGene.BTS_ITEM_GENE
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
            .map { row -> toTestItemGene(row) }
            .asFlow()
    }

    private fun toTestItemGene(row: Map<String, Any>): TestItemGene {
        return TestItemGene(
            itemGeneId = row["item_gene_id"] as String?,
            tstCd = row["tst_cd"] as String,
            geneCd = row["gene_cd"] as String,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime
        )
    }

    // --- TestItemEssentialDoc ---
    override suspend fun saveEssentialDoc(entity: TestItemEssentialDoc): TestItemEssentialDoc = essentialDocDataRepo.save(entity)
    override suspend fun findEssentialDocById(itemEstlDocId: String): TestItemEssentialDoc? = essentialDocDataRepo.findById(itemEstlDocId)
    override suspend fun deleteEssentialDocById(itemEstlDocId: String) = essentialDocDataRepo.deleteById(itemEstlDocId)

    override fun findEssentialDocsByTstCd(tstCd: String): Flow<TestItemEssentialDoc> {
        val table = BtsItemEstlDoc.BTS_ITEM_ESTL_DOC
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
            .map { row -> toTestItemEssentialDoc(row) }
            .asFlow()
    }

    private fun toTestItemEssentialDoc(row: Map<String, Any>): TestItemEssentialDoc {
        return TestItemEssentialDoc(
            itemEstlDocId = row["item_estl_doc_id"] as String?,
            tstCd = row["tst_cd"] as String,
            docCd = row["doc_cd"] as String,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String?,
            updateDetime = row["update_detime"] as LocalDateTime?
        )
    }
}
