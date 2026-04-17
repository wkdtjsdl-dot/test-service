package com.idrsys.ailis.sales.domain.model

import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Table("sales_scm.sbl_demand_hst")
class DemandHst(
    demandHstId: String? = null,
    hstCd: String,
    hstMemo: String,
    worker: String,
    workDtime: LocalDateTime,
    demandId: String,
    demandDt: LocalDate,
    custCd: String,
    demandStartDt: LocalDate,
    demandEndDt: LocalDate,
    stndPrice: BigDecimal,
    supval: BigDecimal,
    demandCharge: BigDecimal,
    addtax: BigDecimal,
    dscntRate: BigDecimal,
    demandCreateDtime: LocalDateTime?,
    demandCreatorEmpNo: String?,
    insurePrice: BigDecimal?,
    invcOutputDtime: LocalDateTime?,
    invcOutputEmpno: String?,
    slstmtNo: String?,
    slstmtSendDt: LocalDate?,
    slstmtSendEmpNo: String?,
    demandMemo: String?,
    sapCustCd: String?,
    billPublYn: Boolean,
    invcRecpEmailAddr: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
    colledgerId: String?,
    crcyCd: String? = null,
    frgnCrcyAmt: BigDecimal? = null,
    demandType: String? = null,
) {
    @Id
    @UuidGeneratedId(idFieldName = "demandHstId")
    @Column("demand_hst_id")
    val demandHstId: String? = demandHstId

    @Column("hst_cd")
    val hstCd: String = hstCd

    @Column("hst_memo")
    val hstMemo: String = hstMemo

    @Column("worker")
    val worker: String = worker

    @Column("work_dtime")
    val workDtime: LocalDateTime = workDtime

    @Column("demand_id")
    val demandId: String = demandId

    @Column("demand_dt")
    val demandDt: LocalDate = demandDt

    @Column("cust_cd")
    val custCd: String = custCd

    @Column("demand_start_dt")
    val demandStartDt: LocalDate = demandStartDt

    @Column("demand_end_dt")
    val demandEndDt: LocalDate = demandEndDt

    @Column("stnd_price")
    val stndPrice: BigDecimal = stndPrice

    @Column("supval")
    val supval: BigDecimal = supval

    @Column("demand_charge")
    val demandCharge: BigDecimal = demandCharge

    @Column("addtax")
    val addtax: BigDecimal = addtax

    @Column("dscnt_rate")
    val dscntRate: BigDecimal = dscntRate

    @Column("demand_create_dtime")
    val demandCreateDtime: LocalDateTime? = demandCreateDtime

    @Column("demand_creator_emp_no")
    val demandCreatorEmpNo: String? = demandCreatorEmpNo

    @Column("insure_price")
    val insurePrice: BigDecimal? = insurePrice

    @Column("invc_output_dtime")
    val invcOutputDtime: LocalDateTime? = invcOutputDtime

    @Column("invc_output_empno")
    val invcOutputEmpno: String? = invcOutputEmpno

    @Column("slstmt_no")
    val slstmtNo: String? = slstmtNo

    @Column("slstmt_send_dt")
    val slstmtSendDt: LocalDate? = slstmtSendDt

    @Column("slstmt_send_emp_no")
    val slstmtSendEmpNo: String? = slstmtSendEmpNo

    @Column("demand_memo")
    val demandMemo: String? = demandMemo

    @Column("sap_cust_cd")
    val sapCustCd: String? = sapCustCd

    @Column("bill_publ_yn")
    val billPublYn: Boolean = billPublYn

    @Column("invc_recp_email_addr")
    val invcRecpEmailAddr: String? = invcRecpEmailAddr

    @Column("creator")
    val creator: String = creator

    @Column("create_dtime")
    val createDtime: LocalDateTime = createDtime

    @Column("updater")
    val updater: String = updater

    @Column("update_dtime")
    val updateDtime: LocalDateTime = updateDtime

    @Column("colledger_id")
    val colledgerId: String? = colledgerId

    @Column("curr_cd")
    val crcyCd: String? = crcyCd

    @Column("fx_demand_charge")
    val frgnCrcyAmt: BigDecimal? = frgnCrcyAmt

    @Column("demand_type")
    val demandType: String? = demandType
}
