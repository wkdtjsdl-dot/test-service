package com.idrsys.ailis.tst.domain.model

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
    updateDetime: LocalDateTime
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

    @Column("update_detime")
    var updateDetime: LocalDateTime = updateDetime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = spcmCd

    override fun isNew(): Boolean = _isNew

    fun update(
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
        updater: String
    ) {
        this.spcmCateCd = spcmCateCd
        this.useYn = useYn
        this.spcmNm = spcmNm
        this.spcmAbbrNm = spcmAbbrNm
        this.spcmEngNm = spcmEngNm
        this.spcmEngAbbrNm = spcmEngAbbrNm
        this.collAmt = collAmt
        this.engCollAmt = engCollAmt
        this.spcmStrg = spcmStrg
        this.engSpcmStrg = engSpcmStrg
        this.spcmSafe = spcmSafe
        this.engSpcmSafe = engSpcmSafe
        this.caution = caution
        this.engCaution = engCaution
        this.ref = ref
        this.engRef = engRef
        this.updater = updater
        this.updateDetime = LocalDateTime.now()
    }

    fun delete(updater: String) {
        this.useYn = false
        this.updater = updater
        this.updateDetime = LocalDateTime.now()
    }
}
