package com.idrsys.ailis.sales.adapter.repository.custLogs

import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustMstHst
import com.idrsys.ailis.sales.application.required.repository.custLogs.CustLogsCustomRepository
import com.idrsys.ailis.sales.domain.model.CustMstHst
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_MST_HST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CustLogsCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : CustLogsCustomRepository {

    override suspend fun findAllByCustMstId(custMstId: String): Flow<CustMstHst> {
        val query = dslContext.selectFrom(SCS_CUST_MST_HST)
            .where(SCS_CUST_MST_HST.CUST_MST_ID.eq(custMstId))
            .orderBy(SCS_CUST_MST_HST.CUST_MST_HST_ID.desc())

        var statement = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            statement = statement.bind(index, value!!)
        }

        return statement
            .map { row, _ -> row.toCustMstHst() }
            .all()
            .asFlow()
    }

    override suspend fun findDiffCustLogByCustMstHstId(custMstHstId: Long): Flow<CustMstHst> {
        val hst = SCS_CUST_MST_HST

        val currentLogQuery = dslContext.selectFrom(hst)
            .where(hst.CUST_MST_HST_ID.eq(custMstHstId))

        var currentLogStatement = databaseClient.sql(currentLogQuery.sql)
        currentLogQuery.bindValues.forEachIndexed { index, value ->
            currentLogStatement = currentLogStatement.bind(index, value!!)
        }

        val currentLogMono = currentLogStatement
            .map { row, _ -> row.toCustMstHst() }
            .one()

        return currentLogMono.flatMapMany { currentLog ->
            val previousLogQuery = dslContext.selectFrom(hst)
                .where(hst.CUST_MST_ID.eq(currentLog.custMstId))
                .and(hst.CUST_MST_HST_ID.lt(currentLog.custMstHstId!!))
                .orderBy(hst.CUST_MST_HST_ID.desc())
                .limit(1)

            var previousLogStatement = databaseClient.sql(previousLogQuery.sql)
            previousLogQuery.bindValues.forEachIndexed { index, value ->
                previousLogStatement = previousLogStatement.bind(index, value!!)
            }

            val previousLogMono = previousLogStatement
                .map { row, _ -> row.toCustMstHst() }
                .one()
            Flux.concat(previousLogMono, Mono.just(currentLog))
        }.asFlow()
    }
}