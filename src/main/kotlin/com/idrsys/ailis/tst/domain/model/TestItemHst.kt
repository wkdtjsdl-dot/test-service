package com.idrsys.ailis.tst.domain.model

import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("tst_scm.bts_item_hst")
class TestItemHst(
    itemHstId: String? = null,
    hstDesc: String,
    tstCd: String,
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
    tstSubYn: Boolean?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime
) : Persistable<String> {

    @Id
    @UuidGeneratedId(idFieldName = "itemHstId")
    @Column("item_hst_id")
    val itemHstId: String? = itemHstId

    @Column("hst_desc")
    var hstDesc: String = hstDesc
        private set

    @Column("tst_cd")
    var tstCd: String = tstCd
        private set

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

    @Column("tst_sub_yn")
    var tstSubYn: Boolean? = tstSubYn
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

    override fun getId(): String? = itemHstId

    override fun isNew(): Boolean = _isNew
}

@Table("tst_scm.bts_item_sub_hst")
class TestItemSubHst(
    itemSubHstId: String? = null,
    hstDesc: String,
    itemSubId: String?,
    tstCd: String?,
    tstSubCd: String?,
    startDt: LocalDate?,
    endDt: LocalDate?,
    useYn: Boolean?,
    tstSubNm: String?,
    tstSubAbbrNm: String?,
    tstSubEngNm: String?,
    tstSubEngAbbrNm: String?,
    tstSubIntNm: String?,
    rstTypeShortYn: Boolean?,
    rstTypeLongYn: Boolean?,
    rstTypeFileYn: Boolean?,
    rstTypeUrlYn: Boolean?,
    refVal: String?,
    engRefVal: String?,
    creator: String?,
    createDtime: LocalDateTime?,
    updater: String?,
    updateDtime: LocalDateTime?
) : Persistable<String> {

    @Id
    @UuidGeneratedId(idFieldName = "itemSubHstId")
    @Column("item_sub_hst_id")
    val itemSubHstId: String? = itemSubHstId

    @Column("hst_desc")
    var hstDesc: String = hstDesc
        private set

    @Column("item_sub_id")
    var itemSubId: String? = itemSubId
        private set

    @Column("tst_cd")
    var tstCd: String? = tstCd
        private set

    @Column("tst_sub_cd")
    var tstSubCd: String? = tstSubCd
        private set

    @Column("start_dt")
    var startDt: LocalDate? = startDt
        private set

    @Column("end_dt")
    var endDt: LocalDate? = endDt
        private set

    @Column("use_yn")
    var useYn: Boolean? = useYn
        private set

    @Column("tst_sub_nm")
    var tstSubNm: String? = tstSubNm
        private set

    @Column("tst_sub_abbr_nm")
    var tstSubAbbrNm: String? = tstSubAbbrNm
        private set

    @Column("tst_sub_eng_nm")
    var tstSubEngNm: String? = tstSubEngNm
        private set

    @Column("tst_sub_eng_abbr_nm")
    var tstSubEngAbbrNm: String? = tstSubEngAbbrNm
        private set

    @Column("tst_sub_int_nm")
    var tstSubIntNm: String? = tstSubIntNm
        private set

    @Column("rst_type_short_yn")
    var rstTypeShortYn: Boolean? = rstTypeShortYn
        private set

    @Column("rst_type_long_yn")
    var rstTypeLongYn: Boolean? = rstTypeLongYn
        private set

    @Column("rst_type_file_yn")
    var rstTypeFileYn: Boolean? = rstTypeFileYn
        private set

    @Column("rst_type_url_yn")
    var rstTypeUrlYn: Boolean? = rstTypeUrlYn
        private set

    @Column("ref_val")
    var refVal: String? = refVal
        private set

    @Column("eng_ref_val")
    var engRefVal: String? = engRefVal
        private set

    @Column("creator")
    var creator: String? = creator
        private set

    @Column("create_dtime")
    var createDtime: LocalDateTime? = createDtime
        private set

    @Column("updater")
    var updater: String? = updater
        private set

    @Column("update_dtime")
    var updateDtime: LocalDateTime? = updateDtime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = itemSubHstId

    override fun isNew(): Boolean = _isNew
}
