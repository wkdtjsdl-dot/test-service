package com.idrsys.ailis.sales.adapter.repository.ifFieldInfo

import com.idrsys.ailis.sales.adapter.persistence.mapper.toIfFieldInfoAutoCompleteResponse
import com.idrsys.ailis.sales.adapter.persistence.mapper.toIfFieldInfoQuery
import com.idrsys.ailis.sales.application.dto.query.IfFieldInfoQuery
import com.idrsys.ailis.sales.application.dto.request.ifFieldInfo.IfFieldInfoAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoAutoCompleteResponse
import com.idrsys.ailis.sales.application.required.repository.ifFieldInfo.IfFieldInfoCustomRepository
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_IF_FIELD_INFO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class IfFieldInfoCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : IfFieldInfoCustomRepository {

    override fun findAllIfFieldInfos(): Flow<IfFieldInfoQuery> {
        val query = dslContext.select(
            SCS_IF_FIELD_INFO.asterisk()
        )
            .from(SCS_IF_FIELD_INFO)
            .orderBy(SCS_IF_FIELD_INFO.IF_FIELD_NM.asc())

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toIfFieldInfoQuery() }.all().asFlow()
    }

    override fun findIfFieldInfoAutoCompleteList(searchParam: IfFieldInfoAutoCompleteSearchParam): Flow<IfFieldInfoAutoCompleteResponse> {
        val baseQuery = dslContext.select(
            SCS_IF_FIELD_INFO.IF_FIELD_INFO_ID,
            SCS_IF_FIELD_INFO.IF_FIELD_NM
        )
            .from(SCS_IF_FIELD_INFO)

        // 필드명 검색 조건에 따라 쿼리 빌드
        val query = if (searchParam.ifFieldNm != null) {
            baseQuery.where(SCS_IF_FIELD_INFO.IF_FIELD_NM.likeIgnoreCase("%${searchParam.ifFieldNm}%"))
                .orderBy(SCS_IF_FIELD_INFO.IF_FIELD_NM.asc())
                .limit(50)
        } else {
            baseQuery.orderBy(SCS_IF_FIELD_INFO.IF_FIELD_NM.asc())
                .limit(50)
        }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toIfFieldInfoAutoCompleteResponse() }.all().asFlow()
    }
}
