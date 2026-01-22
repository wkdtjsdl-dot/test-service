package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.application.dto.request.collection.CollectionListSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CollectionSearchParam
import com.idrsys.ailis.sales.application.dto.response.CollectionBillListResponse
import com.idrsys.ailis.sales.application.required.repository.collection.CollectionBillRepository
import com.idrsys.ailis.sales.domain.model.CollectionBill
import com.idrsys.ailis.sales.generated.jooq.SalesScm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class CollectionBillRepositoryImpl(
    private val collectionBillDataRepository: CollectionBillDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : CollectionBillRepository {

    // Basic CRUD operations (delegated to Spring Data R2DBC)
    override suspend fun save(collectionBill: CollectionBill): CollectionBill {
        return collectionBillDataRepository.save(collectionBill)
    }

    override suspend fun findById(id: String): CollectionBill? {
        return collectionBillDataRepository.findById(id)
    }

    override suspend fun delete(collectionBill: CollectionBill) {
        collectionBillDataRepository.delete(collectionBill)
    }

    // Custom query operations (implemented with jOOQ)
    override suspend fun findCollectionBills(searchParam: CollectionListSearchParam): Flow<CollectionBillListResponse> {
        val cb = SalesScm.SALES_SCM.SBL_COLBILL
        val cm = SalesScm.SALES_SCM.SCS_CUST_MST
        val bd = SalesScm.SALES_SCM.SBL_BANK_DEPOSIT

        val query = dslContext
            .select(
                cb.COLBILL_ID,
                cb.CUST_CD,
                cb.COLBILL_DT,
                cb.PAY_METHOD_CD,
                cb.PAY_AMT,
                cb.CARD_PAY_ID,
                cb.BANK_DEPOSIT_ID,
                cb.CARD_COMP_CD,
                cb.CARD_COMP_NM,
                cb.CARD_APPR_NO,
                cb.CARD_NO,
                cb.CARD_BILL_NO,
                cb.INSTL_MONTH,
                cb.ACCOUNT_YEAR,
                cb.SURECP_SLSTMT_NO,
                cb.SALES_SLSTMT_NO,
                cb.ADVRECE_YN,
                cb.CLOSING_CD,
                cb.COLLEDGER_ID,
                cb.SEND_YN,
                cb.REMARK,
                cm.SAP_CUST_CD,
                cm.BZOFFI_CD,
                cm.CUST_NM,
                bd.ACCOUNT_NO,
            )
            .from(cb)
            .join(cm)
            .on(cb.CUST_CD.eq(cm.CUST_CD))
            .leftJoin(bd)
            .on(cb.BANK_DEPOSIT_ID.eq(bd.BANK_DEPOSIT_ID))
            .where(
                cb.COLBILL_DT.between(searchParam.startDt, searchParam.endDt)
                    .and(searchParam.custCd?.let { cm.CUST_CD.eq(it) } ?: DSL.noCondition())
                    .and(searchParam.bzoffiCd?.let { cm.BZOFFI_CD.eq(it) } ?: DSL.noCondition())
                    .and(searchParam.closingCd?.let { cb.CLOSING_CD.eq(it) } ?: DSL.noCondition())
            )

        var executeSpec = databaseClient.sql { query.sql }
        query.bindValues.forEachIndexed { index, value ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toCollectionBillListResponse(row) }
            .asFlow()
    }

    private fun toCollectionBillListResponse(row: Map<String, Any>): CollectionBillListResponse {
        return CollectionBillListResponse(
            colbillId = row["colbill_id"] as? String,
            custCd = row["cust_cd"] as String,
            colbillDt = row["colbill_dt"] as LocalDate,
            payMethodCd = row["pay_method_cd"] as String,
            payAmt = row["pay_amt"] as BigDecimal,
            cardPayId = row["card_pay_id"] as? String,
            bankDepositId = row["bank_deposit_id"] as? String,
            cardCompCd = row["card_comp_cd"] as? String,
            cardCompNm = row["card_comp_nm"] as? String,
            cardApprNo = row["card_appr_no"] as? String,
            cardNo = row["card_no"] as? String,
            cardBillNo = row["card_bill_no"] as? String,
            instlMonth = row["instl_month"] as? String,
            accountYear = row["account_year"] as? String,
            surecpSlstmtNo = row["surecp_slstmt_no"] as? String,
            salesSlstmtNo = row["sales_slstmt_no"] as? String,
            advreceYn = (row["advrece_yn"] as? String)?.let { it == "Y" || it == "true" } ?: false,
            closingCd = row["closing_cd"] as? String,
            colledgerId = row["colledger_id"] as? String,
            sendYn = (row["send_yn"] as? String)?.let { it == "Y" || it == "true" } ?: false,
            sapCustCd = row["sap_cust_cd"] as? String,
            bzoffiCd = row["bzoffi_cd"] as? String,
            custNm = row["cust_nm"] as? String,
            accountNo = row["account_no"] as? String,
            remark = row["remark"] as? String,
        )
    }

    override suspend fun countCollectionBills(searchParam: CollectionSearchParam): Long {
        // TODO: Implement with jOOQ when needed
        return 0L
    }

    override suspend fun findCollectionBillById(colbillId: String): CollectionBill? {
        return collectionBillDataRepository.findById(colbillId)
    }
}