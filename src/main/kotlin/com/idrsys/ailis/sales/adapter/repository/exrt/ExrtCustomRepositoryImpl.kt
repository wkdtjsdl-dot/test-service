package com.idrsys.ailis.sales.adapter.repository.exrt

import com.idrsys.ailis.sales.adapter.persistence.mapper.toExrt
import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtBatchCommand
import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtSearchParam
import com.idrsys.ailis.sales.application.required.repository.exrt.ExrtCustomRepository
import com.idrsys.ailis.sales.domain.model.Exrt
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_EXRT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Query
import org.jooq.SelectLimitStep
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ExrtCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : ExrtCustomRepository {

    override suspend fun batchInsert(commands: List<ExrtBatchCommand>, creator: String): Int {
        if (commands.isEmpty()) return 0

        val now = LocalDateTime.now()
        val query = dslContext.insertInto(SCS_EXRT)
            .columns(
                SCS_EXRT.STND_DT,
                SCS_EXRT.CRCY_CD,
                SCS_EXRT.STND_EXRT,
                SCS_EXRT.CREATOR,
                SCS_EXRT.CREATE_DTIME,
                SCS_EXRT.UPDATER,
                SCS_EXRT.UPDATE_DTIME
            )
            .apply {
                commands.forEach { cmd ->
                    values(
                        cmd.stndDt,
                        cmd.crcyCd,
                        cmd.stndExrt,
                        creator,
                        now,
                        creator,
                        now
                    )
                }
            }
            .onConflict(SCS_EXRT.STND_DT, SCS_EXRT.CRCY_CD)
            .doNothing()

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.fetch().rowsUpdated().awaitSingle().toInt()
    }

    override fun findExrts(searchParam: ExrtSearchParam, pageable: Pageable?): Flow<Exrt> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(SCS_EXRT.asterisk())
            .from(SCS_EXRT)
            .where(conditions)
            .orderBy(SCS_EXRT.STND_DT.desc(), SCS_EXRT.CRCY_CD.asc())
            .let { applyPaging(it, pageable ?: Pageable.unpaged()) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toExrt() }.all().asFlow()
    }

    override suspend fun countExrts(searchParam: ExrtSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SCS_EXRT)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingle()
    }

    override suspend fun findExrtById(exrtId: Long): Exrt? {
        val query = dslContext.select(SCS_EXRT.asterisk())
            .from(SCS_EXRT)
            .where(SCS_EXRT.EXRT_ID.eq(exrtId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toExrt() }.one().awaitSingleOrNull()
    }

    private fun applyPaging(q: SelectLimitStep<*>, pageable: Pageable?): Query {
        if (pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: ExrtSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()

        searchParam.stndDtFrom?.let {
            conds += SCS_EXRT.STND_DT.ge(it)
        }

        searchParam.stndDtTo?.let {
            conds += SCS_EXRT.STND_DT.le(it)
        }

        searchParam.crcyCd?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_EXRT.CRCY_CD.eq(it)
        }

        return conds
    }
}
