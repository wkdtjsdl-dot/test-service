package com.idrsys.ailis.sales.application.dto.response.sap

/**
 * Response DTO for the SAP RFC 'ZFI_CUSTOMER_IF_LABS'.
 */
data class SapCustomerIfLabsResponse(
    val returnCode: String?, // E_IFRTC
    val returnMessage: String?, // E_IFMSG
    val data: List<CustomerIfLabsResult> // T_ZFIS703
)

data class CustomerIfLabsResult(
    val kunnr: String?, // SAP고객코드
    val rtc: String?,   // 리턴코드
    val msg: String?    // 리턴메세지
)
