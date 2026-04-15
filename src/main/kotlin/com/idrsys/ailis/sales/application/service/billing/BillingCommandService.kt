package com.idrsys.ailis.sales.application.service.billing

import com.idrsys.ailis.sales.application.dto.request.billing.CreateDemandCommand
import com.idrsys.ailis.sales.application.dto.request.billing.SapInvcPostingRow
import com.idrsys.ailis.sales.application.dto.request.billing.SendSalesStatementBatchCommand
import com.idrsys.ailis.sales.application.dto.response.CancelDemandResponse
import com.idrsys.ailis.sales.application.dto.response.CreateDemandResponse
import com.idrsys.ailis.sales.application.dto.response.SendSalesStatementBatchResponse
import com.idrsys.ailis.sales.application.dto.response.SendSalesStatementBatchResult
import com.idrsys.ailis.sales.application.required.external.ReqServicePort
import com.idrsys.ailis.sales.application.required.repository.billing.DemandHstRepository
import com.idrsys.ailis.sales.application.required.repository.billing.DemandRepository
import com.idrsys.ailis.sales.application.required.repository.collection.CollectionLedgerRepository
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
import com.idrsys.ailis.sales.application.required.sap.InvoiceErpPort
import com.idrsys.ailis.sales.application.usecase.billing.BillingCommandUseCase
import com.idrsys.ailis.sales.domain.model.CollectionLedger
import com.idrsys.ailis.sales.domain.model.Demand
import com.idrsys.ailis.sales.domain.model.DemandHst
import com.idrsys.web.exception.UserDefinedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Billing Command Service
 *
 * Implements write operations for billing domain following TDD approach
 */
@Service
@Transactional
class BillingCommandService(
    private val demandRepository: DemandRepository,
    private val demandHstRepository: DemandHstRepository,
    private val collectionLedgerRepository: CollectionLedgerRepository,
    private val reqServicePort: ReqServicePort,
    private val custCustomRepository: CustCustomRepository,
    private val invoiceErpPort: InvoiceErpPort,
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
        // 1. 고객 조회 및 SAP 거래처 코드 검증
        val cust = custCustomRepository.findByCustCd(command.custCd)
            ?: throw UserDefinedException("CUST_NOT_FOUND", "고객을 찾을 수 없습니다: ${command.custCd}")

        val sapCustCd = cust.sapCustCd
        if (sapCustCd.isNullOrBlank()) {
            throw UserDefinedException(
                "SAP_CUST_CD_NOT_FOUND",
                "SAP 거래처 연동실패(고객관리-ERP 코드확인)"
            )
        }

        // 2. Check for duplicate demand in the same period
        // TODO: Implement duplicate check when custom repository is available

        // 3. Calculate demand charge from command data

        // 4. Create demand entity
        val demand = Demand(
            demandId = null,
            demandDt = command.demandDt,
            demandStartDt = command.demandStartDt,
            demandEndDt = command.demandEndDt,
            custCd = command.custCd,
            stndPrice = command.stndPrice,
            supval = command.supval,
            addtax = command.addtax,
            demandCharge = command.demandCharge,
            dscntRate = command.dscntRate,
            insurePrice = command.insurePrice,
            sapCustCd = sapCustCd,
            invcRecpEmailAddr = command.invcRecpEmailAddr,
            demandMemo = command.demandMemo,
            creator = adminId,
            createDtime = LocalDateTime.now(),
            updater = adminId,
            updateDtime = LocalDateTime.now(),
            demandType = command.demandType,
        )
        demand.setAsNew()

        // 5. Create collection ledger entry for demand
        val ledger = CollectionLedger.createForDemand(
            custCd = command.custCd,
            demandDt = command.demandDt,
            demandCharge = command.demandCharge,
            creator = adminId
        )

        val savedLedger = collectionLedgerRepository.save(ledger)

        // 6. Assign colledgerId to demand and save
        demand.assignColledgerId(savedLedger.colledgerId!!)
        val savedDemand = demandRepository.save(demand)

        // 7. Update test requests with closing information
        val createdRequestCount = reqServicePort.updateTstItemClosingInfo(
            directAcctCd = command.custCd,
            startDt = command.demandStartDt,
            endDt = command.demandEndDt,
            exrtId = command.exrtId,
            stndExrt = command.stndExrt,
            closingMemo = command.demandMemo,
            closingUser = adminId
        )

        // 8. Save demand history snapshot (모든 생성 작업 완료 후)
        demandHstRepository.save(DemandHst(
            hstCd = "HST_C",
            hstMemo = "청구 생성",
            worker = adminId,
            workDtime = LocalDateTime.now(),
            demandId = savedDemand.demandId!!,
            demandDt = savedDemand.demandDt,
            custCd = savedDemand.custCd,
            demandStartDt = savedDemand.demandStartDt,
            demandEndDt = savedDemand.demandEndDt,
            stndPrice = savedDemand.stndPrice,
            supval = savedDemand.supval,
            demandCharge = savedDemand.demandCharge,
            addtax = savedDemand.addtax,
            dscntRate = savedDemand.dscntRate,
            demandCreateDtime = savedDemand.demandCreateDtime,
            demandCreatorEmpNo = savedDemand.demandCreatorEmpNo,
            insurePrice = savedDemand.insurePrice,
            invcOutputDtime = savedDemand.invcOutputDtime,
            invcOutputEmpno = savedDemand.invcOutputEmpno,
            slstmtNo = savedDemand.slstmtNo,
            slstmtSendDt = savedDemand.slstmtSendDt,
            slstmtSendEmpNo = savedDemand.slstmtSendEmpNo,
            demandMemo = savedDemand.demandMemo,
            sapCustCd = savedDemand.sapCustCd,
            billPublYn = savedDemand.billPublYn,
            invcRecpEmailAddr = savedDemand.invcRecpEmailAddr,
            creator = savedDemand.creator,
            createDtime = savedDemand.createDtime,
            updater = savedDemand.updater,
            updateDtime = savedDemand.updateDtime,
            colledgerId = savedDemand.colledgerId,
            demandType = savedDemand.demandType,
        ))

        // 9. Return response
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

        // 3. Delete associated collection ledger using colledgerId
        demand.colledgerId?.let { colledgerId ->
            val ledger = collectionLedgerRepository.findById(colledgerId)
            if (ledger != null) {
                collectionLedgerRepository.delete(ledger)
            }
        }

        // 4. Save demand history snapshot (cancel)
        demandHstRepository.save(DemandHst(
            hstCd = "HST_D",
            hstMemo = "청구 취소",
            worker = adminId,
            workDtime = LocalDateTime.now(),
            demandId = demand.demandId!!,
            demandDt = demand.demandDt,
            custCd = demand.custCd,
            demandStartDt = demand.demandStartDt,
            demandEndDt = demand.demandEndDt,
            stndPrice = demand.stndPrice,
            supval = demand.supval,
            demandCharge = demand.demandCharge,
            addtax = demand.addtax,
            dscntRate = demand.dscntRate,
            demandCreateDtime = demand.demandCreateDtime,
            demandCreatorEmpNo = demand.demandCreatorEmpNo,
            insurePrice = demand.insurePrice,
            invcOutputDtime = demand.invcOutputDtime,
            invcOutputEmpno = demand.invcOutputEmpno,
            slstmtNo = demand.slstmtNo,
            slstmtSendDt = demand.slstmtSendDt,
            slstmtSendEmpNo = demand.slstmtSendEmpNo,
            demandMemo = demand.demandMemo,
            sapCustCd = demand.sapCustCd,
            billPublYn = demand.billPublYn,
            invcRecpEmailAddr = demand.invcRecpEmailAddr,
            creator = demand.creator,
            createDtime = demand.createDtime,
            updater = demand.updater,
            updateDtime = demand.updateDtime,
            colledgerId = demand.colledgerId,
            demandType = demand.demandType,
        ))

        // 5. Release test requests (reset closing information)
        val releasedRequestCount = reqServicePort.releaseTstItemClosingInfo(
            directAcctCd = demand.custCd,
            startDt = demand.demandStartDt,
            endDt = demand.demandEndDt,
            updater = adminId
        )

        // 6. Delete demand
        demandRepository.delete(demand)

        // 7. Return response
        return CancelDemandResponse(
            demandId = demandId,
            cancelled = true,
            releasedRequestCount = releasedRequestCount
        )
    }

    /**
     * Send sales statements to ERP in batch (매출전표 배치 생성)
     *
     * Business Rules:
     * 1. 이미 전송된 건(slstmtNo != null)은 스킵
     * 2. 1 RFC 호출로 N건 배치 처리
     * 3. LISGC (1-based 5자리 index)로 응답 row와 입력 demandId 매칭
     * 4. 부분 성공 허용 — 성공한 건만 저장
     */
    override suspend fun sendSalesStatementBatch(
        command: SendSalesStatementBatchCommand,
        adminId: String
    ): SendSalesStatementBatchResponse {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        // LISGC 포맷: yyyyMMdd(8) + HHmm(4) + index(3) = 15자리
        // 분 단위로 prefix가 달라져 동시 배치 간 충돌 방지
        val lisgcPrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))

        // 1. demand 조회 + 이미 전송된 건 필터
        data class DemandWithIndex(val index: Int, val demand: Demand, val lisgc: String)

        val validItems = mutableListOf<DemandWithIndex>()
        val skippedResults = mutableListOf<SendSalesStatementBatchResult>()

        command.demandIds.forEachIndexed { idx, demandId ->
            val demand = demandRepository.findById(demandId)
            when {
                demand == null -> skippedResults += SendSalesStatementBatchResult(
                    demandId = demandId, sent = false, message = "청구서를 찾을 수 없습니다."
                )
                demand.slstmtNo != null -> skippedResults += SendSalesStatementBatchResult(
                    demandId = demandId, sent = false,
                    message = "이미 ERP 매출전표가 생성되었습니다: ${demand.slstmtNo}"
                )
                else -> validItems += DemandWithIndex(
                    index = idx,
                    demand = demand,
                    lisgc = lisgcPrefix + (validItems.size + 1).toString().padStart(3, '0')
                )
            }
        }

        if (validItems.isEmpty()) {
            return SendSalesStatementBatchResponse(sentCount = 0, results = skippedResults)
        }

        // 2. 고객 정보 일괄 조회 (custCd 중복 제거)
        val custCds = validItems.map { it.demand.custCd }.distinct()
        val custMap = custCds.mapNotNull { custCustomRepository.findByCustCd(it) }
            .associateBy { it.custCd }

        // 3. T_ZFIS704 배치 row 구성 — LISGC = 1-based 5자리 인덱스
        val rows = validItems.map { item ->
            val demand = item.demand
            val cust = custMap[demand.custCd]
            SapInvcPostingRow(
                lisgc = item.lisgc,
                xref1 = demand.custCd.takeLast(6),      // 뒤에서 6글자
                debcl = demand.demandType,                  // 10: 일반(매출), 30: 선수금
                budat = demand.demandDt.format(formatter),
                xnegp = "",                                // 취소/수정 전표지시자 (일반전표 SPACE , 취소 'X')
                xblnr = cust?.bizrno ?: "",
                mwskz = "A1",
                kostl = "0033020102",                       // GC지놈 TS팀 고정값
                aufnr = "",
                waers = if (cust?.frgnAcctYn == true) "USD" else "KRW",
                wrbtr = demand.supval,
                wmwst = demand.addtax,
                bupla = "3300",                             // 고정값
                zuonr = demand.custCd,
                xref2 = "20",                               // 10 일발행, 20 월발행, 90 미발행
                xref3 = "E",                                // E 전자계산서, SPACE 수기
                email = demand.invcRecpEmailAddr ?: "",
                sgtxt = "${cust?.custNm ?: demand.custCd} 검사비",
                kidno = cust?.custNm ?: "",                 // 거래처명
            )
        }

        // 4. 배치 RFC 호출
        val sapResults = invoiceErpPort.postInvoices(rows)

        // 5. LISGC로 응답 매칭 — 성공한 건 저장
        val lisgcToItem = validItems.associateBy { it.lisgc }
        val sentResults = mutableListOf<SendSalesStatementBatchResult>()

        sapResults.forEach { sapResult ->
            val item = lisgcToItem[sapResult.lisgc] ?: return@forEach
            val demand = item.demand
            if (sapResult.rtc == "E") {
                sentResults += SendSalesStatementBatchResult(
                    demandId = demand.demandId!!,
                    sent = false,
                    message = "SAP 전기 오류 [${sapResult.rtc}]: ${sapResult.msg ?: "알 수 없는 오류"}"
                )
            } else if (sapResult.belnr.isNullOrBlank()) {
                sentResults += SendSalesStatementBatchResult(
                    demandId = demand.demandId!!,
                    sent = false,
                    message = "SAP에서 전표번호가 반환되지 않았습니다."
                )
            } else {
                demand.sendSalesStatement(sapResult.belnr, adminId)
                val savedDemand = demandRepository.save(demand)
                demandHstRepository.save(DemandHst(
                    hstCd = "HST_M",
                    hstMemo = "매출전표 생성",
                    worker = adminId,
                    workDtime = LocalDateTime.now(),
                    demandId = savedDemand.demandId!!,
                    demandDt = savedDemand.demandDt,
                    custCd = savedDemand.custCd,
                    demandStartDt = savedDemand.demandStartDt,
                    demandEndDt = savedDemand.demandEndDt,
                    stndPrice = savedDemand.stndPrice,
                    supval = savedDemand.supval,
                    demandCharge = savedDemand.demandCharge,
                    addtax = savedDemand.addtax,
                    dscntRate = savedDemand.dscntRate,
                    demandCreateDtime = savedDemand.demandCreateDtime,
                    demandCreatorEmpNo = savedDemand.demandCreatorEmpNo,
                    insurePrice = savedDemand.insurePrice,
                    invcOutputDtime = savedDemand.invcOutputDtime,
                    invcOutputEmpno = savedDemand.invcOutputEmpno,
                    slstmtNo = savedDemand.slstmtNo,
                    slstmtSendDt = savedDemand.slstmtSendDt,
                    slstmtSendEmpNo = savedDemand.slstmtSendEmpNo,
                    demandMemo = savedDemand.demandMemo,
                    sapCustCd = savedDemand.sapCustCd,
                    billPublYn = savedDemand.billPublYn,
                    invcRecpEmailAddr = savedDemand.invcRecpEmailAddr,
                    creator = savedDemand.creator,
                    createDtime = savedDemand.createDtime,
                    updater = savedDemand.updater,
                    updateDtime = savedDemand.updateDtime,
                    colledgerId = savedDemand.colledgerId,
                    demandType = savedDemand.demandType,
                ))
                sentResults += SendSalesStatementBatchResult(
                    demandId = savedDemand.demandId!!,
                    sent = true,
                    slstmtNo = sapResult.belnr,
                    message = "전송 완료"
                )
            }
        }

        val allResults = skippedResults + sentResults
        return SendSalesStatementBatchResponse(
            sentCount = sentResults.count { it.sent },
            results = allResults
        )
    }
}
