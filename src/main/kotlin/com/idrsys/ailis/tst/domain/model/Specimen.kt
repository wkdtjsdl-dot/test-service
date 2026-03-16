package com.idrsys.ailis.tst.domain.model

import com.idrsys.ailis.tst.domain.command.SpecimenCreateCommand
import com.idrsys.ailis.tst.domain.command.SpecimenUpdateCommand
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("tst_scm.bbs_spcm")
class Specimen(
    spcmCd: String? = null,
    spcmCateCd: String?,
    useYn: Boolean,
    spcmNm: String,
    spcmAbbrNm: String?,
    spcmEngNm: String,
    spcmEngAbbrNm: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("spcm_cd")
    val spcmCd: String? = spcmCd

    @Column("spcm_cate_cd")
    var spcmCateCd: String? = spcmCateCd
        private set

    @Column("use_yn")
    var useYn: Boolean = useYn
        private set

    @Column("spcm_nm")
    var spcmNm: String = spcmNm
        private set

    @Column("spcm_abbr_nm")
    var spcmAbbrNm: String? = spcmAbbrNm
        private set

    @Column("spcm_eng_nm")
    var spcmEngNm: String = spcmEngNm
        private set

    @Column("spcm_eng_abbr_nm")
    var spcmEngAbbrNm: String? = spcmEngAbbrNm
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

    override fun getId(): String? = spcmCd

    override fun isNew(): Boolean = _isNew

    fun update(
        command: SpecimenUpdateCommand,
        updater: String,
        updateDtime: LocalDateTime
    ) {
        this.spcmCateCd = command.spcmCateCd
        this.useYn = command.useYn
        this.spcmNm = command.spcmNm
        this.spcmAbbrNm = command.spcmAbbrNm
        this.spcmEngNm = command.spcmEngNm
        this.spcmEngAbbrNm = command.spcmEngAbbrNm
        this.updater = updater
        this.updateDtime = updateDtime
    }

    fun delete(updater: String, updateDtime: LocalDateTime) {
        this.useYn = false
        this.updater = updater
        this.updateDtime = updateDtime
    }

    companion object {
        fun create(
            command: SpecimenCreateCommand,
            creator: String,
            now: LocalDateTime
        ): Specimen {
            return Specimen(
                spcmCd = command.spcmCd,
                spcmCateCd = command.spcmCateCd,
                useYn = command.useYn,
                spcmNm = command.spcmNm,
                spcmAbbrNm = command.spcmAbbrNm,
                spcmEngNm = command.spcmEngNm,
                spcmEngAbbrNm = command.spcmEngAbbrNm,
                creator = creator,
                createDtime = now,
                updater = creator,
                updateDtime = now
            ).apply { setAsNew() }
        }
    }
}
