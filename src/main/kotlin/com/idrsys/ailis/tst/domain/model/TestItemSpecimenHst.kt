package com.idrsys.ailis.tst.domain.model

import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("tst_scm.bts_spcm_hst")
class TestItemSpecimenHst(
    spcmHstId: String? = null,
    hstDesc: String,
    tstCd: String,
    spcmCd: String,
    sortOrder: Int,
    estlYn: Boolean,
    takeQnty: String,
    engTakeQnty: String,
    useQnty: String,
    engUseQnty: String,
    strgMethod: String,
    engStrgMethod: String,
    spcmStbl: String?,
    engSpcmStbl: String?,
    takeMethod: String?,
    engTakeMethod: String?,
    spcmDesc: String,
    engDesc: String?,
    caution: String,
    engCaution: String,
    spcmCntnCd: String,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime
) : Persistable<String> {

    @Id
    @UuidGeneratedId(idFieldName = "spcmHstId")
    @Column("spcm_hst_id")
    val spcmHstId: String? = spcmHstId

    @Column("hst_desc")
    var hstDesc: String = hstDesc
        private set

    @Column("tst_cd")
    var tstCd: String = tstCd
        private set

    @Column("spcm_cd")
    var spcmCd: String = spcmCd
        private set

    @Column("sort_order")
    var sortOrder: Int = sortOrder
        private set

    @Column("estl_yn")
    var estlYn: Boolean = estlYn
        private set

    @Column("take_qnty")
    var takeQnty: String = takeQnty
        private set

    @Column("eng_take_qnty")
    var engTakeQnty: String = engTakeQnty
        private set

    @Column("use_qnty")
    var useQnty: String = useQnty
        private set

    @Column("eng_use_qnty")
    var engUseQnty: String = engUseQnty
        private set

    @Column("strg_method")
    var strgMethod: String = strgMethod
        private set

    @Column("eng_strg_method")
    var engStrgMethod: String = engStrgMethod
        private set

    @Column("spcm_stbl")
    var spcmStbl: String? = spcmStbl
        private set

    @Column("eng_spcm_stbl")
    var engSpcmStbl: String? = engSpcmStbl
        private set

    @Column("take_method")
    var takeMethod: String? = takeMethod
        private set

    @Column("eng_take_method")
    var engTakeMethod: String? = engTakeMethod
        private set

    @Column("spcm_desc")
    var spcmDesc: String = spcmDesc
        private set

    @Column("eng_desc")
    var engDesc: String? = engDesc
        private set

    @Column("caution")
    var caution: String = caution
        private set

    @Column("eng_caution")
    var engCaution: String = engCaution
        private set

    @Column("spcm_cntn_cd")
    var spcmCntnCd: String = spcmCntnCd
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

    override fun getId(): String? = spcmHstId

    override fun isNew(): Boolean = _isNew
}