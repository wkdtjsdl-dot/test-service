package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.application.dto.request.estimate.EstimateSearchParam
import com.idrsys.ailis.sales.application.dto.response.EstimateItemResponse
import com.idrsys.ailis.sales.application.dto.response.EstimateResponse
import com.idrsys.ailis.sales.application.required.repository.estimate.EstimateRepository
import com.idrsys.ailis.sales.domain.model.Estimate
import com.idrsys.ailis.sales.generated.jooq.SalesScm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class EstimateRepositoryImpl(
    private val estimateDataRepository: EstimateDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : EstimateRepository {

    // Basic CRUD operations (delegated to Spring Data R2DBC)
    override suspend fun save(estimate: Estimate): Estimate {
        return estimateDataRepository.save(estimate)
    }

    override suspend fun findById(id: String): Estimate? {
        return estimateDataRepository.findById(id)
    }

    override suspend fun delete(estimate: Estimate) {
        estimateDataRepository.delete(estimate)
    }

    // Custom query operations (implemented with jOOQ)
    override fun findEstimates(searchParam: EstimateSearchParam): Flow<EstimateResponse> {
        // TODO: Implement with jOOQ when needed
        val table = SalesScm.SALES_SCM.SBL_ESTIMATE
        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(table.PUBL_DT.between(searchParam.startDt, searchParam.endDt)
                .and(searchParam.receiver?.let { table.RECP_PERSON.like(it) })
                .and(searchParam.reference?.let { table.REF_PERSON.like(it) }))
        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
           .fetch()
           .all()
           .map { rowToEstimate(it) }
           .asFlow()
    }

    private fun rowToEstimate(row: Map<String, Any>): EstimateResponse {
        return  EstimateResponse(
            estimateId = row["estimate_id"] as String,

            docType = row["doc_type_nm"] as String,
            docNo = row["doc_no"] as String,
            regDt = row["publ_dt"] as LocalDate,
            title = row["doc_title"] as String,

            receiver = row["recp_person"] as? String,
            reference = row["ref_person"] as? String,
            writerEmpNo = row["worker"] as? String,
            deptCd = row["work_dept"] as? String,
            remark = row["remark"] as? String,
            note = row["spnote"] as? String,

            totalSupval = row["supval"] as? BigDecimal ?: BigDecimal.ZERO,
            totalAddtax = row["addtax"] as? BigDecimal ?: BigDecimal.ZERO,
            totalAmt = row["demand_charge"] as? BigDecimal ?: BigDecimal.ZERO,

            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            items = (row["items"] as? List<EstimateItemResponse>) ?: emptyList()
        )
    }


    override suspend fun countEstimates(searchParam: EstimateSearchParam): Long {
        // TODO: Implement with jOOQ when needed
        return 0L
    }

    override suspend fun findEstimateById(estimateId: String): Estimate? {
        return estimateDataRepository.findById(estimateId)
    }

    override suspend fun generateDocNo(year: Int, docType: String): String {
        // TODO: Implement with jOOQ when needed
        return ""
    }
}