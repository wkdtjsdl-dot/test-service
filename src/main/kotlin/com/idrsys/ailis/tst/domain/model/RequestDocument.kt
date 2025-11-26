package com.idrsys.ailis.tst.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("tst_scm.bbs_tst_req_doc")
class RequestDocument(
    docCd: String? = null,
    docDivCd: String,
    docNm: String,
    docEngNm: String,
    docFileId: String?,
    docEngFileId: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDetime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("doc_cd")
    val docCd: String? = docCd

    @Column("doc_div_cd")
    var docDivCd: String = docDivCd
        private set

    @Column("doc_nm")
    var docNm: String = docNm
        private set

    @Column("doc_eng_nm")
    var docEngNm: String = docEngNm
        private set

    @Column("doc_file_id")
    var docFileId: String? = docFileId
        private set

    @Column("doc_eng_file_id")
    var docEngFileId: String? = docEngFileId
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

    override fun getId(): String? = docCd

    override fun isNew(): Boolean = _isNew

    fun update(
        docDivCd: String,
        docNm: String,
        docEngNm: String,
        docFileId: String?,
        docEngFileId: String?,
        updater: String
    ) {
        this.docDivCd = docDivCd
        this.docNm = docNm
        this.docEngNm = docEngNm
        this.docFileId = docFileId
        this.docEngFileId = docEngFileId
        this.updater = updater
        this.updateDetime = LocalDateTime.now()
    }
}
