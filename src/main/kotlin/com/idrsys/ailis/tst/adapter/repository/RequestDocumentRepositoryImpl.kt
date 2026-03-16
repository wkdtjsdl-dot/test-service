package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.required.repository.RequestDocumentRepository
import com.idrsys.ailis.tst.domain.model.RequestDocument
import com.idrsys.ailis.tst.generated.jooq.tables.BbsTstReqDoc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface RequestDocumentDataRepository : CoroutineCrudRepository<RequestDocument, String>

@Repository
class RequestDocumentRepositoryImpl(
    private val requestDocumentDataRepository: RequestDocumentDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : RequestDocumentRepository {

    override suspend fun save(requestDocument: RequestDocument): RequestDocument {
        return requestDocumentDataRepository.save(requestDocument)
    }

    override suspend fun findById(id: String): RequestDocument? {
        return requestDocumentDataRepository.findById(id)
    }

    override suspend fun deleteById(id: String) {
        requestDocumentDataRepository.deleteById(id)
    }

    override suspend fun findAllByDocDivCd(docDivCd: String?): Flow<RequestDocument> {
        val table = BbsTstReqDoc.BBS_TST_REQ_DOC
        val query = dslContext.select(table.fields().toList()).from(table)

        if (docDivCd != null) {
            query.where(table.DOC_DIV_CD.eq(docDivCd))
        }

        query.orderBy(table.DOC_CD)

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
            .map { row -> toRequestDocument(row) }
            .asFlow()
    }

    private fun toRequestDocument(row: Map<String, Any>): RequestDocument {
        return RequestDocument(
            docCd = row["doc_cd"] as String,
            docDivCd = row["doc_div_cd"] as String,
            docNm = row["doc_nm"] as String,
            docEngNm = row["doc_eng_nm"] as String,
            docFileId = row["doc_file_id"] as String?,
            docEngFileId = row["doc_eng_file_id"] as String?,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_dtime"] as LocalDateTime
        )
    }
}
