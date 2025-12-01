package com.idrsys.ailis.sales.adapter.repository.hospitalData

import com.idrsys.ailis.sales.adapter.persistence.mapper.toHospitalMediSbjt
import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalMediSbjtCustomRepository
import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_HOSP_MEDI_SBJT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
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

    override suspend fun findByCareInstIdAndDgsbjtCd(careInstId: String, dgsbjtCd: String): HospitalMediSbjt? {
        val query = dslContext.selectFrom(SCS_HOSP_MEDI_SBJT)
            .where(SCS_HOSP_MEDI_SBJT.CARE_INST_ID.eq(careInstId))
            .and(SCS_HOSP_MEDI_SBJT.MEDI_SBJT_CD.eq(dgsbjtCd))

        return databaseClient.sql(query.sql)
            .bind(0, careInstId)
            .bind(1, dgsbjtCd)
            .map { row, _ -> row.toHospitalMediSbjt() }
            .first()
            .awaitSingleOrNull()
    }

    override fun findAllMediSbjtCdsByCareInstId(careInstId: String): Flow<String> {
        val query = dslContext.select(SCS_HOSP_MEDI_SBJT.MEDI_SBJT_CD)
            .from(SCS_HOSP_MEDI_SBJT)
            .where(SCS_HOSP_MEDI_SBJT.CARE_INST_ID.eq(careInstId))

        return databaseClient.sql(query.sql)
            .bind(0, careInstId)
            .map { row -> row.get(SCS_HOSP_MEDI_SBJT.MEDI_SBJT_CD.name) as String }
            .all()
            .asFlow()
    }

    override suspend fun deleteByCareInstIdAndMediSbjtCdNotIn(careInstId: String, mediSbjtCds: List<String>): Int {
        val query = if (mediSbjtCds.isEmpty()) {
            dslContext.deleteFrom(SCS_HOSP_MEDI_SBJT)
                .where(SCS_HOSP_MEDI_SBJT.CARE_INST_ID.eq(careInstId))
        } else {
            dslContext.deleteFrom(SCS_HOSP_MEDI_SBJT)
                .where(SCS_HOSP_MEDI_SBJT.CARE_INST_ID.eq(careInstId))
                .and(SCS_HOSP_MEDI_SBJT.MEDI_SBJT_CD.notIn(mediSbjtCds))
        }
        return databaseClient.sql(query.sql)
            .bindValues(query.bindValues)
            .fetch()
            .rowsUpdated()
            .awaitSingleOrNull()?.toInt() ?: 0
    }
}
