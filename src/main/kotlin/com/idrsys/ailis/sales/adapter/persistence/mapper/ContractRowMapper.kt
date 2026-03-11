package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.domain.model.Contract
import io.r2dbc.spi.Row
import java.time.LocalDate
import java.time.LocalDateTime

internal fun Row.toContract(): Contract = Contract(
    custCntrId = this.get("cust_cntr_id", Long::class.java),
    custMstId = this.get("cust_mst_id", String::class.java)!!,
    custCd = this.get("cust_cd", String::class.java)!!,
    cntrNo = this.get("cntr_no", String::class.java),
    cntrDt = this.get("cntr_dt", LocalDate::class.java),
    cntrStartDt = this.get("cntr_start_dt", LocalDate::class.java),
    cntrEndDt = this.get("cntr_end_dt", LocalDate::class.java),
    cntrType = this.get("cntr_type_cd", String::class.java),
    recntrMonth = this.get("recntr_month", String::class.java),
    cntrNm = this.get("cntr_nm", String::class.java),
    cntrCont = this.get("cntr_cont", String::class.java),
    cntrPicId = this.get("cntr_pic_id", String::class.java),
    atchGrupId = this.get("atch_grup_id", String::class.java)!!,
    useYn = this.get("use_yn", Boolean::class.java)!!,
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)
