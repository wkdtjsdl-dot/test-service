package com.idrsys.ailis.sales.adapter.repository.custaddinfo

import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustAddInfoQuery
import com.idrsys.ailis.sales.application.dto.query.CustAddInfoQuery
import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoSearchParam
import com.idrsys.ailis.sales.application.required.repository.custaddinfo.CustAddInfoCustomRepository
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_ADD_INFO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class CustAddInfoCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : CustAddInfoCustomRepository {

    override fun findAllByCustMstId(searchParam: CustAddInfoSearchParam): Flow<CustAddInfoQuery> {
        val query = dslContext.selectFrom(SCS_CUST_ADD_INFO)
            .where(SCS_CUST_ADD_INFO.CUST_MST_ID.eq(searchParam.custMstId))
            .and(SCS_CUST_ADD_INFO.USE_YN.isTrue)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustAddInfoQuery() }.all().asFlow()
    }
}
