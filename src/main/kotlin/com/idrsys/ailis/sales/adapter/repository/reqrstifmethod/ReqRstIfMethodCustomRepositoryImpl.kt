package com.idrsys.ailis.sales.adapter.repository.reqrstifmethod

import com.idrsys.ailis.sales.adapter.persistence.mapper.toReqRstIfMethod
import com.idrsys.ailis.sales.adapter.persistence.mapper.toReqRstIfMethodQuery
import com.idrsys.ailis.sales.application.dto.query.ReqRstIfMethodQuery
import com.idrsys.ailis.sales.application.dto.request.reqrstifmethod.ReqRstIfMethodSearchParam
import com.idrsys.ailis.sales.application.required.repository.reqrstifmethod.ReqRstIfMethodCustomRepository
import com.idrsys.ailis.sales.domain.model.ReqRstIfMethod
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_REQ_RST_IF_METHOD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.DSLContext
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ReqRstIfMethodCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : ReqRstIfMethodCustomRepository {

    override fun findAllByCustMstId(searchParam: ReqRstIfMethodSearchParam): Flow<ReqRstIfMethodQuery> {
        val query = dslContext.selectFrom(SCS_REQ_RST_IF_METHOD)
            .where(SCS_REQ_RST_IF_METHOD.CUST_MST_ID.eq(searchParam.custMstId))
            .and(SCS_REQ_RST_IF_METHOD.USE_YN.isTrue)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toReqRstIfMethodQuery() }.all().asFlow()
    }

    override suspend fun findCurrentByCustMstId(custMstId: String): ReqRstIfMethod? {
        val query = dslContext.selectFrom(SCS_REQ_RST_IF_METHOD)
            .where(SCS_REQ_RST_IF_METHOD.CUST_MST_ID.eq(custMstId))
            .and(SCS_REQ_RST_IF_METHOD.USE_YN.isTrue)
            .and(SCS_REQ_RST_IF_METHOD.APPLY_END_DT.eq(LocalDate.parse("9999-12-31")))
            .orderBy(SCS_REQ_RST_IF_METHOD.APPLY_START_DT.desc())
            .limit(1)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toReqRstIfMethod() }.one().awaitFirstOrNull()
    }
}
