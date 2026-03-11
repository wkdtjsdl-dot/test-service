package com.idrsys.ailis.sales.adapter.repository.ifConfInfo

import com.idrsys.ailis.sales.adapter.persistence.mapper.toIfConfInfoQuery
import com.idrsys.ailis.sales.application.dto.query.IfConfInfoQuery
import com.idrsys.ailis.sales.application.required.repository.ifConfInfo.IfConfInfoCustomRepository
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_IF_CONF_INFO
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_IF_FIELD_INFO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class IfConfInfoCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : IfConfInfoCustomRepository {

    override fun findByIfCustInfoId(ifCustInfoId: String): Flow<IfConfInfoQuery> {
        val query = dslContext.select(
            SCS_IF_CONF_INFO.asterisk(),
            SCS_IF_FIELD_INFO.IF_FIELD_NM.`as`("if_field_nm")
        )
            .from(SCS_IF_CONF_INFO)
            .leftJoin(SCS_IF_FIELD_INFO).on(SCS_IF_CONF_INFO.IF_FIELD_INFO_ID.eq(SCS_IF_FIELD_INFO.IF_FIELD_INFO_ID))
            .where(SCS_IF_CONF_INFO.IF_CUST_INFO_ID.eq(ifCustInfoId))
            .orderBy(SCS_IF_CONF_INFO.COL_IDX.asc())

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toIfConfInfoQuery() }.all().asFlow()
    }
}
