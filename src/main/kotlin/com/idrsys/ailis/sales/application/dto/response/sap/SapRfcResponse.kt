package com.idrsys.ailis.sales.application.dto.response.sap

import com.idrsys.ailis.sales.application.dto.request.sap.CustomerIfLabsRow

/**
 * Response DTO for the SAP RFC 'ZFI_CUSTOMER_IF_LABS'.
 */
data class SapCustomerIfLabsResponse(
    val returnCode: String?, // E_IFRTC
    val returnMessage: String?, // E_IFMSG
    val data: List<CustomerIfLabsRow> // T_ZFIS703
)
