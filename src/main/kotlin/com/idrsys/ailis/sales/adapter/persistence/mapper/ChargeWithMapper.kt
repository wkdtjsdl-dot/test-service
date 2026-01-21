package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.idrsys.ailis.sales.application.dto.query.ChargeWithDetails
import com.idrsys.ailis.sales.application.dto.query.SalesPicInfo
import io.r2dbc.spi.Row
import java.time.LocalDate
import java.time.LocalDateTime

private val objectMapper = ObjectMapper()

private val salesPicListType = object : TypeReference<List<SalesPicInfo>>() {}

internal fun Row.toChargeWithDetail(): ChargeWithDetails {
    val salesPicsJson = this.get("sales_pics", String::class.java)
    val salesPicsList = if (salesPicsJson.isNullOrBlank() || salesPicsJson == "[]" || salesPicsJson == "[{}]") {
        emptyList()
    } else {
        try { objectMapper.readValue(salesPicsJson, salesPicListType) }
        catch (e: Exception) { emptyList() }
    }

    return ChargeWithDetails(
        custChargeId    = this.get("cust_charge_id", String::class.java)!!,
        custMstId       = this.get("cust_mst_id", String::class.java),
        custCd          = this.get("cust_cd", String::class.java)!!,
        applyStartDt    = this.get("apply_start_dt", LocalDate::class.java)!!,
        applyEndDt      = this.get("apply_end_dt", LocalDate::class.java)!!,
        tstCd           = this.get("tst_cd", String::class.java)!!,
        crcyCd          = this.get("crcy_cd", String::class.java)!!,
        stndPrice       = this.get("stnd_price", Long::class.javaObjectType),
        specialCharge   = this.get("special_charge", Long::class.javaObjectType)!!,
        supval          = this.get("supval", Long::class.javaObjectType),
        addtax          = this.get("addtax", Long::class.javaObjectType),
        remark          = this.get("remark", String::class.java),
        apprInfoNo = this.get("appr_info_no", Long::class.java),
        currApprSeq     = this.get("curr_appr_seq", Int::class.javaObjectType),
        apprSubmsEmpNo  = this.get("appr_subms_emp_no", String::class.java),
        apprSubmsDtime  = this.get("appr_subms_dtime", LocalDateTime::class.java),
        lastApprStatCd  = this.get("last_appr_stat_cd", String::class.java)!!,
        apprLvlCd       = this.get("appr_lvl_cd", String::class.java),
        creator         = this.get("creator", String::class.java)!!,
        createDtime     = this.get("create_dtime", LocalDateTime::class.java)!!,
        updater         = this.get("updater", String::class.java)!!,
        updateDtime     = this.get("update_dtime", LocalDateTime::class.java)!!,
        custNm          = this.get("cust_nm", String::class.java),
        bzoffiCd        = this.get("bzoffi_cd", String::class.java),
        salesPics       = salesPicsList
    )
}