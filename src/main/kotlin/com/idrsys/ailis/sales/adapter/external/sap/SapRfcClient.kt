package com.idrsys.ailis.sales.adapter.external.sap

import com.idrsys.ailis.sales.application.dto.request.sap.CustomerIfLabsRow
import com.idrsys.ailis.sales.application.dto.response.sap.SapCustomerIfLabsResponse
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

    fun testConnection(): String {
        return try {
            val destination = JCoDestinationManager.getDestination(destinationName)
            destination.ping()
            "Successfully pinged SAP destination: $destinationName"
        } catch (e: JCoException) {
            logger.error("SAP connection test failed for destination $destinationName", e)
            "SAP connection test failed: ${e.message}"
        }
    }

    @Throws(JCoException::class)
    fun executeCustomerIfLabs(customers: List<CustomerIfLabsRow>): SapCustomerIfLabsResponse {
        logger.info("Executing RFC: {}", sapConfig.rfc.customerIfLabs)
        val destination: JCoDestination = JCoDestinationManager.getDestination(destinationName)
        val function = destination.repository.getFunction(sapConfig.rfc.customerIfLabs)
            ?: throw JCoException(JCoException.JCO_ERROR_FUNCTION_NOT_FOUND, "Function ${sapConfig.rfc.customerIfLabs} not found in SAP.")

        // Set import parameters
        function.importParameterList?.setValue("I_BUKRS", sapConfig.rfc.params.i_bukrs)
        function.importParameterList?.setValue("I_GSBER", sapConfig.rfc.params.i_gsber)

        // Fill table parameter
        val table = function.tableParameterList?.getTable("T_ZFIS703")
        table?.let { mapRequestToTable(customers, it) }

        function.execute(destination)

        val returnCode = function.exportParameterList?.getString("E_IFRTC")
        val returnMessage = function.exportParameterList?.getString("E_IFMSG")
        val resultData = table?.let { mapTableToResponse(it) } ?: emptyList()

        return SapCustomerIfLabsResponse(returnCode, returnMessage, resultData)
    }

    private fun mapRequestToTable(customers: List<CustomerIfLabsRow>, table: JCoTable) {
        customers.forEach { customer ->
            table.appendRow()
            table.setValue("LISGC", customer.lisgc)
            table.setValue("INDCF", customer.indcf)
            table.setValue("STCD1", customer.stcd1)
            table.setValue("NAME1", customer.name1)
            table.setValue("ORT01", customer.ort01)
            table.setValue("PSTLZ", customer.pstlz)
            table.setValue("STRAS", customer.stras)
            table.setValue("LAND1", customer.land1)
            table.setValue("TELF1", customer.telf1)
            table.setValue("SMTP_ADDR", customer.smtp_addr)
            table.setValue("J_1KFREPRE", customer.j_1kfrepre)
            table.setValue("J_1KFTBUS", customer.j_1kftbus)
            table.setValue("J_1KFTIND", customer.j_1kftind)
            table.setValue("GRICD", customer.gricd)
            table.setValue("GRIDT", customer.gridt)
            table.setValue("ZTERM", customer.zterm)
            table.setValue("KUNNR", customer.kunnr)
            table.setValue("ZBDNUM", customer.zbdnum)
            table.setValue("RTC", customer.rtc)
            table.setValue("MSG", customer.msg)
        }
    }

    private fun mapTableToResponse(table: JCoTable): List<CustomerIfLabsRow> {
        return List(table.numRows) { i ->
            table.setRow(i)
            CustomerIfLabsRow(
                lisgc = table.getString("LISGC"),
                indcf = table.getString("INDCF"),
                stcd1 = table.getString("STCD1"),
                name1 = table.getString("NAME1"),
                ort01 = table.getString("ORT01"),
                pstlz = table.getString("PSTLZ"),
                stras = table.getString("STRAS"),
                land1 = table.getString("LAND1"),
                telf1 = table.getString("TELF1"),
                smtp_addr = table.getString("SMTP_ADDR"),
                j_1kfrepre = table.getString("J_1KFREPRE"),
                j_1kftbus = table.getString("J_1KFTBUS"),
                j_1kftind = table.getString("J_1KFTIND"),
                gricd = table.getString("GRICD"),
                gridt = table.getString("GRIDT"),
                zterm = table.getString("ZTERM"),
                kunnr = table.getString("KUNNR"),
                zbdnum = table.getString("ZBDNUM"),
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
