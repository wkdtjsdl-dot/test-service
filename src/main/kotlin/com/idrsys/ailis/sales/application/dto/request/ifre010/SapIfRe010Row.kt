package com.idrsys.ailis.sales.application.dto.request.ifre010

import java.math.BigDecimal

sealed class SapIfRe010Row {
    // ── 공통 입력 ───────────────────────────────────────────────
    abstract val budat: String?        // BUDAT       전기일자 (yyyyMMdd)
    abstract val kkber: String?        // KKBER       신용관리범위
    abstract val vkgrp: String?        // VKGRP       영업사원그룹
    abstract val vkbur: String?        // VKBUR       영업사무소
    abstract val seq: String?          // SEQ         순번
    abstract val gjahr: String?        // GJAHR       회계연도
    abstract val monat: String?        // MONAT       회계기간
    abstract val uzawe: String?        // UZAWE       지불방법 (02:예금, 04:카드)
    abstract val wrbtr: BigDecimal?    // WRBTR       수금금액
    abstract val inDate: String?       // IN_DATE     입금일자 (yyyyMMdd)
    abstract val stcd2: String?        // STCD2       사업자등록번호
    abstract val sgtxt: String?        // SGTXT       품목텍스트
    abstract val vkorg: String?        // VKORG       판매조직
    abstract val gsber: String?        // GSBER       사업영역

    data class Bank(
        override val budat: String?,
        override val kkber: String?,
        override val vkgrp: String?,
        override val vkbur: String?,
        override val seq: String?,
        override val gjahr: String?,
        override val monat: String?,
        override val uzawe: String?,
        override val wrbtr: BigDecimal?,
        override val inDate: String?,
        override val stcd2: String?,
        override val sgtxt: String?,
        override val vkorg: String?,
        override val gsber: String?,
        // ── 예금 전용 ───────────────────────────────────────────
        val bankl: String?,            // BANKL       은행번호
        val bankn: String?,            // BANKN       은행계정번호
        val belnrGasu: String?,        // BELNR_GASU  가수금 전표번호
        val kunnrzz: String? = null,   // KUNNRZZ     비즈니스 파트너 고객번호
    ) : SapIfRe010Row()

    data class Card(
        override val budat: String?,
        override val kkber: String?,
        override val vkgrp: String?,
        override val vkbur: String?,
        override val seq: String?,
        override val gjahr: String?,
        override val monat: String?,
        override val uzawe: String?,
        override val wrbtr: BigDecimal?,
        override val inDate: String?,
        override val stcd2: String?,
        override val sgtxt: String?,
        override val vkorg: String?,
        override val gsber: String?,
        // ── 카드 전용 ───────────────────────────────────────────
        val kunnrzz: String?,          // KUNNRZZ     비즈니스 파트너 고객번호
        val zfbdt: String?,            // ZFBDT       만기일/결제일 (yyyyMMdd)
        val zstmemb: String?,          // ZSTMEMB     가맹점번호
        val zcompcd: String?,          // ZCOMPCD     카드회사코드
        val appramt: BigDecimal?,      // APPRAMT     승인금액
        val insomonth: String?,        // INSOMONTH   할부개월수
        val rudat: String?,            // RUDAT       전기일자/결제일 (yyyyMMdd)
    ) : SapIfRe010Row()
}
