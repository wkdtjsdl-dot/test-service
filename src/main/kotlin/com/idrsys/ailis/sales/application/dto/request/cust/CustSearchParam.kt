package com.idrsys.ailis.sales.application.dto.cust

data class CustSearchParam(
    val custCdNm: String? = null,
    val rprsCustCdNm: String? = null,
    val custStatCd: String? = null,
    val regStartDt: String? = null,
    val regEndDt: String? = null,
    val custDivCd: String? = null,
    val asrtCd: String? = null,
    val bizrno:String? = null,
    val careInstNo:String? = null,
    val frgnAcctYn:Boolean? = null,
    val studyProjCustYn:Boolean? = null,
    val sapCustCd:String? = null,
    val custTypeCd:String? = null
)
