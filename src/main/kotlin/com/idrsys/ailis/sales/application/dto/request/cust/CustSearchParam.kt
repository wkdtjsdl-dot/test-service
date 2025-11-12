package com.idrsys.ailis.sales.application.dto.cust

data class CustSearchParam(
    val bzoffiCd: String? = null,
    val custCdNm: String? = null,
    val custCd: String? = null, // 자동완성 선택시
    val rprsCustCdNm: String? = null,
    val rprsCustCd: String? = null, // 자동완성 선택시
    val custStatCd: String? = null,
    val regStartDt: String? = null,
    val regEndDt: String? = null,
    val custDivCd: String? = null,
    val asrtCd: String? = null,
    val medicalSubj: String? = null,
    val bizrno:String? = null,
    val careInstNo:String? = null,
    val cntrStartDt: String? = null,
    val cntrEndDt: String? = null,
    val cntrEndStartDt: String? = null,
    val cntrEndEndDt: String? = null,
    val recntrMonth: String? = null,
    val frgnAcctYn:Boolean? = null,
    val studyProjCustYn:Boolean? = null,
    val sapCustCd:String? = null,
    val custTypeCd:String? = null
)

data class CustAutoCompleteSearchParam(
    val custCdNm: String? = null,
    val rprsCustCdNm: String? = null,
)
