package com.idrsys.ailis.sales.application.service.billing

import com.idrsys.ailis.sales.application.dto.request.billing.CreateDemandCommand
import com.idrsys.ailis.sales.application.dto.request.billing.RecalculateBillingCommand
import com.idrsys.ailis.sales.application.dto.request.billing.SapInvcPostingRow
import com.idrsys.ailis.sales.application.dto.request.billing.SendSalesStatementBatchCommand
import com.idrsys.ailis.sales.application.dto.request.billing.UpdateDemandMemoCommand
import com.idrsys.ailis.sales.application.dto.response.CancelDemandResponse
import com.idrsys.ailis.sales.application.dto.response.CreateDemandResponse
import com.idrsys.ailis.sales.application.dto.response.RecalculateBillingResponse
import com.idrsys.ailis.sales.application.dto.response.SendSalesStatementBatchResponse
import com.idrsys.ailis.sales.application.dto.response.SendSalesStatementBatchResult
import com.idrsys.ailis.sales.application.dto.response.UpdateDemandMemoResponse
import com.idrsys.ailis.sales.application.required.external.BaseServicePort
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
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
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
    private val baseServicePort: BaseServicePort,
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
        // crcyCd: command에서 전달된 값(예: 'CRCY_KRW') 그대로 저장 — 의뢰내역 필터와 일치시키기 위해
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
            demandCreatorEmpNo = adminId,
            creator = adminId,
            createDtime = LocalDateTime.now(),
            updater = adminId,
            updateDtime = LocalDateTime.now(),
            crcyCd = command.crcyCd,
            frgnCrcyAmt = command.frgnCrcyAmt,
            demandType = command.demandType
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
        // demandType '30' → RQDV_PR, '10' → NON_PR (CASE WHEN 분기와 동일한 로직)
        val tstReqDivCd = if (command.demandType == "30") "RQDV_PR" else "NON_PR"
        val constituentCustCds = custCustomRepository.findConstituentCustCds(command.custCd)
        val createdRequestCount = reqServicePort.updateTstItemClosingInfo(
            custCds = constituentCustCds,
            startDt = command.demandStartDt,
            endDt = command.demandEndDt,
            exrtId = command.exrtId,
            stndExrt = command.stndExrt,
            closingMemo = null,
            closingUser = adminId,
            tstReqDivCd = tstReqDivCd,
            crcyCd = command.crcyCd,
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
            crcyCd = savedDemand.crcyCd,
            frgnCrcyAmt = savedDemand.frgnCrcyAmt,
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
            crcyCd = demand.crcyCd,
            frgnCrcyAmt = demand.frgnCrcyAmt,
            demandType = demand.demandType,
        ))

        // 5. Release test requests (reset closing information)
        // demandType '30' → RQDV_PR, '10' → NON_PR — 생성 시와 동일한 분기 로직
        val tstReqDivCd = if (demand.demandType == "30") "RQDV_PR" else "NON_PR"
        val constituentCustCds = custCustomRepository.findConstituentCustCds(demand.custCd)
        val releasedRequestCount = reqServicePort.releaseTstItemClosingInfo(
            custCds = constituentCustCds,
            startDt = demand.demandStartDt,
            endDt = demand.demandEndDt,
            updater = adminId,
            tstReqDivCd = tstReqDivCd,
            crcyCd = demand.crcyCd,
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

        // 3. 건별 RFC 호출 — 1 demand = 1 RFC call (SAP가 배치 단일 전표 생성하므로 개별 호출)
        val sentResults = mutableListOf<SendSalesStatementBatchResult>()

        for (item in validItems) {
            val demand = item.demand
            val cust = custMap[demand.custCd]
            val row = SapInvcPostingRow(
                lisgc = item.lisgc,
                xref1 = demand.custCd.takeLast(6),      // 뒤에서 6글자
                debcl = demand.demandType ?: "10",
                budat = demand.demandDt.format(formatter),
                xnegp = "",                                // 취소/수정 전표지시자 (일반전표 SPACE , 취소 'X')
                xblnr = cust?.bizrno ?: "",
                mwskz = "A1",
                kostl = "0033020102",                       // GC지놈 TS팀 고정값
                aufnr = "",
                waers = demand.crcyCd?.takeLast(3) ?: "KRW",
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

            val sapResult = invoiceErpPort.postInvoices(listOf(row)).firstOrNull()

            when {
                sapResult == null -> sentResults += SendSalesStatementBatchResult(
                    demandId = demand.demandId!!,
                    sent = false,
                    message = "SAP 응답이 없습니다."
                )
                sapResult.rtc == "E" -> sentResults += SendSalesStatementBatchResult(
                    demandId = demand.demandId!!,
                    sent = false,
                    message = "SAP 전기 오류 [${sapResult.rtc}]: ${sapResult.msg ?: "알 수 없는 오류"}"
                )
                sapResult.belnr.isNullOrBlank() -> sentResults += SendSalesStatementBatchResult(
                    demandId = demand.demandId!!,
                    sent = false,
                    message = "SAP에서 전표번호가 반환되지 않았습니다."
                )
                else -> {
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
                        crcyCd = savedDemand.crcyCd,
                        frgnCrcyAmt = savedDemand.frgnCrcyAmt,
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
        }

        val allResults = skippedResults + sentResults
        return SendSalesStatementBatchResponse(
            sentCount = sentResults.count { it.sent },
            results = allResults
        )
    }

    override suspend fun recalculateBillingDemands(
        command: RecalculateBillingCommand,
        adminId: String
    ): RecalculateBillingResponse {
        val startDt = command.yearMonth.atDay(1)
        val endDt = command.yearMonth.atEndOfMonth()
        val now = LocalDateTime.now()

        // 1. Scope by bzoffiCd if provided
        val scopeCustCds: List<String>? = command.bzoffiCd?.let { bzoffiCd ->
            custCustomRepository.findCustCdsByFrgnAcctYn(false, bzoffiCd) +
                custCustomRepository.findCustCdsByFrgnAcctYn(true, bzoffiCd)
        }
        if (scopeCustCds != null && scopeCustCds.isEmpty()) {
            return RecalculateBillingResponse(0, 0, 0)
        }

        // 2. Get CLCD_Y summaries from req-service
        val rawSummaries = reqServicePort.getClosedDemandSummary(startDt, endDt, scopeCustCds)

        // 3. Apply same exclusion filters as unbilled flow
        val noBillPublCustCds = custCustomRepository.findNoBillPublCustCds()
        val afterBillPublFilter = rawSummaries.filter { it.custCd !in noBillPublCustCds }

        val branch100DeptCds = baseServicePort.getDeptCdsByBranchBcd("100")
        val branch100CustCds = if (branch100DeptCds.isNotEmpty()) {
            custCustomRepository.findCustCdsByBzoffiCds(branch100DeptCds)
        } else emptySet()
        val filteredSummaries = if (branch100CustCds.isNotEmpty()) {
            afterBillPublFilter.filter { it.custCd !in branch100CustCds }
        } else afterBillPublFilter

        // 4. Apply rprsAcctBillCombPublYn mapping → target map
        data class TargetAmounts(
            val stndPrice: BigDecimal,
            val supval: BigDecimal,
            val addtax: BigDecimal,
            val demandCharge: BigDecimal
        )

        val custCdList = filteredSummaries.map { it.custCd }.distinct()
        val rprsBillingInfoMap = custCustomRepository.findRprsBillingInfoByCustCds(custCdList)

        val targetMap: Map<Triple<String, String, String?>, TargetAmounts> = filteredSummaries
            .groupBy { summary ->
                val info = rprsBillingInfoMap[summary.custCd]
                val billingKey = if (info?.rprsAcctBillCombPublYn == true) info.rprsCustCd else summary.custCd
                val demandType = if (summary.tstReqDivCd == "RQDV_PR") "30" else "10"
                Triple(billingKey, demandType, summary.crcyCd)
            }
            .mapValues { (_, summaries) ->
                TargetAmounts(
                    stndPrice = summaries.sumOf { it.stndPrice },
                    supval = summaries.sumOf { it.supval },
                    addtax = summaries.sumOf { it.addtax },
                    demandCharge = summaries.sumOf { it.demandCharge }
                )
            }

        // 5. Get current modifiable demands (slstmtNo IS NULL)
        val allCurrentDemands = demandRepository.findModifiableByMonth(startDt, endDt)
        val scopedCurrentDemands = if (scopeCustCds != null) {
            val scopeSet = scopeCustCds.toSet()
            allCurrentDemands.filter { it.custCd in scopeSet }
        } else allCurrentDemands

        val currentMap = scopedCurrentDemands.associateBy {
            Triple(it.custCd, it.demandType, it.crcyCd)
        }

        // 6. Delta
        val allKeys = (targetMap.keys + currentMap.keys).distinct()
        var insertedCount = 0
        var updatedCount = 0
        var deletedCount = 0

        for (key in allKeys) {
            val target = targetMap[key]
            val current = currentMap[key]

            when {
                target != null && current == null -> {
                    // INSERT: new demand + ledger + history
                    val billingKey = key.first
                    val demandType = key.second
                    val crcyCd = key.third

                    val sapCustCd = custCustomRepository.findByCustCd(billingKey)?.sapCustCd
                    val dscntRate = if (target.stndPrice > BigDecimal.ZERO) {
                        (target.stndPrice - target.demandCharge)
                            .divide(target.stndPrice, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal("100"))
                            .setScale(2, RoundingMode.HALF_UP)
                    } else BigDecimal.ZERO

                    val demand = Demand(
                        demandId = null,
                        demandDt = LocalDate.now(),
                        demandStartDt = startDt,
                        demandEndDt = endDt,
                        custCd = billingKey,
                        stndPrice = target.stndPrice,
                        supval = target.supval,
                        addtax = target.addtax,
                        demandCharge = target.demandCharge,
                        dscntRate = dscntRate,
                        sapCustCd = sapCustCd,
                        demandCreatorEmpNo = adminId,
                        creator = adminId,
                        createDtime = now,
                        updater = adminId,
                        updateDtime = now,
                        crcyCd = crcyCd,
                        demandType = demandType
                    )
                    demand.setAsNew()

                    val ledger = CollectionLedger.createForDemand(
                        custCd = billingKey,
                        demandDt = LocalDate.now(),
                        demandCharge = target.demandCharge,
                        creator = adminId
                    )
                    val savedLedger = collectionLedgerRepository.save(ledger)
                    demand.assignColledgerId(savedLedger.colledgerId!!)
                    val savedDemand = demandRepository.save(demand)

                    demandHstRepository.save(buildDemandHst(savedDemand, "HST_C", "청구수가 재마감", adminId, now))
                    insertedCount++
                }

                target == null && current != null -> {
                    // DELETE: history + ledger + demand
                    val demand = demandRepository.findById(current.demandId) ?: continue

                    demandHstRepository.save(buildDemandHst(demand, "HST_D", "청구수가 재마감", adminId, now))

                    current.colledgerId?.let { colledgerId ->
                        collectionLedgerRepository.findById(colledgerId)?.let {
                            collectionLedgerRepository.delete(it)
                        }
                    }

                    demandRepository.delete(demand)
                    deletedCount++
                }

                target != null && current != null -> {
                    // UPDATE if amounts differ
                    val amountsUnchanged = target.supval.compareTo(current.supval) == 0 &&
                        target.addtax.compareTo(current.addtax) == 0
                    if (amountsUnchanged) continue

                    val demand = demandRepository.findById(current.demandId) ?: continue
                    demand.recalculateCharges(target.supval, target.addtax, adminId)
                    val savedDemand = demandRepository.save(demand)

                    current.colledgerId?.let { colledgerId ->
                        collectionLedgerRepository.findById(colledgerId)?.let { ledger ->
                            ledger.updateCollection(
                                colbillDt = ledger.colbillDt,
                                colbillAmt = target.demandCharge,
                                colbillItemNm = ledger.colbillItemNm,
                                colbillItemDtl = ledger.colbillItemDtl,
                                updater = adminId
                            )
                            collectionLedgerRepository.save(ledger)
                        }
                    }

                    demandHstRepository.save(buildDemandHst(savedDemand, "HST_C", "청구수가 재마감", adminId, now))
                    updatedCount++
                }
            }
        }

        return RecalculateBillingResponse(insertedCount, updatedCount, deletedCount)
    }

    private fun buildDemandHst(demand: Demand, hstCd: String, hstMemo: String, worker: String, workDtime: LocalDateTime): DemandHst {
        return DemandHst(
            hstCd = hstCd,
            hstMemo = hstMemo,
            worker = worker,
            workDtime = workDtime,
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
            crcyCd = demand.crcyCd,
            frgnCrcyAmt = demand.frgnCrcyAmt,
            demandType = demand.demandType,
        )
    }

    override suspend fun updateDemandMemo(
        demandId: String,
        command: UpdateDemandMemoCommand,
        adminId: String
    ): UpdateDemandMemoResponse {
        val demand = demandRepository.findById(demandId)
            ?: throw UserDefinedException("DEMAND_NOT_FOUND", "청구 건을 찾을 수 없습니다.")

        demand.updateMemo(command.demandMemo, adminId)
        val savedDemand = demandRepository.save(demand)

        demandHstRepository.save(DemandHst(
            hstCd = "HST_M",
            hstMemo = "청구 메모 수정",
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
            crcyCd = savedDemand.crcyCd,
            frgnCrcyAmt = savedDemand.frgnCrcyAmt,
            demandType = savedDemand.demandType,
        ))

        return UpdateDemandMemoResponse(
            demandId = savedDemand.demandId!!,
            demandMemo = savedDemand.demandMemo
        )
    }
}
