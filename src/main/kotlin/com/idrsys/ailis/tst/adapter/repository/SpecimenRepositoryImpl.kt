package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.required.repository.SpecimenRepository
import com.idrsys.ailis.tst.domain.model.Specimen
import com.idrsys.ailis.tst.generated.jooq.tables.BbsSpcm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface SpecimenDataRepository : CoroutineCrudRepository<Specimen, String>

@Repository
class SpecimenRepositoryImpl(
    private val specimenDataRepository: SpecimenDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : SpecimenRepository {

    override suspend fun save(specimen: Specimen): Specimen {
        return specimenDataRepository.save(specimen)
    }

    override suspend fun findById(id: String): Specimen? {
        return specimenDataRepository.findById(id)
    }

    override suspend fun deleteById(id: String) {
        specimenDataRepository.deleteById(id)
    }

    override suspend fun findAll(spcmNm: String?, spcmCateCd: String?): Flow<Specimen> {
        val table = BbsSpcm.BBS_SPCM
        val query = dslContext.select(table.fields().toList()).from(table)

        if (spcmNm != null) {
            query.where(table.SPCM_NM.likeIgnoreCase("%$spcmNm%")).or(table.SPCM_ENG_NM.likeIgnoreCase("%$spcmNm%"))
        }
        if (spcmCateCd != null && spcmCateCd.isNotBlank()) {
            query.where(table.SPCM_CATE_CD.eq(spcmCateCd))
        }
        query.where(table.USE_YN.isTrue)
        query.orderBy(table.SPCM_CD)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toSpecimen(row) }
            .asFlow()
    }

    private fun toSpecimen(row: Map<String, Any>): Specimen {
        return Specimen(
            spcmCd = row["spcm_cd"] as String,
            spcmCateCd = row["spcm_cate_cd"] as String?,
            useYn = row["use_yn"] as Boolean,
            spcmNm = row["spcm_nm"] as String,
            spcmAbbrNm = row["spcm_abbr_nm"] as String?,
            spcmEngNm = row["spcm_eng_nm"] as String,
            spcmEngAbbrNm = row["spcm_eng_abbr_nm"] as String?,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_dtime"] as LocalDateTime
        )
    }
}
