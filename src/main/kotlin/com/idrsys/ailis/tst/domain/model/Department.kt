package com.idrsys.ailis.tst.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("tst_scm.bbs_dept_group")
class DepartmentGroup(
    deptGroupId: String? = null,
    deptCd: String,
    tstCateCd: String,
    tstCateNm: String,
    updateAuthCd: String,
    dupAllowYn: Boolean,
    sortOrder: Int,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("dept_group_id")
    val deptGroupId: String? = deptGroupId

    @Column("dept_cd")
    var deptCd: String = deptCd
        private set

    @Column("tst_cate_cd")
    var tstCateCd: String = tstCateCd
        private set

    @Column("tst_cate_nm")
    var tstCateNm: String = tstCateNm
        private set

    @Column("update_auth_cd")
    var updateAuthCd: String = updateAuthCd
        private set

    @Column("dup_allow_yn")
    var dupAllowYn: Boolean = dupAllowYn
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
    var updateDtime: LocalDateTime = updateDtime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = deptGroupId

    override fun isNew(): Boolean = _isNew
}

@Table("tst_scm.bbs_dept_grp_itm")
class DepartmentGroupItem(
    deptGrpItmId: String? = null,
    deptCd: String,
    tstCateCd: String,
    tstCateItemCd: String,
    tstCateItemNm: String,
    sortOrder: Int,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("dept_grp_itm_id")
    val deptGrpItmId: String? = deptGrpItmId

    @Column("dept_cd")
    var deptCd: String = deptCd
        private set

    @Column("tst_cate_cd")
    var tstCateCd: String = tstCateCd
        private set

    @Column("tst_cate_item_cd")
    var tstCateItemCd: String = tstCateItemCd
        private set

    @Column("tst_cate_item_nm")
    var tstCateItemNm: String = tstCateItemNm
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
    var updateDtime: LocalDateTime = updateDtime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = deptGrpItmId

    override fun isNew(): Boolean = _isNew
}

@Table("tst_scm.bbs_dept_grp_itm_tst")
class DepartmentGroupItemTest(
    deptGrpItmTstId: String? = null,
    deptCd: String,
    tstCateCd: String,
    tstCateItemCd: String,
    tstCd: String,
    creator: String,
    createDtime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("dept_grp_itm_tst_id")
    val deptGrpItmTstId: String? = deptGrpItmTstId

    @Column("dept_cd")
    var deptCd: String = deptCd
        private set

    @Column("tst_cate_cd")
    var tstCateCd: String = tstCateCd
        private set

    @Column("tst_cate_item_cd")
    var tstCateItemCd: String = tstCateItemCd
        private set

    @Column("tst_cd")
    var tstCd: String = tstCd
        private set

    @Column("creator")
    var creator: String = creator
        private set

    @Column("create_dtime")
    var createDtime: LocalDateTime = createDtime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = deptGrpItmTstId

    override fun isNew(): Boolean = _isNew
}

@Table("tst_scm.bbs_dept_tst_item")
class DepartmentTestItem(
    deptTstItemId: String? = null,
    deptCd: String,
    tstCd: String,
    tstNm: String,
    tstAbbrNm: String,
    tstEngNm: String,
    tstEngAbbrNm: String,
    sortOrder: Int,
    useYn: Boolean,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("dept_tst_item_id")
    val deptTstItemId: String? = deptTstItemId

    @Column("dept_cd")
    var deptCd: String = deptCd
        private set

    @Column("tst_cd")
    var tstCd: String = tstCd
        private set

    @Column("tst_nm")
    var tstNm: String = tstNm
        private set

    @Column("tst_abbr_nm")
    var tstAbbrNm: String = tstAbbrNm
        private set

    @Column("tst_eng_nm")
    var tstEngNm: String = tstEngNm
        private set

    @Column("tst_eng_abbr_nm")
    var tstEngAbbrNm: String = tstEngAbbrNm
        private set

    @Column("sort_order")
    var sortOrder: Int = sortOrder
        private set

    @Column("use_yn")
    var useYn: Boolean = useYn
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
    var updateDtime: LocalDateTime = updateDtime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = deptTstItemId

    override fun isNew(): Boolean = _isNew
}
