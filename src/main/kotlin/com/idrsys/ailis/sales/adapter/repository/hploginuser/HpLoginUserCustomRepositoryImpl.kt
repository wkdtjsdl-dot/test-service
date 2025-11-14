package com.idrsys.ailis.sales.adapter.repository.hploginuser

import com.idrsys.ailis.sales.adapter.persistence.mapper.toHpLoginUserQuery
import com.idrsys.ailis.sales.application.dto.query.HpLoginUserQuery
import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserSearchParam
import com.idrsys.ailis.sales.application.required.repository.hploginuser.HpLoginUserCustomRepository
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_HP_LOGIN_USER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class HpLoginUserCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : HpLoginUserCustomRepository {

    override fun findAllByCustMstId(searchParam: HpLoginUserSearchParam): Flow<HpLoginUserQuery> {
        val query = dslContext.selectFrom(SCS_HP_LOGIN_USER)
            .where(SCS_HP_LOGIN_USER.CUST_MST_ID.eq(searchParam.custMstId))
            .and(SCS_HP_LOGIN_USER.USE_YN.isTrue)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toHpLoginUserQuery() }.all().asFlow()
    }
}
