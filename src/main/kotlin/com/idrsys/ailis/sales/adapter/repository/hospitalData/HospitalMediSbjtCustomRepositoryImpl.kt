package com.idrsys.ailis.sales.adapter.repository.hospitalData

import com.idrsys.ailis.sales.adapter.persistence.mapper.toHospitalMediSbjt
import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalMediSbjtCustomRepository
import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_HOSP_MEDI_SBJT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class HospitalMediSbjtCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : HospitalMediSbjtCustomRepository {
    override fun getHospitalMediSbjt(careInstId: String): Flow<HospitalMediSbjt> {
        val query = dslContext
            .select(*SCS_HOSP_MEDI_SBJT.fields())
            .from(SCS_HOSP_MEDI_SBJT)
            .where(SCS_HOSP_MEDI_SBJT.CARE_INST_ID.eq(careInstId))

        return databaseClient.sql(query.sql)
            .bind(0, careInstId)
            .map { row, _ -> row.toHospitalMediSbjt() }
            .all()
            .asFlow()
    }
}
