package com.idrsys.ailis.tst.domain.model

import com.idrsys.ailis.tst.domain.command.TestReferenceCreateCommand
import com.idrsys.ailis.tst.domain.command.TestReferenceGroupCreateCommand
import com.idrsys.ailis.tst.domain.command.TestReferenceGroupItemCreateCommand
import com.idrsys.ailis.tst.domain.command.TestReferenceGroupItemUpdateCommand
import com.idrsys.ailis.tst.domain.command.TestReferenceGroupUpdateCommand
import com.idrsys.ailis.tst.domain.command.TestReferenceUpdateCommand
import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("tst_scm.bbs_tst_ref")
class TestReference(
    refCd: String? = null,
    refCateCd: String,
    useYn: Boolean,
    refNm: String,
    refAbbrNm: String,
    refEngNm: String,
    refEngAbbrNm: String,
    sortOrder: Int,
    refType: String,
    refSize: Int,
    rangeChkYn: Boolean,
    refMinVal: Int,
    refMaxVal: Int,
    dataFormat: String,
    dftData: String,
    dftEngData: String,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDetime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("ref_cd")
    val refCd: String? = refCd

    @Column("ref_cate_cd")
    var refCateCd: String = refCateCd
        private set

    @Column("use_yn")
    var useYn: Boolean = useYn
        private set

    @Column("ref_nm")
    var refNm: String = refNm
        private set

    @Column("ref_abbr_nm")
    var refAbbrNm: String = refAbbrNm
        private set

    @Column("ref_eng_nm")
    var refEngNm: String = refEngNm
        private set

    @Column("ref_eng_abbr_nm")
    var refEngAbbrNm: String = refEngAbbrNm
        private set

    @Column("sort_order")
    var sortOrder: Int = sortOrder
        private set

    @Column("ref_type")
    var refType: String = refType
        private set

    @Column("ref_size")
    var refSize: Int = refSize
        private set

    @Column("range_chk_yn")
    var rangeChkYn: Boolean = rangeChkYn
        private set

    @Column("ref_min_val")
    var refMinVal: Int = refMinVal
        private set

    @Column("ref_max_val")
    var refMaxVal: Int = refMaxVal
        private set

    @Column("data_format")
    var dataFormat: String = dataFormat
        private set

    @Column("dft_data")
    var dftData: String = dftData
        private set

    @Column("dft_eng_data")
    var dftEngData: String = dftEngData
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

    override fun getId(): String? = refCd

    override fun isNew(): Boolean = _isNew

    fun delete(updater: String, updateDetime: LocalDateTime) {
        this.useYn = false
        this.updater = updater
        this.updateDetime = updateDetime
    }

    fun update(command: TestReferenceUpdateCommand, updater: String, updateDetime: LocalDateTime) {
        this.refCateCd = command.refCateCd
        this.useYn = command.useYn
        this.refNm = command.refNm
        this.refAbbrNm = command.refAbbrNm
        this.refEngNm = command.refEngNm
        this.refEngAbbrNm = command.refEngAbbrNm
        this.sortOrder = command.sortOrder
        this.refType = command.refType
        this.refSize = command.refSize
        this.rangeChkYn = command.rangeChkYn
        this.refMinVal = command.refMinVal
        this.refMaxVal = command.refMaxVal
        this.dataFormat = command.dataFormat
        this.dftData = command.dftData
        this.dftEngData = command.dftEngData
        this.updater = updater
        this.updateDetime = updateDetime
    }

    companion object {
        fun create(
            command: TestReferenceCreateCommand,
            creator: String,
            now: LocalDateTime
        ): TestReference {
            return TestReference(
                refCd = command.refCd,
                refCateCd = command.refCateCd,
                useYn = command.useYn,
                refNm = command.refNm,
                refAbbrNm = command.refAbbrNm,
                refEngNm = command.refEngNm,
                refEngAbbrNm = command.refEngAbbrNm,
                sortOrder = command.sortOrder,
                refType = command.refType,
                refSize = command.refSize,
                rangeChkYn = command.rangeChkYn,
                refMinVal = command.refMinVal,
                refMaxVal = command.refMaxVal,
                dataFormat = command.dataFormat,
                dftData = command.dftData,
                dftEngData = command.dftEngData,
                creator = creator,
                createDtime = now,
                updater = creator,
                updateDetime = now
            ).apply { setAsNew() }
        }
    }
}

@Table("tst_scm.bbs_tst_ref_group")
class TestReferenceGroup(
    refGroupCd: String? = null,
    refNm: String,
    refAbbrNm: String,
    refEngNm: String,
    refEngAbbrNm: String,
    sortOrder: Int,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDetime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("ref_group_cd")
    val refGroupCd: String? = refGroupCd

    @Column("ref_nm")
    var refNm: String = refNm
        private set

    @Column("ref_abbr_nm")
    var refAbbrNm: String = refAbbrNm
        private set

    @Column("ref_eng_nm")
    var refEngNm: String = refEngNm
        private set

    @Column("ref_eng_abbr_nm")
    var refEngAbbrNm: String = refEngAbbrNm
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

    override fun getId(): String? = refGroupCd

    override fun isNew(): Boolean = _isNew

    fun update(command: TestReferenceGroupUpdateCommand, updater: String, updateDetime: LocalDateTime) {
        this.refNm = command.refNm
        this.refAbbrNm = command.refAbbrNm
        this.refEngNm = command.refEngNm
        this.refEngAbbrNm = command.refEngAbbrNm
        this.sortOrder = command.sortOrder
        this.updater = updater
        this.updateDetime = updateDetime
    }

    companion object {
        fun create(
            command: TestReferenceGroupCreateCommand,
            creator: String,
            now: LocalDateTime
        ): TestReferenceGroup {
            return TestReferenceGroup(
                refGroupCd = command.refGroupCd,
                refNm = command.refNm,
                refAbbrNm = command.refAbbrNm,
                refEngNm = command.refEngNm,
                refEngAbbrNm = command.refEngAbbrNm,
                sortOrder = command.sortOrder,
                creator = creator,
                createDtime = now,
                updater = creator,
                updateDetime = now
            ).apply { setAsNew() }
        }
    }
}

@Table("tst_scm.bbs_tst_ref_group_item")
class TestReferenceGroupItem(
    tstRefGroupItemId: String? = null,
    refGroupCd: String,
    refCd: String,
    sortOrder: Int,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDetime: LocalDateTime
) : Persistable<String> {

    @Id
    @UuidGeneratedId(idFieldName = "tstRefGroupItemId")
    @Column("tst_ref_group_item_id")
    val tstRefGroupItemId: String? = tstRefGroupItemId

    @Column("ref_group_cd")
    var refGroupCd: String = refGroupCd
        private set

    @Column("ref_cd")
    var refCd: String = refCd
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

    override fun getId(): String? = tstRefGroupItemId

    override fun isNew(): Boolean = _isNew

    fun update(command: TestReferenceGroupItemUpdateCommand, updater: String, updateDetime: LocalDateTime) {
        this.refGroupCd = command.refGroupCd
        this.refCd = command.refCd
        this.sortOrder = command.sortOrder
        this.updater = updater
        this.updateDetime = updateDetime
    }

    companion object {
        fun create(
            command: TestReferenceGroupItemCreateCommand,
            creator: String,
            now: LocalDateTime
        ): TestReferenceGroupItem {
            return TestReferenceGroupItem(
                tstRefGroupItemId = null,
                refGroupCd = command.refGroupCd,
                refCd = command.refCd,
                sortOrder = command.sortOrder,
                creator = creator,
                createDtime = now,
                updater = creator,
                updateDetime = now
            ).apply { setAsNew() }
        }
    }
}
