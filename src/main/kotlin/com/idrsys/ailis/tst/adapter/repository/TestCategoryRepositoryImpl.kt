package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.required.TestCategoryRepository
import com.idrsys.ailis.tst.domain.model.TestCategory
import com.idrsys.ailis.tst.generated.jooq.tables.BbsTstCate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TestCategoryDataRepository : CoroutineCrudRepository<TestCategory, String>

@Repository
class TestCategoryRepositoryImpl(
    private val testCategoryDataRepository: TestCategoryDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : TestCategoryRepository {

    override suspend fun save(testCategory: TestCategory): TestCategory {
        return testCategoryDataRepository.save(testCategory)
    }

    override suspend fun findById(id: String): TestCategory? {
        return testCategoryDataRepository.findById(id)
    }

    override suspend fun deleteById(id: String) {
        testCategoryDataRepository.deleteById(id)
    }

    override fun findByLargeCateCd(largeCateCd: String, useYn: Boolean?): Flow<TestCategory> {
        val table = BbsTstCate.BBS_TST_CATE
        val condition = if (useYn != null){
            table.USE_YN.eq(useYn)
        } else {
            DSL.noCondition()
        }
        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(table.TST_LARGE_CATE_CD.eq(largeCateCd)).and(condition)
            .orderBy(table.SORT_ORDER)

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
            .map { row: Map<String, Any> -> toTestCategory(row) }
            .asFlow()
    }

    private fun toTestCategory(row: Map<String, Any>): TestCategory {
        return TestCategory(
            tstCateId = row["tst_cate_id"] as String,
            tstLargeCateCd = row["tst_large_cate_cd"] as String,
            tstMediumCateCd = row["tst_medium_cate_cd"] as String,
            cateNm = row["cate_nm"] as String,
            cateAbbrNm = row["cate_abbr_nm"] as String,
            cateEngNm = row["cate_eng_nm"] as String,
            cateEngAbbrNm = row["cate_eng_abbr_nm"] as String,
            useYn = row["use_yn"] as Boolean,
            sortOrder = (row["sort_order"] as Number).toInt(),
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_dtime"] as LocalDateTime
        )
    }
}
