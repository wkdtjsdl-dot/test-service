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
    collAmt: String?,
    engCollAmt: String?,
    spcmStrg: String?,
    engSpcmStrg: String?,
    spcmSafe: String?,
    engSpcmSafe: String?,
    caution: String?,
    engCaution: String?,
    ref: String?,
    engRef: String?,
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

    @Column("coll_amt")
    var collAmt: String? = collAmt
        private set

    @Column("eng_coll_amt")
    var engCollAmt: String? = engCollAmt
        private set

    @Column("spcm_strg")
    var spcmStrg: String? = spcmStrg
        private set

    @Column("eng_spcm_strg")
    var engSpcmStrg: String? = engSpcmStrg
        private set

    @Column("spcm_safe")
    var spcmSafe: String? = spcmSafe
        private set

    @Column("eng_spcm_safe")
    var engSpcmSafe: String? = engSpcmSafe
        private set

    @Column("caution")
    var caution: String? = caution
        private set

    @Column("eng_caution")
    var engCaution: String? = engCaution
        private set

    @Column("ref")
    var ref: String? = ref
        private set

    @Column("eng_ref")
    var engRef: String? = engRef
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
        this.collAmt = command.collAmt
        this.engCollAmt = command.engCollAmt
        this.spcmStrg = command.spcmStrg
        this.engSpcmStrg = command.engSpcmStrg
        this.spcmSafe = command.spcmSafe
        this.engSpcmSafe = command.engSpcmSafe
        this.caution = command.caution
        this.engCaution = command.engCaution
        this.ref = command.ref
        this.engRef = command.engRef
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
                collAmt = command.collAmt,
                engCollAmt = command.engCollAmt,
                spcmStrg = command.spcmStrg,
                engSpcmStrg = command.engSpcmStrg,
                spcmSafe = command.spcmSafe,
                engSpcmSafe = command.engSpcmSafe,
                caution = command.caution,
                engCaution = command.engCaution,
                ref = command.ref,
                engRef = command.engRef,
                creator = creator,
                createDtime = now,
                updater = creator,
                updateDtime = now
            ).apply { setAsNew() }
        }
    }
}
