package com.idrsys.ailis.sales.adapter.external.sap

import com.idrsys.ailis.sales.application.dto.request.sap.CustomerIfLabsRequest
import com.idrsys.ailis.sales.application.dto.request.sap.CustomerIfLabsRow
import com.idrsys.ailis.sales.application.dto.response.sap.CustomerIfLabsResult
import com.idrsys.ailis.sales.application.dto.request.bankdeposit.BankAccountParam
import com.idrsys.ailis.sales.application.dto.request.billing.SapInvcPostingRow
import com.idrsys.ailis.sales.application.dto.request.ifre010.SapIfRe010Row
import com.idrsys.ailis.sales.application.dto.response.sap.SapBankDepositResult
import com.idrsys.ailis.sales.application.dto.response.sap.SapCustomerIfLabsResponse
import com.idrsys.ailis.sales.application.dto.response.sap.SapIfRe010Result
import com.idrsys.ailis.sales.application.dto.response.sap.SapInvcPostingResult
import com.idrsys.ailis.sales.application.required.sap.BankDepositErpPort
import com.idrsys.ailis.sales.application.required.sap.CollectionErpPort
import com.idrsys.ailis.sales.application.required.sap.InvoiceErpPort
import java.math.BigDecimal
import com.idrsys.ailis.sales.infrastructure.config.SapConfig
import com.sap.conn.jco.JCoDestination
import com.sap.conn.jco.JCoDestinationManager
import com.sap.conn.jco.JCoException
import com.sap.conn.jco.JCoTable
import com.sap.conn.jco.ext.DestinationDataEventListener
import com.sap.conn.jco.ext.DestinationDataProvider
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream
import java.util.Properties

@Component
@Profile("sap")
class SapRfcClient(private val sapConfig: SapConfig) : CollectionErpPort, InvoiceErpPort, BankDepositErpPort {

    private val logger = LoggerFactory.getLogger(SapRfcClient::class.java)
    private val destinationName = "SAP_SALES_SERVICE"

    companion object {
        private const val BUKRS             = "3300" // 회사코드 전체고정
        private const val CUSTOMER_IF_LABS = "ZFI_CUSTOMER_IF_LABS" // 고객관리 - 고객정보 등록/수정 -사업자정보관리 탭 - ERP 코드확인
        private const val IF_RE_010        = "ZFI_IF_RE_010" // 수금확정
        private const val IF_RE_020        = "ZFI_IF_RE_020" // 입금내역
        private const val INVC_POSTING     = "ZFI_INVC_POSTING_LABS" // 매출전표
    }

    init {
        registerDestination()
    }

    private fun registerDestination() {
        val properties = Properties().apply {
            setProperty(DestinationDataProvider.JCO_ASHOST, sapConfig.connection.ashost)
            setProperty(DestinationDataProvider.JCO_SYSNR, sapConfig.connection.sysnr)
            setProperty(DestinationDataProvider.JCO_CLIENT, sapConfig.connection.client)
            setProperty(DestinationDataProvider.JCO_USER, sapConfig.connection.user)
            setProperty(DestinationDataProvider.JCO_PASSWD, sapConfig.connection.passwd)
            setProperty(DestinationDataProvider.JCO_LANG, sapConfig.connection.lang)
            setProperty(DestinationDataProvider.JCO_CODEPAGE, sapConfig.connection.codepage)
        }

        // Avoid re-registering if already present
        if (com.sap.conn.jco.ext.Environment.isDestinationDataProviderRegistered()) {
            logger.info("Destination data provider is already registered.")
        } else {
            val provider = CustomDestinationProvider()
            provider.changeProperties(destinationName, properties)
            com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(provider)
            logger.info("CustomDestinationProvider registered successfully.")
        }
    }

    @Throws(JCoException::class)
    fun executeCustomerIfLabs(request: CustomerIfLabsRequest): SapCustomerIfLabsResponse {
        logger.info("Executing RFC: {}", CUSTOMER_IF_LABS)
        val destination: JCoDestination = JCoDestinationManager.getDestination(destinationName)
        val function = destination.repository.getFunction(CUSTOMER_IF_LABS)
            ?: throw JCoException(JCoException.JCO_ERROR_FUNCTION_NOT_FOUND, "Function $CUSTOMER_IF_LABS not found in SAP.")

        // Set import parameters
        function.importParameterList?.setValue("I_BUKRS", BUKRS)
        function.importParameterList?.setValue("I_GSBER", "1000")

        // Fill table parameter
        val table = function.tableParameterList?.getTable("T_ZFIS703")
        table?.let { mapRequestToTable(request.customers, it) }

        function.execute(destination)

        val returnCode = function.exportParameterList?.getString("E_IFRTC")
        val returnMessage = function.exportParameterList?.getString("E_IFMSG")
        val resultData = table?.let { mapTableToResponse(it) } ?: emptyList()

        return SapCustomerIfLabsResponse(returnCode, returnMessage, resultData)
    }

    @Throws(JCoException::class)
    override fun sendCollection(rtype: String, row: SapIfRe010Row): SapIfRe010Result =
        executeIfRe010(rtype, row)

    @Throws(JCoException::class)
    fun executeIfRe010(rtype: String, row: SapIfRe010Row): SapIfRe010Result {
        logger.info("Executing RFC: {} | rtype={} | payload={}", IF_RE_010, rtype, row)
        val destination: JCoDestination = JCoDestinationManager.getDestination(destinationName)
        val function = destination.repository.getFunction(IF_RE_010)
            ?: throw JCoException(JCoException.JCO_ERROR_FUNCTION_NOT_FOUND, "Function $IF_RE_010 not found in SAP.")

        function.importParameterList?.setValue("I_BUKRS", BUKRS)   // 사회코드
        function.importParameterList?.setValue("I_RTYPE", rtype)
        function.importParameterList?.setValue("I_GSBER", "3300")  // 사업영역 고정값

        // IT_108 테이블 — 1건씩 처리 (리턴 순서 보장 불가)
        val table = function.tableParameterList?.getTable("IT_108")
            ?: throw JCoException(JCoException.JCO_ERROR_FUNCTION_NOT_FOUND, "Table IT_108 not found in SAP.")
        table.appendRow()
        // ── 공통 ────────────────────────────────────────────────
        table.setValue("BUKRS",      BUKRS)
        table.setValue("BUDAT",      row.budat ?: "")
        table.setValue("KKBER",      row.kkber ?: "")
        table.setValue("VKGRP",      row.vkgrp ?: "")
        table.setValue("VKBUR",      row.vkbur ?: "")
        table.setValue("SEQ",        row.seq ?: "")
        table.setValue("GJAHR",      row.gjahr ?: "")
        table.setValue("MONAT",      row.monat ?: "")
        table.setValue("UZAWE",      row.uzawe ?: "")
        row.wrbtr?.let { table.setValue("WRBTR", it.toPlainString()) }
        table.setValue("IN_DATE",    row.inDate ?: "")
        table.setValue("STCD2",      row.stcd2 ?: "")
        table.setValue("SGTXT",      row.sgtxt ?: "")
        table.setValue("VKORG",      row.vkorg ?: "")
        table.setValue("GSBER",      row.gsber ?: "")
        // ── 예금/카드 전용 ──────────────────────────────────────
        when (row) {
            is SapIfRe010Row.Bank -> {
                table.setValue("BANKL",      row.bankl ?: "")
                table.setValue("BANKN",      row.bankn ?: "")
                table.setValue("BELNR_GASU", row.belnrGasu ?: "")
                table.setValue("KUNNRZZ",    row.kunnrzz ?: "")
            }
            is SapIfRe010Row.Card -> {
                table.setValue("KUNNRZZ",    row.kunnrzz ?: "")
                table.setValue("ZFBDT",      row.zfbdt ?: "")
                table.setValue("ZSTMEMB",    row.zstmemb ?: "")
                table.setValue("ZCOMPCD",    row.zcompcd ?: "")
                row.appramt?.let { table.setValue("APPRAMT", it.toPlainString()) }
                table.setValue("INSOMONTH",  row.insomonth ?: "")
                table.setValue("RUDAT",      row.rudat ?: "")
            }
        }
        // ── SAP 리턴 필드 (입력 제외) ────────────────────────────
        // BELNR1, BELNR2, CONFIRM_ID, ADATE, RESUL,
        // STCD2, XBLNR, ZSTATUS, ZRESULT

        function.execute(destination)

        val returnCode    = function.exportParameterList?.getString("RTYPE")
        val returnMessage = function.exportParameterList?.getString("RETURN")

        return if (table.numRows > 0) {
            table.setRow(0)
            SapIfRe010Result(
                belnr1        = table.getString("BELNR1").takeIf { it.isNotBlank() },
                belnr2        = table.getString("BELNR2").takeIf { it.isNotBlank() },
                confirmId     = table.getString("CONFIRM_ID").takeIf { it.isNotBlank() },
                adate         = table.getString("ADATE").takeIf { it.isNotBlank() },
                resul         = table.getString("RESUL").takeIf { it.isNotBlank() },
                stcd1         = table.getString("STCD1").takeIf { it.isNotBlank() },
                stcd2         = table.getString("STCD2").takeIf { it.isNotBlank() },
                xblnr         = table.getString("XBLNR").takeIf { it.isNotBlank() },
                sgtxt         = table.getString("SGTXT").takeIf { it.isNotBlank() },
                vkorg         = table.getString("VKORG").takeIf { it.isNotBlank() },
                gsber         = table.getString("GSBER").takeIf { it.isNotBlank() },
                rudat         = table.getString("RUDAT").takeIf { it.isNotBlank() },
                zstatus       = table.getString("ZSTATUS").takeIf { it.isNotBlank() },
                zresult       = table.getString("ZRESULT").takeIf { it.isNotBlank() },
                returnCode    = returnCode?.takeIf { it.isNotBlank() },
                returnMessage = returnMessage?.takeIf { it.isNotBlank() },
            )
        } else {
            SapIfRe010Result(
                belnr1 = null, belnr2 = null, confirmId = null,
                adate = null, resul = null, stcd1 = null, stcd2 = null,
                xblnr = null, sgtxt = null, vkorg = null, gsber = null,
                rudat = null, zstatus = null, zresult = null,
                returnCode    = returnCode?.takeIf { it.isNotBlank() },
                returnMessage = returnMessage?.takeIf { it.isNotBlank() },
            )
        }
    }

    /**
     * SAP RFC ZFI_IF_RE_020 호출 — 은행 계좌 입금 내역 조회
     *
     * Import: I_BUKRS, I_DATE_FROM, I_DATE_TO
     * Input Table IT_712: 조회할 은행 계좌 목록 (BANKL, BANKN)
     * Output Table IT_713: 입금 내역 (BANKL, BANKN, GJAHR, BELNR, BUDAT, WRBTR, AVLAMT, WAERS, SGTXT)
     */
    @Throws(JCoException::class)
    override fun fetchBankDeposits(
        startDt: String,
        endDt: String,
        bankAccounts: List<BankAccountParam>
    ): List<SapBankDepositResult> = executeIfRe020(startDt, endDt, bankAccounts)

    @Throws(JCoException::class)
    fun executeIfRe020(
        startDt: String,
        endDt: String,
        bankAccounts: List<BankAccountParam>
    ): List<SapBankDepositResult> {
        logger.info("Executing RFC: {} | startDt={}, endDt={}, accounts={}", IF_RE_020, startDt, endDt, bankAccounts.size)
        val destination: JCoDestination = JCoDestinationManager.getDestination(destinationName)
        val function = destination.repository.getFunction(IF_RE_020)
            ?: throw JCoException(JCoException.JCO_ERROR_FUNCTION_NOT_FOUND, "Function $IF_RE_020 not found in SAP.")

        function.importParameterList?.setValue("I_BUKRS", BUKRS)
        function.importParameterList?.setValue("I_DATE_FROM", startDt)
        function.importParameterList?.setValue("I_DATE_TO", endDt)

        // IT_712: 조회할 은행 계좌 목록
        val inputTable = function.tableParameterList?.getTable("IT_712")
        inputTable?.let { table ->
            bankAccounts.forEach { account ->
                table.appendRow()
                table.setValue("BANKL", account.bankl)
                table.setValue("BANKN", account.bankn)
            }
        }

        function.execute(destination)

        val outputTable = function.tableParameterList?.getTable("IT_713")
            ?: return emptyList()

        return mapBankDepositTableToResult(outputTable)
    }

    @Throws(JCoException::class)
    override fun postInvoices(rows: List<SapInvcPostingRow>): List<SapInvcPostingResult> =
        executeInvcPosting(rows)

    @Throws(JCoException::class)
    fun executeInvcPosting(rows: List<SapInvcPostingRow>): List<SapInvcPostingResult> {
        logger.info("Executing RFC: {} | count={}", INVC_POSTING, rows.size)
        val destination: JCoDestination = JCoDestinationManager.getDestination(destinationName)
        val function = destination.repository.getFunction(INVC_POSTING)
            ?: throw JCoException(JCoException.JCO_ERROR_FUNCTION_NOT_FOUND, "Function $INVC_POSTING not found in SAP.")

        function.importParameterList?.setValue("I_BUKRS", BUKRS)   // 사회코드
        function.importParameterList?.setValue("I_GSBER", "1000")  // 사업영역

        val table = function.tableParameterList?.getTable("T_ZFIS704")
            ?: throw JCoException(JCoException.JCO_ERROR_FUNCTION_NOT_FOUND, "Table T_ZFIS704 not found in SAP.")

        // 배치 입력 — LISGC는 row.lisgc (1-based 5자리 index) 로 매칭
        rows.forEach { row ->
            table.appendRow()
            table.setValue("LISGC",  row.lisgc)
            table.setValue("XREF1",  row.xref1 ?: "")
            table.setValue("DEBCL",  row.debcl ?: "")
            table.setValue("BUDAT",  row.budat ?: "")
            table.setValue("XNEGP",  row.xnegp ?: "")
            table.setValue("XBLNR",  row.xblnr ?: "")
            table.setValue("MWSKZ",  row.mwskz ?: "")
            table.setValue("KOSTL",  row.kostl ?: "")
            table.setValue("AUFNR",  row.aufnr ?: "")
            table.setValue("WAERS",  row.waers ?: "")
            row.wrbtr?.let { table.setValue("WRBTR", it.toPlainString()) }
            row.wmwst?.let { table.setValue("WMWST", it.toPlainString()) }
            table.setValue("BUPLA",  row.bupla ?: "")
            table.setValue("ZUONR",  row.zuonr ?: "")
            table.setValue("XREF2",  row.xref2 ?: "")
            table.setValue("XREF3",  row.xref3 ?: "")
            table.setValue("EMAIL",  row.email ?: "")
            table.setValue("SGTXT",  row.sgtxt ?: "")
            table.setValue("KIDNO",  row.kidno ?: "")
        }

        rows.forEachIndexed { i, row ->
            logger.info("[{}] Payload[{}]: LISGC={} XREF1={} DEBCL={} BUDAT={} XNEGP={} XBLNR={} MWSKZ={} KOSTL={} AUFNR={} WAERS={} WRBTR={} WMWST={} BUPLA={} ZUONR={} XREF2={} XREF3={} EMAIL={} SGTXT={} KIDNO={}",
                INVC_POSTING, i,
                row.lisgc, row.xref1, row.debcl, row.budat, row.xnegp, row.xblnr,
                row.mwskz, row.kostl, row.aufnr, row.waers, row.wrbtr, row.wmwst,
                row.bupla, row.zuonr, row.xref2, row.xref3, row.email, row.sgtxt, row.kidno
            )
        }

        function.execute(destination)

        val ifrtc = function.exportParameterList?.getString("E_IFRTC")
        val ifmsg = function.exportParameterList?.getString("E_IFMSG")
        logger.info("[{}] Export: E_IFRTC={} E_IFMSG={} | response rows={}", INVC_POSTING, ifrtc, ifmsg, table.numRows)

        return List(table.numRows) { i ->
            table.setRow(i)
            val lisgc = table.getString("LISGC")
            val belnr = table.getString("BELNR")
            val rtc   = table.getString("RTC")
            val msg   = table.getString("MSG")
            logger.info("[{}] Row[{}]: LISGC={} BELNR={} RTC={} MSG={}", INVC_POSTING, i, lisgc, belnr, rtc, msg)
            SapInvcPostingResult(
                lisgc = lisgc.takeIf { it.isNotBlank() },
                belnr = belnr.takeIf { it.isNotBlank() },
                rtc   = rtc.takeIf   { it.isNotBlank() },
                msg   = msg.takeIf   { it.isNotBlank() },
                ifrtc = ifrtc?.takeIf { it.isNotBlank() },
                ifmsg = ifmsg?.takeIf { it.isNotBlank() },
            )
        }
    }

    private fun mapBankDepositTableToResult(table: JCoTable): List<SapBankDepositResult> {
        return List(table.numRows) { i ->
            table.setRow(i)
            SapBankDepositResult(
                bankl = table.getString("BANKL").takeIf { it.isNotBlank() },      // 은행번호
                bankn = table.getString("BANKN").takeIf { it.isNotBlank() },      // 계좌번호
                accountYear = table.getString("GJAHR").takeIf { it.isNotBlank() }, // 회계연도
                surecpSlstmtNo = table.getString("BELNR").takeIf { it.isNotBlank() }, // 가수금 전표번호
                depositDt = table.getString("BUDAT"),                              // 입금일 (yyyyMMdd)
                depositAmt = table.getBigDecimal("WRBTR") ?: BigDecimal.ZERO,     // 입금액
                outamt = table.getBigDecimal("AVLAMT"),                            // 미정산금액
                crcyCd = table.getString("WAERS").takeIf { it.isNotBlank() },     // 통화
                remark = table.getString("SGTXT").takeIf { it.isNotBlank() }      // 적요
            )
        }
    }

    private fun mapRequestToTable(customers: List<CustomerIfLabsRow>, table: JCoTable) {
        customers.forEach { customer ->
            table.appendRow()
            table.setValue("INDCF", "1") // 법인 1/ 개인 2 법인 고정
            table.setValue("STCD1", customer.stcd1) // front 사업자등록번호
            table.setValue("LAND1", "KR") // 고정
        }
    }

    private fun mapTableToResponse(table: JCoTable): List<CustomerIfLabsResult> {
        return List(table.numRows) { i ->
            table.setRow(i)
            CustomerIfLabsResult(
                kunnr = table.getString("KUNNR"),
                rtc = table.getString("RTC"),
                msg = table.getString("MSG")
            )
        }
    }

    /**
     * Custom destination provider to manage SAP connection properties programmatically.
     */
    class CustomDestinationProvider : DestinationDataProvider {
        private val destinations = mutableMapOf<String, Properties>()

        fun changeProperties(destName: String, properties: Properties) {
            destinations[destName] = properties
        }

        override fun getDestinationProperties(destName: String): Properties? {
            return destinations[destName]
        }

        override fun setDestinationDataEventListener(listener: DestinationDataEventListener) {
            // Not needed for this implementation
        }

        override fun supportsEvents(): Boolean {
            return false
        }
    }
}
