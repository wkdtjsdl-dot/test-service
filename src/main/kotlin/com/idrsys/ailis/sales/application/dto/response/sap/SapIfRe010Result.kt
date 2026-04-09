package com.idrsys.ailis.sales.application.dto.response.sap

data class SapIfRe010Result(
    // IT_108 테이블 리턴 필드
    val belnr1: String?,        // BELNR1      전표번호1
    val belnr2: String?,        // BELNR2      전표번호2
    val confirmId: String?,     // CONFIRM_ID  확인ID
    val adate: String?,         // ADATE       승인일자 (yyyyMMdd)
    val resul: String?,         // RESUL       결과코드
    val stcd1: String?,         // STCD1       사업자등록번호
    val stcd2: String?,         // STCD2       주민등록번호
    val xblnr: String?,         // XBLNR       참조문서번호
    val sgtxt: String?,         // SGTXT       적요
    val vkorg: String?,         // VKORG       판매조직
    val gsber: String?,         // GSBER       사업영역
    val rudat: String?,         // RUDAT       반려일자 (yyyyMMdd)
    val zstatus: String?,       // ZSTATUS     처리상태
    val zresult: String?,       // ZRESULT     처리결과 메시지
    // Export 파라미터
    val returnCode: String?,    // RTYPE       RFC 리턴코드
    val returnMessage: String?, // RETURN      RFC 리턴메시지
)
