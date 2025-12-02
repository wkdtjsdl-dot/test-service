package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.DeptTestItemCategoryResponse
import com.idrsys.ailis.tst.application.dto.request.DepartmentTestItemSearchParam
import com.idrsys.ailis.tst.application.required.DepartmentTestItemRepository
import com.idrsys.ailis.tst.domain.model.DepartmentGroup
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItem
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItemTest
import com.idrsys.ailis.tst.domain.model.DepartmentTestItem
import com.idrsys.ailis.tst.generated.jooq.tables.BbsDeptGrpItm
import com.idrsys.ailis.tst.generated.jooq.tables.BbsDeptGrpItmTst
import com.idrsys.ailis.tst.generated.jooq.tables.BbsDeptTstItem
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

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
    override suspend fun findGroupById(id: String): DepartmentGroup? = groupDataRepo.findById(id)
    override suspend fun deleteGroupById(id: String) = groupDataRepo.deleteById(id)
    override suspend fun findAllGroups(): Flow<DepartmentGroup> = groupDataRepo.findAll()

    // --- DepartmentGroupItem ---
    override suspend fun saveGroupItem(entity: DepartmentGroupItem): DepartmentGroupItem = groupItemDataRepo.save(entity)
    override suspend fun findGroupItemById(id: String): DepartmentGroupItem? = groupItemDataRepo.findById(id)
    override suspend fun deleteGroupItemById(id: String) = groupItemDataRepo.deleteById(id)

    override suspend fun findGroupItemsByDeptCd(deptCd: String): Flow<DepartmentGroupItem> {
        val table = BbsDeptGrpItm.BBS_DEPT_GRP_ITM
        return dslContext.select(table.fields().toList())
            .from(table)
            .where(table.DEPT_CD.eq(deptCd))
            .asFlow()
            .map { r: Record -> r.into(DepartmentGroupItem::class.java) }
    }

    // --- DepartmentGroupItemTest ---
    override suspend fun saveGroupItemTest(entity: DepartmentGroupItemTest): DepartmentGroupItemTest = groupItemTestDataRepo.save(entity)
    override suspend fun findGroupItemTestById(id: String): DepartmentGroupItemTest? = groupItemTestDataRepo.findById(id)
    override suspend fun deleteGroupItemTestById(id: String) = groupItemTestDataRepo.deleteById(id)

    override suspend fun findGroupItemTestsByDeptCd(deptCd: String): Flow<DepartmentGroupItemTest> {
        val table = BbsDeptGrpItmTst.BBS_DEPT_GRP_ITM_TST
        return dslContext.select(table.fields().toList())
            .from(table)
            .where(table.DEPT_CD.eq(deptCd))
            .asFlow()
            .map { r: Record -> r.into(DepartmentGroupItemTest::class.java) }
    }

    // --- DepartmentTestItem ---
    override suspend fun saveTestItem(entity: DepartmentTestItem): DepartmentTestItem = testItemDataRepo.save(entity)
    override suspend fun findTestItemById(id: String): DepartmentTestItem? = testItemDataRepo.findById(id)
    override suspend fun deleteTestItemById(id: String) = testItemDataRepo.deleteById(id)

    override suspend fun findTestItemsByDeptCd(searchParam: DepartmentTestItemSearchParam): Flow<DeptTestItemCategoryResponse> {
        val testItem = BbsDeptTstItem.BBS_DEPT_TST_ITEM
        val item = BtsItem.BTS_ITEM

        var condition = testItem.DEPT_CD.eq(searchParam.deptCd)
            .and(item.USE_YN.isTrue)

        searchParam.tstLargeCateCd?.let {
            condition = condition.and(item.TST_LARGE_CATE_CD.eq(it))
        }
        searchParam.tstMediumCateCd?.let {
            condition = condition.and(item.TST_MEDIUM_CATE_CD.eq(it))
        }

        val query = dslContext.select(
            item.TST_LARGE_CATE_CD,
            item.TST_MEDIUM_CATE_CD,
            testItem.DEPT_TST_ITEM_ID,
            testItem.DEPT_CD,
            testItem.TST_CD,
            testItem.DAN_DIV_CD,
            testItem.TST_DAYWEEK,
            testItem.TST_TATDAY,
            testItem.DEPT_TST_DESC,
        )
            .from(testItem)
            .join(item).on(item.TST_CD.eq(testItem.TST_CD))
            .where(condition)

        // SQL 스트링 생성
        val sql = dslContext.renderInlined(query)

        return databaseClient.sql(sql)
            .map { row, _ ->
                DeptTestItemCategoryResponse(
                    tstLargeCateCd = row.get(item.TST_LARGE_CATE_CD.name, String::class.java)!!,
                    tstMediumCateCd = row.get(item.TST_MEDIUM_CATE_CD.name, String::class.java)!!,
                    deptTstItemId = row.get(testItem.DEPT_TST_ITEM_ID.name, String::class.java)!!,
                    deptCd = row.get(testItem.DEPT_CD.name, String::class.java)!!,
                    tstCd = row.get(testItem.TST_CD.name, String::class.java)!!,
                    danDivCd = row.get(testItem.DAN_DIV_CD.name, String::class.java)!!,
                    tstDayweek = row.get(testItem.TST_DAYWEEK.name, String::class.java)!!,
                    tstTatday = row.get(testItem.TST_TATDAY.name, Integer::class.java)!!.toInt(),
                    deptTstDesc = row.get(testItem.DEPT_TST_DESC.name, String::class.java),
                )
            }
            .all()
            .asFlow()
    }
}
