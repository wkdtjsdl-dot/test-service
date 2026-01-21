package com.idrsys.ailis.sales.application.service.billing

import com.idrsys.ailis.sales.application.dto.request.billing.CreateDemandCommand
import com.idrsys.ailis.sales.application.dto.request.billing.SendSalesStatementCommand
import com.idrsys.ailis.sales.application.dto.response.CancelDemandResponse
import com.idrsys.ailis.sales.application.dto.response.CreateDemandResponse
import com.idrsys.ailis.sales.application.dto.response.SendSalesStatementResponse
import com.idrsys.ailis.sales.application.required.port.ReqServicePort
import com.idrsys.ailis.sales.application.required.repository.billing.DemandRepository
import com.idrsys.ailis.sales.application.required.repository.collection.CollectionLedgerRepository
import com.idrsys.ailis.sales.application.usecase.billing.BillingCommandUseCase
import com.idrsys.ailis.sales.domain.model.CollectionLedger
import com.idrsys.ailis.sales.domain.model.Demand
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Billing Command Service
 *
 * Implements write operations for billing domain following TDD approach
 */
@Service
@Transactional
class BillingCommandService(
    private val demandRepository: DemandRepository,
    private val collectionLedgerRepository: CollectionLedgerRepository,
    private val reqServicePort: ReqServicePort,
) : BillingCommandUseCase {

    /**
     * Create demand (청구 마감)
     *
     * Business Rules:
     * 1. Create demand entity with provided data
     * 2. Create collection ledger entry (division code "0")
     * 3. Update all test requests with closing information
     *
     * Transaction boundary: All operations must succeed or rollback
     */
    override suspend fun createDemand(command: CreateDemandCommand, adminId: String): CreateDemandResponse {
        // 1. Validate customer exists
        // TODO: Add customer validation when customer repository is available

        // 2. Check for duplicate demand in the same period
        // TODO: Implement duplicate check when custom repository is available

        // 3. Calculate demand charge from command data
        val demandCharge = command.supval.add(command.addtax)

        // 4. Create demand entity
        val demand = Demand(
            demandId = null,
            demandDt = command.demandDt,
            demandStartDt = command.demandStartDt,
            demandStndDt = command.demandStndDt,
            custCd = command.custCd,
            stndPrice = command.stndPrice,
            supval = command.supval,
            addtax = command.addtax,
            demandCharge = demandCharge,
            dscntRate = command.dscntRate,
            insurePrice = command.insurePrice,
            sapCustCd = command.sapCustCd,
            invcRecpEmailAddr = command.invcRecpEmailAddr,
            demandMemo = command.demandMemo,
            creator = adminId,
            createDtime = LocalDateTime.now(),
            updater = adminId,
            updateDtime = LocalDateTime.now()
        )
        demand.setAsNew()

        val savedDemand = demandRepository.save(demand)

        // 5. Create collection ledger entry for demand
        val ledger = CollectionLedger.createForDemand(
            custCd = command.custCd,
            demandDt = command.demandDt,
            demandCharge = demandCharge,
            creator = adminId
        )

        val savedLedger = collectionLedgerRepository.save(ledger)

        // 6. Update test requests with closing information
        val createdRequestCount = reqServicePort.updateTstItemClosingInfo(
            directAcctCd = command.custCd,
            startDt = command.demandStartDt,
            endDt = command.demandStndDt,
            closingSupval = command.supval,
            closingAddtax = command.addtax,
            closingDemandCharge = demandCharge,
            exrtId = command.exrtId,
            closingMemo = command.demandMemo,
            closingUser = adminId
        )

        // 7. Return response
        return CreateDemandResponse(
            demandId = savedDemand.demandId.toString(),
            custCd = savedDemand.custCd,
            demandDt = savedDemand.demandDt,
            demandCharge = savedDemand.demandCharge,
            colledgerId = savedLedger.colledgerId.toString(),
            createdRequestCount = createdRequestCount
        )
    }

    /**
     * Cancel demand (청구 취소)
     *
     * Business Rules:
     * 1. Demand can only be cancelled before ERP statement is sent
     * 2. Delete demand entity and associated ledger
     * 3. Release all test requests (reset closing information)
     *
     * Transaction boundary: All operations must succeed or rollback
     */
    override suspend fun cancelDemand(demandId: String, adminId: String): CancelDemandResponse {
        // 1. Find demand
        val demand = demandRepository.findById(demandId)
            ?: throw UserDefinedException("DEMAND_NOT_FOUND", "청구서를 찾을 수 없습니다: $demandId")

        // 2. Check if cancellation is allowed
        if (!demand.canCancel()) {
            throw IllegalArgumentException(
                "ERP 매출전표가 생성된 청구서는 취소할 수 없습니다: ${demand.slstmtNo}"
            )
        }

        // 3. Delete associated collection ledger
        // TODO: Implement ledger deletion when custom repository method is available
        val ledgers = collectionLedgerRepository.findByCustCdAndColbillDtBetweenOrderByColbillDtAsc(
            demand.custCd,
            demand.demandDt,
            demand.demandDt
        ).toList()

        for (ledger in ledgers) {
            if (ledger.colbillDivCd == "0") { // Only delete demand ledgers
                collectionLedgerRepository.delete(ledger)
            }
        }

        // 4. Release test requests (reset closing information)
        val releasedRequestCount = reqServicePort.releaseTstItemClosingInfo(
            directAcctCd = demand.custCd,
            startDt = demand.demandStartDt,
            endDt = demand.demandStndDt,
            updater = adminId
        )

        // 5. Delete demand
        demandRepository.delete(demand)

        // 6. Return response
        return CancelDemandResponse(
            demandId = demandId,
            cancelled = true,
            releasedRequestCount = releasedRequestCount
        )
    }

    /**
     * Send sales statement to ERP (매출전표 생성)
     *
     * Business Rules:
     * 1. Statement can only be sent once
     * 2. After sending, demand cannot be cancelled
     * 3. ERP integration may use saga pattern for compensation
     *
     * Transaction boundary: Single transaction with ERP call
     */
    override suspend fun sendSalesStatement(
        command: SendSalesStatementCommand,
        adminId: String
    ): SendSalesStatementResponse {
        // 1. Find demand
        val demand = demandRepository.findById(command.demandId)
            ?: throw UserDefinedException("DEMAND_NOT_FOUND", "청구서를 찾을 수 없습니다: ${command.demandId}")

        // 2. Check if already sent
        if (demand.slstmtNo != null) {
            throw UserDefinedException(
                "INVALID_OPERATION",
                "이미 ERP 매출전표가 생성되었습니다: ${demand.slstmtNo}"
            )
        }

        // 3. Call ERP integration service
        // TODO: Implement ERP integration service call
        val slstmtNo = "SL-${java.time.LocalDate.now().year}-${demand.demandId.toString().substring(0, 8)}"

        // 4. Update demand with statement number
        demand.sendSalesStatement(slstmtNo, adminId)
        val updatedDemand = demandRepository.save(demand)

        // 5. Return response
        return SendSalesStatementResponse(
            demandId = updatedDemand.demandId.toString(),
            slstmtNo = updatedDemand.slstmtNo!!,
            slstmtSendDt = updatedDemand.slstmtSendDt!!,
            sentToErp = true
        )
    }
}
