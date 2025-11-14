package com.idrsys.ailis.sales.adapter.repository.custreqposststitem

import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustReqPossTstItemQuery
import com.idrsys.ailis.sales.application.dto.query.CustReqPossTstItemQuery
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemSearchParam
import com.idrsys.ailis.sales.application.required.repository.custreqposststitem.CustReqPossTstItemCustomRepository
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_REQ_POSS_TST_ITEM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class CustReqPossTstItemCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : CustReqPossTstItemCustomRepository {

    override fun findAllByCustMstId(searchParam: CustReqPossTstItemSearchParam): Flow<CustReqPossTstItemQuery> {
        val query = dslContext.selectFrom(SCS_CUST_REQ_POSS_TST_ITEM)
            .where(SCS_CUST_REQ_POSS_TST_ITEM.CUST_MST_ID.eq(searchParam.custMstId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustReqPossTstItemQuery() }.all().asFlow()
    }
}
