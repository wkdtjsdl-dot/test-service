package com.idrsys.ailis.tst.domain.model

import com.idrsys.ailis.tst.domain.command.WorkListCreateCommand
import com.idrsys.ailis.tst.domain.command.WorkListItemCreateCommand
import com.idrsys.ailis.tst.domain.command.WorkListItemUpdateCommand
import com.idrsys.ailis.tst.domain.command.WorkListUpdateCommand
import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("tst_scm.bbs_wrklist")
class WorkList(
    wrklistCd: String,
    useYn: Boolean,
    startDt: LocalDate,
    endDt: LocalDate,
    wrklistNm: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("wrklist_cd")
    val wrklistCd: String = wrklistCd

    @Column("use_yn")
    var useYn: Boolean = useYn
        private set

    @Column("start_dt")
    var startDt: LocalDate = startDt
        private set

    @Column("end_dt")
    var endDt: LocalDate = endDt
        private set

    @Column("wrklist_nm")
    var wrklistNm: String? = wrklistNm
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
    private var isNewEntity: Boolean = false

    fun setAsNew() {
        isNewEntity = true
    }

    override fun getId(): String = wrklistCd

    override fun isNew(): Boolean = isNewEntity

    fun update(command: WorkListUpdateCommand, updater: String, now: LocalDateTime): WorkList {
        useYn = command.useYn
        startDt = command.startDt
        endDt = command.endDt
        wrklistNm = command.wrklistNm
        this.updater = updater
        updateDtime = now
        return this
    }

    companion object {
        private val WRKLIST_CODE_REGEX = Regex("^[A-Za-z0-9]+$")

        fun create(command: WorkListCreateCommand, creator: String, now: LocalDateTime): WorkList {
            require(WRKLIST_CODE_REGEX.matches(command.wrklistCd)) {
                "wrklistCd can contain only letters and numbers."
            }

            return WorkList(
                wrklistCd = command.wrklistCd,
                useYn = command.useYn,
                startDt = command.startDt,
                endDt = command.endDt,
                wrklistNm = command.wrklistNm,
                creator = creator,
                createDtime = now,
                updater = creator,
                updateDtime = now
            ).apply { setAsNew() }
        }
    }
}

@Table("tst_scm.bbs_wrklist_itm")
class WorkListItem(
    wrklistItmId: String? = null,
    wrklistCd: String,
    tstCd: String,
    spcmCd: String?,
    tstOption: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime
) : Persistable<String> {

    @Id
    @UuidGeneratedId(idFieldName = "wrklistItmId")
    @Column("wrklist_itm_id")
    val wrklistItmId: String? = wrklistItmId

    @Column("wrklist_cd")
    val wrklistCd: String = wrklistCd

    @Column("tst_cd")
    var tstCd: String = tstCd
        private set

    @Column("spcm_cd")
    var spcmCd: String? = spcmCd
        private set

    @Column("tst_option")
    var tstOption: String? = tstOption
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
    private var isNewEntity: Boolean = false

    fun setAsNew() {
        isNewEntity = true
    }

    override fun getId(): String? = wrklistItmId

    override fun isNew(): Boolean = isNewEntity

    fun update(command: WorkListItemUpdateCommand, updater: String, now: LocalDateTime): WorkListItem {
        tstCd = command.tstCd
        spcmCd = command.spcmCd
        tstOption = command.tstOption
        this.updater = updater
        updateDtime = now
        return this
    }

    companion object {
        fun create(command: WorkListItemCreateCommand, creator: String, now: LocalDateTime): WorkListItem {
            return WorkListItem(
                wrklistItmId = null,
                wrklistCd = command.wrklistCd,
                tstCd = command.tstCd,
                spcmCd = command.spcmCd,
                tstOption = command.tstOption,
                creator = creator,
                createDtime = now,
                updater = creator,
                updateDtime = now
            ).apply { setAsNew() }
        }
    }
}
