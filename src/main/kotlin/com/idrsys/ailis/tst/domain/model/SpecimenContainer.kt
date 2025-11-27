package com.idrsys.ailis.tst.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("tst_scm.bbs_spcm_cntn")
class SpecimenContainer(
    spcmCntnCd: String? = null,
    cntnNm: String,
    cntnEngNm: String,
    cntnFileId: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDetime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("spcm_cntn_cd")
    val spcmCntnCd: String? = spcmCntnCd

    @Column("cntn_nm")
    var cntnNm: String = cntnNm
        private set

    @Column("cntn_eng_nm")
    var cntnEngNm: String = cntnEngNm
        private set

    @Column("cntn_file_id")
    var cntnFileId: String? = cntnFileId
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

    override fun getId(): String? = spcmCntnCd

    override fun isNew(): Boolean = _isNew

    fun update(
        cntnNm: String,
        cntnEngNm: String,
        cntnFileId: String?,
        updater: String,
        updateDetime: LocalDateTime
    ) {
        this.cntnNm = cntnNm
        this.cntnEngNm = cntnEngNm
        this.cntnFileId = cntnFileId
        this.updater = updater
        this.updateDetime = updateDetime
    }
}
