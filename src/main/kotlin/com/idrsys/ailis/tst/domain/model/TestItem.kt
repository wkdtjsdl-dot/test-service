package com.idrsys.ailis.tst.domain.model

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
    webYn: Boolean,
    tstNm: String,
    tstAbbrNm: String,
    tstEngNm: String,
    tstEngAbbrNm: String,
    tstIntNm: String,
    rstTypeShortYn: Boolean,
    rstTypeLongYn: Boolean,
    rstTypeFileYn: Boolean,
    rstTypeUrlYn: Boolean,
    diseaseCd: String,
    tstMethodCd: String?,
    refVal: String,
    engRefVal: String,
    clncSgnf: String,
    engClncSgnf: String,
    tstDesc: String,
    tstEngDesc: String,
    tstDayweek: String,
    tstTatday: Int,
    insuApplyCd: String,
    insuCd: String,
    insuCateNo: String,
    creator: String,
    createDtime: LocalDateTime,
    updater: String?,
    updateDetime: LocalDateTime?
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

    @Column("web_yn")
    var webYn: Boolean = webYn
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
    var tstIntNm: String = tstIntNm
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
    var diseaseCd: String = diseaseCd
        private set

    @Column("tst_method_cd")
    var tstMethodCd: String? = tstMethodCd
        private set

    @Column("ref_val")
    var refVal: String = refVal
        private set

    @Column("eng_ref_val")
    var engRefVal: String = engRefVal
        private set

    @Column("clnc_sgnf")
    var clncSgnf: String = clncSgnf
        private set

    @Column("eng_clnc_sgnf")
    var engClncSgnf: String = engClncSgnf
        private set

    @Column("tst_desc")
    var tstDesc: String = tstDesc
        private set

    @Column("tst_eng_desc")
    var tstEngDesc: String = tstEngDesc
        private set

    @Column("tst_dayweek")
    var tstDayweek: String = tstDayweek
        private set

    @Column("tst_tatday")
    var tstTatday: Int = tstTatday
        private set

    @Column("insu_apply_cd")
    var insuApplyCd: String = insuApplyCd
        private set

    @Column("insu_cd")
    var insuCd: String = insuCd
        private set

    @Column("insu_cate_no")
    var insuCateNo: String = insuCateNo
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

    override fun getId(): String? = tstCd

    override fun isNew(): Boolean = _isNew
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
}

@Table("tst_scm.bts_item_estl_doc")
class TestItemEssentialDoc(
    itemEstlDocId: String? = null,
    tstCd: String,
    docCd: String,
    creator: String,
    createDtime: LocalDateTime
) : Persistable<String> {

    @Id
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

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = itemEstlDocId

    override fun isNew(): Boolean = _isNew
}
