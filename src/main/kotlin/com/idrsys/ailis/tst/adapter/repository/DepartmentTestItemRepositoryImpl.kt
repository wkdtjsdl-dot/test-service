package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.DepartmentGroupItemTestResponse
import com.idrsys.ailis.tst.application.dto.DepartmentGroupItemWithCount
import com.idrsys.ailis.tst.application.dto.DeptTestItemCategoryResponse
import com.idrsys.ailis.tst.application.dto.request.DepartmentGroupItemSearchParam
import com.idrsys.ailis.tst.application.dto.request.DepartmentGroupItemTestSearchParam
import com.idrsys.ailis.tst.application.dto.request.DepartmentTestItemSearchParam
import com.idrsys.ailis.tst.application.required.DepartmentTestItemRepository
import com.idrsys.ailis.tst.domain.model.DepartmentGroup
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItem
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItemTest
import com.idrsys.ailis.tst.domain.model.DepartmentTestItem
import com.idrsys.ailis.tst.generated.jooq.tables.BbsDeptGroup
import com.idrsys.ailis.tst.generated.jooq.tables.BbsDeptGrpItm
import com.idrsys.ailis.tst.generated.jooq.tables.BbsDeptGrpItmTst
import com.idrsys.ailis.tst.generated.jooq.tables.BbsDeptTstItem
import com.idrsys.ailis.tst.generated.jooq.tables.BbsTstCate
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface DepartmentGroupDataRepository : CoroutineCrudRepository<DepartmentGroup, String>

@Repository
interface DepartmentGroupItemDataRepository : CoroutineCrudRepository<DepartmentGroupItem, String>

@Repository
interface DepartmentGroupItemTestDataRepository : CoroutineCrudRepository<DepartmentGroupItemTest, String>

@Repository
interface DepartmentTestItemDataRepository : CoroutineCrudRepository<DepartmentTestItem, String>

@Repository
class DepartmentTestItemRepositoryImpl(
    private val groupDataRepo: DepartmentGroupDataRepository,
    private val groupItemDataRepo: DepartmentGroupItemDataRepository,
    private val groupItemTestDataRepo: DepartmentGroupItemTestDataRepository,
    private val testItemDataRepo: DepartmentTestItemDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : DepartmentTestItemRepository {

    // --- DepartmentGroup ---
    override suspend fun saveGroup(entity: DepartmentGroup): DepartmentGroup = groupDataRepo.save(entity)
    override suspend fun findGroupById(deptGroupId: String): DepartmentGroup? = groupDataRepo.findById(deptGroupId)
    override suspend fun deleteGroupById(deptGroupId: String) = groupDataRepo.deleteById(deptGroupId)
    override suspend fun findAllGroups(): Flow<DepartmentGroup> = groupDataRepo.findAll()
    override suspend fun findlGroupsByDeptCd(deptCd: String): Flow<DepartmentGroup> {
        val table = BbsDeptGroup.BBS_DEPT_GROUP

        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(table.DEPT_CD.eq(deptCd))

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            if (value != null) executeSpec = executeSpec.bind(index, value)
            else executeSpec = executeSpec.bindNull(index, String::class.java)
        }
        return executeSpec
            .fetch()
            .all()
            .map { row -> toDepartmentGroup(row) }
            .asFlow()
    }

    // --- DepartmentGroupItem ---
    override suspend fun saveGroupItem(entity: DepartmentGroupItem): DepartmentGroupItem = groupItemDataRepo.save(entity)
    override suspend fun findGroupItemById(deptGrpItmId: String): DepartmentGroupItem? = groupItemDataRepo.findById(deptGrpItmId)
    override suspend fun deleteGroupItemById(deptGrpItmId: String) = groupItemDataRepo.deleteById(deptGrpItmId)

    override suspend fun findGroupItemsByDeptCd(deptCd: String): Flow<DepartmentGroupItem> {
        val table = BbsDeptGrpItm.BBS_DEPT_GRP_ITM
        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(table.DEPT_CD.eq(deptCd))

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
            .map { row -> toDepartmentGroupItem(row) }
            .asFlow()
    }
    override suspend fun getGroupItems(search: DepartmentGroupItemSearchParam): Flow<DepartmentGroupItemWithCount> {
        val itm = BbsDeptGrpItm.BBS_DEPT_GRP_ITM
        val tstItm = BbsDeptGrpItmTst.BBS_DEPT_GRP_ITM_TST

        val query = dslContext
            .select(
                itm.DEPT_GRP_ITM_ID,
                itm.DEPT_CD,
                itm.TST_CATE_CD,
                itm.TST_CATE_ITEM_CD,
                itm.TST_CATE_ITEM_NM,
                itm.SORT_ORDER,
                DSL.count(tstItm.TST_CD).`as`("test_count")
            )
            .from(itm)
            .leftJoin(tstItm)
            .on(itm.DEPT_CD.eq(tstItm.DEPT_CD))
            .and(itm.TST_CATE_CD.eq(tstItm.TST_CATE_CD))
            .and(itm.TST_CATE_ITEM_CD.eq(tstItm.TST_CATE_ITEM_CD))
            .where(
                itm.DEPT_CD.eq(search.deptCd)
                    .and(itm.TST_CATE_CD.eq(search.tstCateCd))
            )
            .groupBy(
                itm.DEPT_GRP_ITM_ID,
                itm.DEPT_CD,
                itm.TST_CATE_CD,
                itm.TST_CATE_ITEM_CD,
                itm.TST_CATE_ITEM_NM,
                itm.SORT_ORDER
            )



        return databaseClient.sql(query.sql)
            .bind(0,search.deptCd)
            .bind(1, search.tstCateCd)
            .map { row, _ ->
                DepartmentGroupItemWithCount(
                    deptGrpItmId = row.get("dept_grp_itm_id", String::class.java)!!,
                    deptCd = row.get("dept_cd", String::class.java)!!,
                    tstCateCd = row.get("tst_cate_cd", String::class.java)!!,
                    tstCateItemCd = row.get("tst_cate_item_cd", String::class.java)!!,
                    tstCateItemNm = row.get("tst_cate_item_nm", String::class.java)!!,
                    sortOrder = row.get("sort_order", Integer::class.java)!!.toInt(),
                    testCount = row.get("test_count", java.lang.Long::class.java)?.toInt() ?: 0
                )
            }
            .all()
            .asFlow()

    }

    // --- DepartmentGroupItemTest ---
    override suspend fun saveGroupItemTest(entity: DepartmentGroupItemTest): DepartmentGroupItemTest = groupItemTestDataRepo.save(entity)
    override suspend fun deleteGroupItemTestById(deptGrpItmTstId: String) = groupItemTestDataRepo.deleteById(deptGrpItmTstId)

    override suspend fun findGroupItemTestsByDeptCd(
        searchParam: DepartmentGroupItemTestSearchParam
    ): Flow<DepartmentGroupItemTestResponse> {
        val table = BbsDeptGrpItmTst.BBS_DEPT_GRP_ITM_TST
        val joinTable = BtsItem.BTS_ITEM

        val query = dslContext
            .select(table.fields().toList() + joinTable.TST_NM.`as` ("tst_nm"))
            .from(table)
            .leftJoin(joinTable)
            .on(table.TST_CD.eq(joinTable.TST_CD))
            .where(
                table.DEPT_CD.eq(searchParam.deptCd)
                    .and(table.TST_CATE_CD.eq(searchParam.tstCateCd))
                    .and(table.TST_CATE_ITEM_CD.eq(searchParam.tstCateItmCd))
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
            .map { row -> toDepartmentGroupTestItemWithTstNm(row) }
            .asFlow()
    }

    private fun toDepartmentGroupTestItemWithTstNm(row: Map<String, Any>): DepartmentGroupItemTestResponse {
        return DepartmentGroupItemTestResponse(
            deptGrpItmTstId = row["dept_grp_itm_tst_id"] as String,
            deptCd = row["dept_cd"] as String,
            tstCateCd = row["tst_cate_cd"] as String,
            tstCateItemCd = row["tst_cate_item_cd"] as String,
            tstCd = row["tst_cd"] as String,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            tstNm = row["tst_nm"] as String
        )
    }


    // --- DepartmentTestItem ---
    override suspend fun saveTestItem(entity: DepartmentTestItem): DepartmentTestItem = testItemDataRepo.save(entity)
    override suspend fun findTestItemById(deptTstItemId: String): DepartmentTestItem? = testItemDataRepo.findById(deptTstItemId)
    override suspend fun deleteTestItemById(deptTstItemId: String) = testItemDataRepo.deleteById(deptTstItemId)

    override suspend fun findTestItemsByDeptCd(searchParam: DepartmentTestItemSearchParam): Flow<DeptTestItemCategoryResponse> {
        val testItem = BbsDeptTstItem.BBS_DEPT_TST_ITEM
        val item = BtsItem.BTS_ITEM
        val testCate = BbsTstCate.BBS_TST_CATE

        var condition = testItem.DEPT_CD.eq(searchParam.deptCd)
            .and(item.USE_YN.isTrue)

        searchParam.tstLargeCateCd?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(item.TST_LARGE_CATE_CD.eq(it))
        }
        searchParam.tstMediumCateCd?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(item.TST_MEDIUM_CATE_CD.eq(it))
        }

        val query = dslContext.select(
            item.TST_LARGE_CATE_CD,
            item.TST_MEDIUM_CATE_CD,
            testCate.CATE_NM,
            testItem.DEPT_TST_ITEM_ID,
            testItem.DEPT_CD,
            testItem.TST_CD,
            item.TST_NM,
            testItem.DAN_DIV_CD,
            testItem.TST_DAYWEEK,
            testItem.TST_TATDAY,
            testItem.DEPT_TST_DESC,
        )
            .from(testItem)
            .join(item).on(item.TST_CD.eq(testItem.TST_CD))
            .join(testCate).on(testCate.TST_MEDIUM_CATE_CD.eq(item.TST_MEDIUM_CATE_CD))
            .where(condition)

        // SQL 스트링 생성
        return databaseClient.sql(dslContext.renderInlined(query))
            .map { row, _ ->
                DeptTestItemCategoryResponse(
                    tstLargeCateCd = row.get(item.TST_LARGE_CATE_CD.name, String::class.java)!!,
                    tstMediumCateCd = row.get(item.TST_MEDIUM_CATE_CD.name, String::class.java)!!,
                    cateNm = row.get(testCate.CATE_NM.name, String::class.java)!!,
                    deptTstItemId = row.get(testItem.DEPT_TST_ITEM_ID.name, String::class.java)!!,
                    deptCd = row.get(testItem.DEPT_CD.name, String::class.java)!!,
                    tstCd = row.get(testItem.TST_CD.name, String::class.java)!!,
                    tstNm = row.get(item.TST_NM.name, String::class.java)!!,
                    danDivCd = row.get(testItem.DAN_DIV_CD.name, String::class.java)!!,
                    tstDayweek = row.get(testItem.TST_DAYWEEK.name, String::class.java)!!,
                    tstTatday = row.get(testItem.TST_TATDAY.name, Integer::class.java)!!.toInt(),
                    deptTstDesc = row.get(testItem.DEPT_TST_DESC.name, String::class.java),
                )
            }
            .all()
            .asFlow()
    }

    private fun toDepartmentGroup(row: Map<String, Any>): DepartmentGroup {
        return DepartmentGroup(
            deptGroupId = row["dept_group_id"] as String?,
            deptCd = row["dept_cd"] as String,
            tstCateCd = row["tst_cate_cd"] as String,
            tstCateNm = row["tst_cate_nm"] as String,
            updateAuthCd = row["update_auth_cd"] as String,
            dupAllowYn = row["dup_allow_yn"] as Boolean,
            sortOrder = (row["sort_order"] as Number).toInt(),
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_detime"] as LocalDateTime
        )
    }


    private fun toDepartmentGroupItem(row: Map<String, Any>): DepartmentGroupItem {
        return DepartmentGroupItem(
            deptGrpItmId = row["dept_grp_itm_id"] as String?,
            deptCd = row["dept_cd"] as String,
            tstCateCd = row["tst_cate_cd"] as String,
            tstCateItemCd = row["tst_cate_item_cd"] as String,
            tstCateItemNm = row["tst_cate_item_nm"] as String,
            sortOrder = (row["sort_order"] as Number).toInt(),
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_detime"] as LocalDateTime
        )
    }

    private fun toDepartmentGroupItemTest(row: Map<String, Any>): DepartmentGroupItemTest {
        return DepartmentGroupItemTest(
            deptGrpItmTstId = row["dept_grp_itm_tst_id"] as String?,
            deptCd = row["dept_cd"] as String,
            tstCateCd = row["tst_cate_cd"] as String,
            tstCateItemCd = row["tst_cate_item_cd"] as String,
            tstCd = row["tst_cd"] as String,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime
        )
    }

    private fun toDepartmentTestItem(row: Map<String, Any>): DepartmentTestItem {
        return DepartmentTestItem(
            deptTstItemId = row["dept_tst_item_id"] as String?,
            deptCd = row["dept_cd"] as String,
            tstCd = row["tst_cd"] as String,
            danDivCd = row["dan_div_cd"] as String,
            tstDayweek = row["tst_dayweek"] as String,
            tstTatday = (row["tst_tatday"] as Number).toInt(),
            deptTstDesc = row["dept_tst_desc"] as String?,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_detime"] as LocalDateTime
        )
    }
}
