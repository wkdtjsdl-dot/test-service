package com.idrsys.ailis.tst.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("tst_scm.bbs_tst_cate")
class TestCategory(
    tstCateId: String? = null,
    tstLargeCateCd: String,
    tstMediumCateCd: String,
    cateNm: String,
    cateAbbrNm: String,
    cateEngNm: String,
    cateEngAbbrNm: String,
    useYn: Boolean,
    sortOrder: Int,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDetime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("tst_cate_id")
    val tstCateId: String? = tstCateId

    @Column("tst_large_cate_cd")
    val tstLargeCateCd: String = tstLargeCateCd

    @Column("tst_medium_cate_cd")
    var tstMediumCateCd: String = tstMediumCateCd
        private set

    @Column("cate_nm")
    var cateNm: String = cateNm
        private set

    @Column("cate_abbr_nm")
    var cateAbbrNm: String = cateAbbrNm
        private set

    @Column("cate_eng_nm")
    var cateEngNm: String = cateEngNm
        private set

    @Column("cate_eng_abbr_nm")
    var cateEngAbbrNm: String = cateEngAbbrNm
        private set

    @Column("use_yn")
    var useYn: Boolean = useYn
        private set

    @Column("sort_order")
    var sortOrder: Int = sortOrder
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

    override fun getId(): String? = tstCateId

    override fun isNew(): Boolean = _isNew

    fun update(
        cateNm: String,
        cateAbbrNm: String,
        cateEngNm: String,
        cateEngAbbrNm: String,
        useYn: Boolean,
        sortOrder: Int,
        updater: String,
        updateDetime: LocalDateTime
    ) {
        this.cateNm = cateNm
        this.cateAbbrNm = cateAbbrNm
        this.cateEngNm = cateEngNm
        this.cateEngAbbrNm = cateEngAbbrNm
        this.useYn = useYn
        this.sortOrder = sortOrder
        this.updater = updater
        this.updateDetime = updateDetime
    }

    fun delete(updater: String, updateDetime: LocalDateTime) {
        this.useYn = false
        this.updater = updater
        this.updateDetime = updateDetime
    }
}
