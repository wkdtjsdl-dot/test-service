package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.ChargeApproveQuery
import io.r2dbc.spi.Row
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Row → ChargeApproveQuery 매핑 함수
 */
fun Row.toChargeApproveQuery(): ChargeApproveQuery {
    return ChargeApproveQuery(
        custChargeId = this.get("cust_charge_id", String::class.java)!!,
        custCd = this.get("cust_cd", String::class.java)!!,
        custNm = this.get("cust_nm", String::class.java),  // JOIN으로 가져옴
        tstCd = this.get("tst_cd", String::class.java)!!,
        tstNm = null,  // Service에서 채움
        applyStartDt = this.get("apply_start_dt", LocalDate::class.java)!!,
        applyEndDt = this.get("apply_end_dt", LocalDate::class.java)!!,
        specialCharge = this.get("special_charge", Long::class.java)!!,
        stndPrice = this.get("stnd_price", java.lang.Long::class.java)?.toLong(),
        supval = this.get("supval", Long::class.java),
        addtax = this.get("addtax", Long::class.java),
        remark = this.get("remark", String::class.java),
        apprInfoNo = this.get("appr_info_no", java.lang.Long::class.java)?.toLong(),
        currApprSeq = this.get("curr_appr_seq", Integer::class.java)?.toInt(),
        apprSubmsEmpNo = this.get("appr_subms_emp_no", String::class.java),
        apprSubmsDtime = this.get("appr_subms_dtime", LocalDateTime::class.java),
        lastApprStatCd = this.get("last_appr_stat_cd", String::class.java)!!,
        apprLvlCd = this.get("appr_lvl_cd", String::class.java)
    )
}
