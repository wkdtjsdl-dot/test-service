package com.idrsys.ailis.tst.domain.model

import com.idrsys.ailis.tst.domain.command.StandardChargeCreateCommand
import com.idrsys.ailis.tst.domain.command.StandardChargeUpdateCommand
import com.idrsys.ailis.tst.domain.command.TestItemCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemEssentialDocCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemEssentialDocUpdateCommand
import com.idrsys.ailis.tst.domain.command.TestItemGeneCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemRefItemCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemRefItemUpdateCommand
import com.idrsys.ailis.tst.domain.command.TestItemSpecimenCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemUpdateCommand
import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("tst_scm.bts_item")
class TestItem(
    tstCd: String? = null,
    tstLargeCateCd: String,
    tstMediumCateCd: String,
    startDt: LocalDate,
    endDt: LocalDate,
    useYn: Boolean,
    reqPossYn: Boolean,
    webKorYn: Boolean,
    webEngYn: Boolean,
    tstNm: String,
    tstAbbrNm: String,
    tstEngNm: String,
    tstEngAbbrNm: String,
    tstIntNm: String?,
    rstTypeShortYn: Boolean,
    rstTypeLongYn: Boolean,
    rstTypeFileYn: Boolean,
    rstTypeUrlYn: Boolean,
    diseaseCd: String?,
    tstMethodCd: String?,
    refVal: String?,
    engRefVal: String?,
    clncSgnf: String?,
    engClncSgnf: String?,
    tstDesc: String?,
    tstEngDesc: String?,
    tstDayweek: String?,
    tstTatday: Int?,
    insuApplyCd: String?,
    insuCd: String?,
    insuCateNo: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDetime: LocalDateTime
) : Persistable<String> {

    @Id
    @Column("tst_cd")
    val tstCd: String? = tstCd

    @Column("tst_large_cate_cd")
    var tstLargeCateCd: String = tstLargeCateCd
        private set

    @Column("tst_medium_cate_cd")
    var tstMediumCateCd: String = tstMediumCateCd
        private set

    @Column("start_dt")
    var startDt: LocalDate = startDt
        private set

    @Column("end_dt")
    var endDt: LocalDate = endDt
        private set

    @Column("use_yn")
    var useYn: Boolean = useYn
        private set

    @Column("req_poss_yn")
    var reqPossYn: Boolean = reqPossYn
        private set

    @Column("web_kor_yn")
    var webKorYn: Boolean = webKorYn
        private set

    @Column("web_eng_yn")
    var webEngYn: Boolean = webEngYn
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

    @Column("tst_int_nm")
    var tstIntNm: String? = tstIntNm
        private set

    @Column("rst_type_short_yn")
    var rstTypeShortYn: Boolean = rstTypeShortYn
        private set

    @Column("rst_type_long_yn")
    var rstTypeLongYn: Boolean = rstTypeLongYn
        private set

    @Column("rst_type_file_yn")
    var rstTypeFileYn: Boolean = rstTypeFileYn
        private set

    @Column("rst_type_url_yn")
    var rstTypeUrlYn: Boolean = rstTypeUrlYn
        private set

    @Column("disease_cd")
    var diseaseCd: String? = diseaseCd
        private set

    @Column("tst_method_cd")
    var tstMethodCd: String? = tstMethodCd
        private set

    @Column("ref_val")
    var refVal: String? = refVal
        private set

    @Column("eng_ref_val")
    var engRefVal: String? = engRefVal
        private set

    @Column("clnc_sgnf")
    var clncSgnf: String? = clncSgnf
        private set

    @Column("eng_clnc_sgnf")
    var engClncSgnf: String? = engClncSgnf
        private set

    @Column("tst_desc")
    var tstDesc: String? = tstDesc
        private set

    @Column("tst_eng_desc")
    var tstEngDesc: String? = tstEngDesc
        private set

    @Column("tst_dayweek")
    var tstDayweek: String? = tstDayweek
        private set

    @Column("tst_tatday")
    var tstTatday: Int? = tstTatday
        private set

    @Column("insu_apply_cd")
    var insuApplyCd: String? = insuApplyCd
        private set

    @Column("insu_cd")
    var insuCd: String? = insuCd
        private set

    @Column("insu_cate_no")
    var insuCateNo: String? = insuCateNo
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

    override fun getId(): String? = tstCd

    override fun isNew(): Boolean = _isNew

    fun update(command: TestItemUpdateCommand, updater: String, updateDetime: LocalDateTime) {
        this.tstLargeCateCd = command.tstLargeCateCd
        this.tstMediumCateCd = command.tstMediumCateCd
        this.startDt = command.startDt
        this.endDt = command.endDt
        this.useYn = command.useYn
        this.reqPossYn = command.reqPossYn
        this.webKorYn = command.webKorYn
        this.webEngYn = command.webEngYn
        this.tstNm = command.tstNm
        this.tstAbbrNm = command.tstAbbrNm
        this.tstEngNm = command.tstEngNm
        this.tstEngAbbrNm = command.tstEngAbbrNm
        this.tstIntNm = command.tstIntNm
        this.rstTypeShortYn = command.rstTypeShortYn
        this.rstTypeLongYn = command.rstTypeLongYn
        this.rstTypeFileYn = command.rstTypeFileYn
        this.rstTypeUrlYn = command.rstTypeUrlYn
        this.diseaseCd = command.diseaseCd
        this.tstMethodCd = command.tstMethodCd
        this.refVal = command.refVal
        this.engRefVal = command.engRefVal
        this.clncSgnf = command.clncSgnf
        this.engClncSgnf = command.engClncSgnf
        this.tstDesc = command.tstDesc
        this.tstEngDesc = command.tstEngDesc
        this.tstDayweek = command.tstDayweek
        this.tstTatday = command.tstTatday
        this.insuApplyCd = command.insuApplyCd
        this.insuCd = command.insuCd
        this.insuCateNo = command.insuCateNo
        this.updater = updater
        this.updateDetime = updateDetime
    }

    companion object {
        fun create(
            command: TestItemCreateCommand,
            creator: String,
            now: LocalDateTime
        ): TestItem {
            return TestItem(
                tstCd = command.tstCd,
                tstLargeCateCd = command.tstLargeCateCd,
                tstMediumCateCd = command.tstMediumCateCd,
                startDt = command.startDt,
                endDt = command.endDt,
                useYn = command.useYn,
                reqPossYn = command.reqPossYn,
                webKorYn = command.webKorYn,
                webEngYn = command.webEngYn,
                tstNm = command.tstNm,
                tstAbbrNm = command.tstAbbrNm,
                tstEngNm = command.tstEngNm,
                tstEngAbbrNm = command.tstEngAbbrNm,
                tstIntNm = command.tstIntNm,
                rstTypeShortYn = command.rstTypeShortYn,
                rstTypeLongYn = command.rstTypeLongYn,
                rstTypeFileYn = command.rstTypeFileYn,
                rstTypeUrlYn = command.rstTypeUrlYn,
                diseaseCd = command.diseaseCd,
                tstMethodCd = command.tstMethodCd,
                refVal = command.refVal,
                engRefVal = command.engRefVal,
                clncSgnf = command.clncSgnf,
                engClncSgnf = command.engClncSgnf,
                tstDesc = command.tstDesc,
                tstEngDesc = command.tstEngDesc,
                tstDayweek = command.tstDayweek,
                tstTatday = command.tstTatday,
                insuApplyCd = command.insuApplyCd,
                insuCd = command.insuCd,
                insuCateNo = command.insuCateNo,
                creator = creator,
                createDtime = now,
                updater = creator,
                updateDetime = now
            ).apply { setAsNew() }
        }
    }
}

@Table("tst_scm.bts_stnd_charge")
class StandardCharge(
    stndChargeId: String? = null,
    tstCd: String,
    applyStartDt: LocalDate,
    applyEndDt: LocalDate,
    insuCd: String?,
    insuCateNo: String?,
    relatValuePoint: Double?,
    insuCharge: Double,
    qladCharge: Double,
    stndCharge: Double,
    lowestCharge: Double,
    qladCd: String?,
    relatValueQladPoint: Double,
    outputInsuCd: String?,
    totalQladCharge: Double,
    supval: Double,
    addtax: Double,
    creator: String,
    createDtime: LocalDateTime
) : Persistable<String> {

    @Id
    @UuidGeneratedId(idFieldName = "stndChargeId")
    @Column("stnd_charge_id")
    val stndChargeId: String? = stndChargeId

    @Column("tst_cd")
    var tstCd: String = tstCd
        private set

    @Column("apply_start_dt")
    var applyStartDt: LocalDate = applyStartDt
        private set

    @Column("apply_end_dt")
    var applyEndDt: LocalDate = applyEndDt
        private set

    @Column("insu_cd")
    var insuCd: String? = insuCd
        private set

    @Column("insu_cate_no")
    var insuCateNo: String? = insuCateNo
        private set

    @Column("relat_value_point")
    var relatValuePoint: Double? = relatValuePoint
        private set

    @Column("insu_charge")
    var insuCharge: Double = insuCharge
        private set

    @Column("qlad_charge")
    var qladCharge: Double = qladCharge
        private set

    @Column("stnd_charge")
    var stndCharge: Double = stndCharge
        private set

    @Column("lowest_charge")
    var lowestCharge: Double = lowestCharge
        private set

    @Column("qlad_cd")
    var qladCd: String? = qladCd
        private set

    @Column("relat_value_qlad_point")
    var relatValueQladPoint: Double = relatValueQladPoint
        private set

    @Column("output_insu_cd")
    var outputInsuCd: String? = outputInsuCd
        private set

    @Column("total_qlad_charge")
    var totalQladCharge: Double = totalQladCharge
        private set

    @Column("supval")
    var supval: Double = supval
        private set

    @Column("addtax")
    var addtax: Double = addtax
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

    override fun getId(): String? = stndChargeId

    override fun isNew(): Boolean = _isNew

    fun update(
        command: StandardChargeUpdateCommand,
        updater: String,
        updateDetime: LocalDateTime
    ) {
        this.applyStartDt = command.applyStartDt
        this.applyEndDt = command.applyEndDt
        this.insuCd = command.insuCd
        this.insuCateNo = command.insuCateNo
        this.relatValuePoint = command.relatValuePoint
        this.insuCharge = command.insuCharge
        this.qladCharge = command.qladCharge
        this.stndCharge = command.stndCharge
        this.lowestCharge = command.lowestCharge
        this.qladCd = command.qladCd
        this.relatValueQladPoint = command.relatValueQladPoint
        this.outputInsuCd = command.outputInsuCd
        this.totalQladCharge = command.totalQladCharge
        this.supval = command.supval
        this.addtax = command.addtax
    }

    companion object {
        fun create(
            command: StandardChargeCreateCommand,
            creator: String,
            now: LocalDateTime
        ): StandardCharge {
            return StandardCharge(
                stndChargeId = null,
                tstCd = command.tstCd,
                applyStartDt = command.applyStartDt,
                applyEndDt = command.applyEndDt,
                insuCd = command.insuCd,
                insuCateNo = command.insuCateNo,
                relatValuePoint = command.relatValuePoint,
                insuCharge = command.insuCharge,
                qladCharge = command.qladCharge,
                stndCharge = command.stndCharge,
                lowestCharge = command.lowestCharge,
                qladCd = command.qladCd,
                relatValueQladPoint = command.relatValueQladPoint,
                outputInsuCd = command.outputInsuCd,
                totalQladCharge = command.totalQladCharge,
                supval = command.supval,
                addtax = command.addtax,
                creator = creator,
                createDtime = now
            ).apply { setAsNew() }
        }
    }
}

@Table("tst_scm.bts_spcm")
class TestItemSpecimen(
    spcmId: String? = null,
    tstCd: String,
    spcmCd: String,
    sortOrder: Int,
    estlYn: Boolean,
    takeQnty: String,
    engTakeQnty: String,
    useQnty: String,
    engUseQnty: String,
    strgMethodCd: String,
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
    updater: String?,
    updateDetime: LocalDateTime?
) : Persistable<String> {

    @Id
    @UuidGeneratedId(idFieldName = "spcmId")
    @Column("spcm_id")
    val spcmId: String? = spcmId

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

    @Column("strg_method_cd")
    var strgMethodCd: String = strgMethodCd
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
    var updater: String? = updater
        private set

    @Column("update_detime")
    var updateDetime: LocalDateTime? = updateDetime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = spcmId

    override fun isNew(): Boolean = _isNew

    companion object {
        fun create(
            command: TestItemSpecimenCreateCommand,
            creator: String,
            now: LocalDateTime
        ): TestItemSpecimen {
            return TestItemSpecimen(
                spcmId = null,
                tstCd = command.tstCd,
                spcmCd = command.spcmCd,
                sortOrder = command.sortOrder,
                estlYn = command.estlYn,
                takeQnty = command.takeQnty,
                engTakeQnty = command.engTakeQnty,
                useQnty = command.useQnty,
                engUseQnty = command.engUseQnty,
                strgMethodCd = command.strgMethodCd,
                spcmStbl = command.spcmStbl,
                engSpcmStbl = command.engSpcmStbl,
                takeMethod = command.takeMethod,
                engTakeMethod = command.engTakeMethod,
                spcmDesc = command.spcmDesc,
                engDesc = command.engDesc,
                caution = command.caution,
                engCaution = command.engCaution,
                spcmCntnCd = command.spcmCntnCd,
                creator = creator,
                createDtime = now,
                updater = null,
                updateDetime = null
            ).apply { setAsNew() }
        }
    }
}

@Table("tst_scm.bts_ref_item")
class TestItemRefItem(
    refItemId: String? = null,
    tstCd: String,
    refCd: String,
    estlYn: Boolean,
    sortOrder: Int,
    creator: String,
    createDtime: LocalDateTime,
    updater: String?,
    updateDetime: LocalDateTime?
) : Persistable<String> {

    @Id
    @UuidGeneratedId(idFieldName = "refItemId")
    @Column("ref_item_id")
    val refItemId: String? = refItemId

    @Column("tst_cd")
    var tstCd: String = tstCd
        private set

    @Column("ref_cd")
    var refCd: String = refCd
        private set

    @Column("estl_yn")
    var estlYn: Boolean = estlYn
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
    var updater: String? = updater
        private set

    @Column("update_detime")
    var updateDetime: LocalDateTime? = updateDetime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = refItemId

    override fun isNew(): Boolean = _isNew

    fun update(
        command: TestItemRefItemUpdateCommand,
        updater: String,
        updateDetime: LocalDateTime
    ) {
        this.estlYn = command.estlYn
        this.sortOrder = command.sortOrder
        this.updater = updater
        this.updateDetime = updateDetime
    }

    companion object {
        fun create(
            command: TestItemRefItemCreateCommand,
            creator: String,
            now: LocalDateTime
        ): TestItemRefItem {
            return TestItemRefItem(
                refItemId = null,
                tstCd = command.tstCd,
                refCd = command.refCd,
                estlYn = command.estlYn,
                sortOrder = command.sortOrder,
                creator = creator,
                createDtime = now,
                updater = null,
                updateDetime = null
            ).apply { setAsNew() }
        }
    }
}

@Table("tst_scm.bts_item_gene")
class TestItemGene(
    itemGeneId: String? = null,
    tstCd: String,
    geneCd: String,
    creator: String,
    createDtime: LocalDateTime
) : Persistable<String> {

    @Id
    @UuidGeneratedId(idFieldName = "itemGeneId")
    @Column("item_gene_id")
    val itemGeneId: String? = itemGeneId

    @Column("tst_cd")
    var tstCd: String = tstCd
        private set

    @Column("gene_cd")
    var geneCd: String = geneCd
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

    override fun getId(): String? = itemGeneId

    override fun isNew(): Boolean = _isNew

    companion object {
        fun create(
            command: TestItemGeneCreateCommand,
            creator: String,
            now: LocalDateTime
        ): TestItemGene {
            return TestItemGene(
                itemGeneId = null,
                tstCd = command.tstCd,
                geneCd = command.geneCd,
                creator = creator,
                createDtime = now
            ).apply { setAsNew() }
        }
    }
}

@Table("tst_scm.bts_item_estl_doc")
class TestItemEssentialDoc(
    itemEstlDocId: String? = null,
    tstCd: String,
    docCd: String,
    creator: String,
    createDtime: LocalDateTime,
    updater: String? = null,
    updateDetime: LocalDateTime? = null
) : Persistable<String> {

    @Id
    @UuidGeneratedId(idFieldName = "itemEstlDocId")
    @Column("item_estl_doc_id")
    val itemEstlDocId: String? = itemEstlDocId

    @Column("tst_cd")
    var tstCd: String = tstCd
        private set

    @Column("doc_cd")
    var docCd: String = docCd
        private set

    @Column("creator")
    var creator: String = creator
        private set

    @Column("create_dtime")
    var createDtime: LocalDateTime = createDtime
        private set

    @Column("updater")
    var updater: String? = updater
        private set

    @Column("update_detime")
    var updateDetime: LocalDateTime? = updateDetime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    fun update(
        command: TestItemEssentialDocUpdateCommand,
        updater: String,
        now: LocalDateTime
    ) {
        this.docCd = command.docCd
        this.updater = updater
        this.updateDetime = now
    }

    override fun getId(): String? = itemEstlDocId

    override fun isNew(): Boolean = _isNew

    companion object {
        fun create(
            command: TestItemEssentialDocCreateCommand,
            creator: String,
            now: LocalDateTime
        ): TestItemEssentialDoc {
            return TestItemEssentialDoc(
                itemEstlDocId = null,
                tstCd = command.tstCd,
                docCd = command.docCd,
                creator = creator,
                createDtime = now
            ).apply { setAsNew() }
        }
    }
}
