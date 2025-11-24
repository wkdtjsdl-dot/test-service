package com.idrsys.ailis.sales.domain.model

import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtCommand
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Table("sales_scm.scs_exrt")
class Exrt(
    exrtId: Long? = null,
    stndDt: LocalDate,
    crcyCd: String,
    stndExrt: BigDecimal,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) : Persistable<Long> {

    @Id
    @Column("exrt_id")
    val exrtId: Long? = exrtId

    @Column("stnd_dt")
    var stndDt: LocalDate = stndDt
        private set

    @Column("crcy_cd")
    var crcyCd: String = crcyCd
        private set

    @Column("stnd_exrt")
    var stndExrt: BigDecimal = stndExrt
        private set

    @Column("creator")
    var creator: String = creator
        private set

    @Column("create_dtime")
    var createDtime: LocalDateTime = createDtime
        private set

    @Column("updater")
    var updater: String = updater
        private set

    @Column("update_dtime")
    var updateDtime: LocalDateTime = updateDtime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): Long? = exrtId

    override fun isNew(): Boolean = _isNew

    fun update(command: ExrtCommand, updater: String) {
        this.stndDt = command.stndDt
        this.crcyCd = command.crcyCd
        this.stndExrt = command.stndExrt
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }

    fun updateStndExrt(newStndExrt: BigDecimal, updater: String) {
        this.stndExrt = newStndExrt
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
