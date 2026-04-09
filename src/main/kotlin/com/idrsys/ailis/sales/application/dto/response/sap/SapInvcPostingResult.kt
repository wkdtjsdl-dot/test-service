package com.idrsys.ailis.sales.application.dto.response.sap

/**
 * SAP RFC ZFI_INVC_POSTING_LABS — T_ZFIS704 응답 row + export params
 */
data class SapInvcPostingResult(
    val lisgc: String?,         // LISGC  송신 SEQ — 배치 row 매칭용
    val belnr: String?,         // BELNR  회계전표번호 (전기 후 SAP가 채워줌)
    val rtc: String?,           // RTC    리턴코드 (에러 시 non-blank)
    val msg: String?,           // MSG    리턴메시지
    val ifrtc: String?,         // E_IFRTC  RFC 전체 리턴코드
    val ifmsg: String?,         // E_IFMSG  RFC 전체 리턴메시지
)
