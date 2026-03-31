package com.idrsys.ailis.sales.adapter.external.sap

import com.idrsys.ailis.sales.application.dto.request.sap.CustomerIfLabsRequest
import com.idrsys.ailis.sales.application.dto.request.sap.CustomerIfLabsRow
import com.idrsys.ailis.sales.application.dto.response.sap.CustomerIfLabsResult
import com.idrsys.ailis.sales.application.dto.request.bankdeposit.BankAccountParam
import com.idrsys.ailis.sales.application.dto.response.sap.SapBankDepositResult
import com.idrsys.ailis.sales.application.dto.response.sap.SapCustomerIfLabsResponse
import java.math.BigDecimal
import com.idrsys.ailis.sales.infrastructure.config.SapConfig
import com.sap.conn.jco.JCoDestination
import com.sap.conn.jco.JCoDestinationManager
import com.sap.conn.jco.JCoException
import com.sap.conn.jco.JCoTable
import com.sap.conn.jco.ext.DestinationDataEventListener
import com.sap.conn.jco.ext.DestinationDataProvider
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream
import java.util.Properties

@Component
class SapRfcClient(private val sapConfig: SapConfig) {

    private val logger = LoggerFactory.getLogger(SapRfcClient::class.java)
    private val destinationName = "SAP_SALES_SERVICE"

    companion object {
        private const val BUKRS             = "3300"
        private const val CUSTOMER_IF_LABS = "ZFI_CUSTOMER_IF_LABS" // 고객관리 - 고객정보 등록/수정 -사업자정보관리 탭 - ERP 코드확인
        private const val IF_RE_010        = "ZFI_IF_RE_010"
        private const val IF_RE_020        = "ZFI_IF_RE_020"
        private const val INVC_POSTING     = "ZFI_INVC_POSTING_LABS"
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
    fun executeIfRe010(): Unit = TODO("ZFI_IF_RE_010 not yet implemented")

    /**
     * SAP RFC ZFI_IF_RE_020 호출 — 은행 계좌 입금 내역 조회
     *
     * Import: I_BUKRS, I_DATE_FROM, I_DATE_TO
     * Input Table IT_712: 조회할 은행 계좌 목록 (BANKL, BANKN)
     * Output Table IT_713: 입금 내역 (BANKL, BANKN, GJAHR, BELNR, BUDAT, WRBTR, AVLAMT, WAERS, SGTXT)
     */
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
    fun executeInvcPosting(): Unit = TODO("ZFI_INVC_POSTING_LABS not yet implemented")

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
