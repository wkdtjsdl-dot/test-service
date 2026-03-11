package com.idrsys.ailis.sales.adapter.repository.custatchfile

import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustAtchFileQuery
import com.idrsys.ailis.sales.application.dto.query.CustAtchFileQuery
import com.idrsys.ailis.sales.application.required.repository.custatchfile.CustAtchFileCustomRepository
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_ATCH_FILE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class CustAtchFileCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : CustAtchFileCustomRepository {

    override fun findAllByCustMstId(custMstId: String): Flow<CustAtchFileQuery> {
        val query = dslContext.selectFrom(SCS_CUST_ATCH_FILE)
            .where(SCS_CUST_ATCH_FILE.USE_YN.isTrue)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustAtchFileQuery() }.all().asFlow()
    }
}
