package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.domain.model.Exrt
import io.r2dbc.spi.Row
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

internal fun Row.toExrt(): Exrt = Exrt(
    exrtId = this.get("exrt_id", Long::class.java),
    stndDt = this.get("stnd_dt", LocalDate::class.java)!!,
    crcyCd = this.get("crcy_cd", String::class.java)!!,
    stndExrt = this.get("stnd_exrt", BigDecimal::class.java)!!,
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!
)
