package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.ClinicalTestCodeResponse
import com.idrsys.ailis.tst.application.dto.ClinicalTestCodeSearchParam
import com.idrsys.ailis.tst.application.required.repository.ClinicalTestCodeRepository
import com.idrsys.ailis.tst.generated.jooq.tables.BbsTstCate
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItem
import com.idrsys.ailis.tst.generated.jooq.tables.BtsStndCharge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate

@Repository
class ClinicalTestCodeRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : ClinicalTestCodeRepository {

    override fun search(param: ClinicalTestCodeSearchParam): Flow<ClinicalTestCodeResponse> {
        val i = BtsItem.BTS_ITEM
        val sc = BtsStndCharge.BTS_STND_CHARGE
        val cLarge = BbsTstCate.BBS_TST_CATE.`as`("c_large")
        val cMedium = BbsTstCate.BBS_TST_CATE.`as`("c_medium")
        val endDate9999 = LocalDate.of(9999, 12, 31)

        var condition: Condition = DSL.noCondition()
        param.tstCd?.takeIf { it.isNotBlank() }?.let { condition = condition.and(i.TST_CD.likeIgnoreCase("%$it%")) }
        param.tstNm?.takeIf { it.isNotBlank() }?.let { condition = condition.and(i.TST_NM.likeIgnoreCase("%$it%")) }
        param.useYn?.let { condition = condition.and(i.USE_YN.eq(it)) }
        param.startDt?.let { condition = condition.and(i.START_DT.greaterOrEqual(it)) }
        param.endDt?.let { condition = condition.and(i.END_DT.lessOrEqual(it)) }
        param.partCd?.takeIf { it.isNotBlank() }?.let { condition = condition.and(i.TST_LARGE_CATE_CD.eq(it)) }
        param.tstStatCd?.takeIf { it.isNotBlank() }?.let { condition = condition.and(i.TST_MEDIUM_CATE_CD.eq(it)) }

        val cLargeTable = BbsTstCate.BBS_TST_CATE.`as`("c_large")
        val cMediumTable = BbsTstCate.BBS_TST_CATE.`as`("c_medium")

        val query = dslContext
            .select(
                i.TST_CD,
                i.USE_YN,
                i.TST_NM,
                DSL.inline<String>(null).`as`("spcm_info"),
                DSL.field(DSL.name("c_large", "cate_nm")).`as`("part_nm"),
                i.TST_METHOD_CD.`as`("tst_method_nm"),
                DSL.field(DSL.name("c_medium", "cate_nm")).`as`("tst_stat_nm"),
                DSL.inline<String>(null).`as`("tst_stat_nm2"),
                i.TST_DAYWEEK,
                i.TST_TATDAY,
                DSL.inline<Int>(null).`as`("tat_hour"),
                DSL.inline<Boolean>(null).`as`("outsource_yn"),
                DSL.inline<String>(null).`as`("take_qnty"),
                i.TST_DESC.`as`("tst_guide"),
                sc.STND_PRICE,
                sc.SUPVAL,
                sc.ADDTAX
            )
            .from(i)
            .leftJoin(cLargeTable).on(
                DSL.field(DSL.name("c_large", "tst_large_cate_cd")).eq(i.TST_LARGE_CATE_CD)
                    .and(DSL.field(DSL.name("c_large", "tst_medium_cate_cd")).eq(""))
            )
            .leftJoin(cMediumTable).on(
                DSL.field(DSL.name("c_medium", "tst_large_cate_cd")).eq(i.TST_LARGE_CATE_CD)
                    .and(DSL.field(DSL.name("c_medium", "tst_medium_cate_cd")).eq(i.TST_MEDIUM_CATE_CD))
                    .and(DSL.field(DSL.name("c_medium", "tst_medium_cate_cd")).ne(""))
            )
            .leftJoin(sc).on(sc.TST_CD.eq(i.TST_CD).and(sc.APPLY_END_DT.eq(endDate9999)))
            .where(condition)
            .orderBy(i.TST_CD)

        var spec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            spec = if (value != null) spec.bind(index, value) else spec.bindNull(index, String::class.java)
        }

        return spec.fetch().all().map { row ->
            ClinicalTestCodeResponse(
                tstCd = row["tst_cd"] as String,
                useYn = row["use_yn"] as Boolean,
                tstNm = row["tst_nm"] as String,
                spcmInfo = row["spcm_info"] as? String,
                partNm = row["part_nm"] as? String,
                tstMethodNm = row["tst_method_nm"] as? String,
                tstStatNm = row["tst_stat_nm"] as? String,
                tstStatNm2 = row["tst_stat_nm2"] as? String,
                tstDayweek = row["tst_dayweek"] as? String,
                tatDay = row["tst_tatday"] as? Int,
                tatHour = row["tat_hour"] as? Int,
                outsourceYn = row["outsource_yn"] as? Boolean,
                takeQnty = row["take_qnty"] as? String,
                tstGuide = row["tst_guide"] as? String,
                stndPrice = row["stnd_price"] as? BigDecimal,
                supval = row["supval"] as? BigDecimal,
                addtax = row["addtax"] as? BigDecimal
            )
        }.asFlow()
    }
}
