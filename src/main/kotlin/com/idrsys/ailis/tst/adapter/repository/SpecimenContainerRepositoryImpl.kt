package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.required.SpecimenContainerRepository
import com.idrsys.ailis.tst.domain.model.SpecimenContainer
import com.idrsys.ailis.tst.generated.jooq.tables.BbsSpcmCntn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.jooq.impl.DSL.lower
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface SpecimenContainerDataRepository : CoroutineCrudRepository<SpecimenContainer, String>

@Repository
class SpecimenContainerRepositoryImpl(
    private val specimenContainerDataRepository: SpecimenContainerDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : SpecimenContainerRepository {

    override suspend fun save(specimenContainer: SpecimenContainer): SpecimenContainer {
        return specimenContainerDataRepository.save(specimenContainer)
    }

    override suspend fun findById(id: String): SpecimenContainer? {
        return specimenContainerDataRepository.findById(id)
    }

    override suspend fun deleteById(id: String) {
        specimenContainerDataRepository.deleteById(id)
    }

    override suspend fun findAllByCntnNm(cntnNm: String?): Flow<SpecimenContainer> {
        val table = BbsSpcmCntn.BBS_SPCM_CNTN
        val query = dslContext.select(table.fields().toList()).from(table)

        if (!cntnNm.isNullOrBlank()) {
            query.where(
                lower(table.CNTN_NM).like("%${cntnNm.lowercase()}%")
            )
        }
        
        query.orderBy(table.SPCM_CNTN_CD)

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
            .map { row -> toSpecimenContainer(row) }
            .asFlow()
    }

    private fun toSpecimenContainer(row: Map<String, Any>): SpecimenContainer {
        return SpecimenContainer(
            spcmCntnCd = row["spcm_cntn_cd"] as String,
            cntnNm = row["cntn_nm"] as String,
            cntnEngNm = row["cntn_eng_nm"] as String,
            cntnFileId = row["cntn_file_id"] as String?,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_dtime"] as LocalDateTime
        )
    }
}
