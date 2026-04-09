package com.idrsys.ailis.sales.application.dto.request.billing

import java.math.BigDecimal

/**
 * SAP RFC ZFI_INVC_POSTING_LABS — T_ZFIS704 입력 row DTO
 */
data class SapInvcPostingRow(
    val lisgc: String,          // LISGC  송신 SEQ ("00001" 고정, 1건씩 처리)
    val xref1: String?,         // XREF1  거래처코드 (custCd)
    val debcl: String?,         // DEBCL  매출/선수금 구분 (10=매출, 30=선수금)
    val budat: String?,         // BUDAT  전기일 (yyyyMMdd)
    val xnegp: String?,         // XNEGP  취소/수정 전표 지시자
    val xblnr: String?,         // XBLNR  사업자등록번호
    val mwskz: String?,         // MWSKZ  세금코드 (A1=일반매출, A4=카드/현금)
    val kostl: String?,         // KOSTL  코스트센터코드
    val aufnr: String?,         // AUFNR  오더번호 (건증만 필수)
    val waers: String?,         // WAERS  통화코드
    val wrbtr: BigDecimal?,     // WRBTR  공급가액
    val wmwst: BigDecimal?,     // WMWST  세액
    val bupla: String?,         // BUPLA  사업장코드 (3300)
    val zuonr: String?,         // ZUONR  거래처코드
    val xref2: String?,         // XREF2  계산서 발행기준 (10=일발행, 20=월발행, 90=미발행)
    val xref3: String?,         // XREF3  전자계산서 여부 (E=전자, SPACE=수기)
    val email: String?,         // EMAIL  세금계산서 발송 이메일
    val sgtxt: String?,         // SGTXT  품목명 외 건수 Text
    val kidno: String?,         // KIDNO  거래처명
)
