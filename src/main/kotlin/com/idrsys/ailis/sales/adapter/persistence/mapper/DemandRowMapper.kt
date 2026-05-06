package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.DemandMonthlyInfo
import com.idrsys.ailis.sales.application.dto.query.DemandWithCustInfo
import com.idrsys.ailis.sales.domain.model.Demand
import io.r2dbc.spi.Row
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

internal fun Row.toDemand(): Demand {
    val demand = Demand(
        demandId = this.get("demand_id", String::class.java),
        demandDt = this.get("demand_dt", LocalDate::class.java)!!,
        custCd = this.get("cust_cd", String::class.java)!!,
        demandStartDt = this.get("demand_start_dt", LocalDate::class.java)!!,
        demandEndDt = this.get("demand_end_dt", LocalDate::class.java)!!,
        stndPrice = this.get("stnd_price", BigDecimal::class.java)!!,
        supval = this.get("supval", BigDecimal::class.java)!!,
        demandCharge = this.get("demand_charge", BigDecimal::class.java)!!,
        addtax = this.get("addtax", BigDecimal::class.java)!!,
        dscntRate = this.get("dscnt_rate", BigDecimal::class.java)!!,
        demandCreateDtime = this.get("demand_create_dtime", LocalDateTime::class.java)!!,
        demandCreatorEmpNo = this.get("demand_creator_emp_no", String::class.java),
        insurePrice = this.get("insure_price", BigDecimal::class.java),
        invcOutputDtime = this.get("invc_output_dtime", LocalDateTime::class.java),
        invcOutputEmpno = this.get("invc_output_empno", String::class.java),
        demandMemo = this.get("demand_memo", String::class.java),
        sapCustCd = this.get("sap_cust_cd", String::class.java),
        billPublYn = (this.get("bill_publ_yn") as? Boolean) ?: false,
        invcRecpEmailAddr = this.get("invc_recp_email_addr", String::class.java),
        creator = this.get("creator", String::class.java)!!,
        createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
        updater = this.get("updater", String::class.java)!!,
        updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!,
        colledgerId = this.get("colledger_id", String::class.java),
        crcyCd = this.get("curr_cd", String::class.java),
        frgnCrcyAmt = this.get("fx_demand_charge", BigDecimal::class.java),
        demandType = this.get("demand_type", String::class.java)
    )

    if (demand.demandId != null) {
        demand.setAsExisting()
    }

    return demand
}

internal fun Row.toDemandMonthlyInfo(): DemandMonthlyInfo {
    return DemandMonthlyInfo(
        demandId = this.get("demand_id", String::class.java)!!,
        custCd = this.get("cust_cd", String::class.java)!!,
        demandType = this.get("demand_type", String::class.java) ?: "10",
        crcyCd = this.get("curr_cd", String::class.java),
        stndPrice = this.get("stnd_price", BigDecimal::class.java)!!,
        supval = this.get("supval", BigDecimal::class.java)!!,
        addtax = this.get("addtax", BigDecimal::class.java)!!,
        demandCharge = this.get("demand_charge", BigDecimal::class.java)!!,
        colledgerId = this.get("colledger_id", String::class.java)
    )
}

internal fun Row.toDemandWithCustInfo(): DemandWithCustInfo {
    return DemandWithCustInfo(
        demandId = this.get("demand_id", String::class.java)!!,
        custCd = this.get("cust_cd", String::class.java)!!,
        demandDt = this.get("demand_dt", LocalDate::class.java)!!,
        demandStartDt = this.get("demand_start_dt", LocalDate::class.java)!!,
        demandEndDt = this.get("demand_end_dt", LocalDate::class.java)!!,
        demandCreateDtime = this.get("demand_create_dtime", LocalDateTime::class.java)!!,
        demandCreatorEmpNo = this.get("demand_creator_emp_no", String::class.java),
        stndPrice = this.get("stnd_price", BigDecimal::class.java)!!,
        supval = this.get("supval", BigDecimal::class.java)!!,
        addtax = this.get("addtax", BigDecimal::class.java)!!,
        demandCharge = this.get("demand_charge", BigDecimal::class.java)!!,
        dscntRate = this.get("dscnt_rate", BigDecimal::class.java)!!,
        slstmtNo = this.get("slstmt_no", String::class.java),
        slstmtSendDt = this.get("slstmt_send_dt", LocalDate::class.java),
        billPublYn = (this.get("bill_publ_yn") as? Boolean) ?: false,
        invcRecpEmailAddr = this.get("invc_recp_email_addr", String::class.java),
        demandMemo = this.get("demand_memo", String::class.java),
        creator = this.get("creator", String::class.java)!!,
        createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
        colledgerId = this.get("colledger_id", String::class.java),
        custNm = this.get("cust_nm", String::class.java),
        bzoffiCd = this.get("bzoffi_cd", String::class.java),
        sapCustCd = this.get("sap_cust_cd", String::class.java),
        crcyCd = this.get("curr_cd", String::class.java),
        frgnCrcyAmt = this.get("fx_demand_charge", BigDecimal::class.java),
        demandType = this.get("demand_type", String::class.java)
    )
}
