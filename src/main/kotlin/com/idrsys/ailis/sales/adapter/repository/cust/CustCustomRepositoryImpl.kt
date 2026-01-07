package com.idrsys.ailis.sales.adapter.repository.cust

import com.idrsys.ailis.sales.adapter.persistence.mapper.*
import com.idrsys.ailis.sales.application.dto.cust.CustAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.query.*
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
import com.idrsys.ailis.sales.generated.jooq.Tables.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.*
import org.jooq.impl.DSL.*
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class CustCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : CustCustomRepository {
    override suspend fun findCustMstIdByCustCd(custCd: String): String? {
        val query = dslContext.select(SCS_CUST_MST.CUST_MST_ID)
            .from(SCS_CUST_MST)
            .where(SCS_CUST_MST.CUST_CD.eq(custCd))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get("cust_mst_id", String::class.java) }.one().awaitSingleOrNull()
    }

    override fun findCustsWithSalsPicInfo(searchParam: CustSearchParam, pageable: Pageable): Flow<CustWithSalsPicInfo> {
        val conditions = buildConditions(searchParam)
        val rprsCustMst = SCS_CUST_MST.`as`("RPRS_CUST_MST")

        // 계약종료일 들어올 경우 계약테이블 leftjoin
        val needsContractJoin = !searchParam.cntrStartDt.isNullOrBlank() ||
                !searchParam.cntrEndDt.isNullOrBlank() ||
                !searchParam.cntrEndStartDt.isNullOrBlank() ||
                !searchParam.cntrEndEndDt.isNullOrBlank() ||
                !searchParam.recntrMonth.isNullOrBlank()

        // 진료과목 검색 시 병원 테이블 join
        val needsHospitalJoin = !searchParam.medicalSubj.isNullOrBlank()

        val salsPicInfoField: Field<*> =
            (stringAgg(
                SCS_GCGN_SALS_PIC_INFO.SALS_TEAM_CD.concat("=").concat(SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID),
                inline(",")
            ) as Field<*>).`as`("sals_pic_info")

        var queryPart = dslContext.select(
            SCS_CUST_MST.asterisk(),
            rprsCustMst.CUST_NM.`as`("RPRS_CUST_NM"),
            salsPicInfoField
        )
            .from(SCS_CUST_MST)
            .leftJoin(SCS_GCGN_SALS_PIC_INFO).on(SCS_CUST_MST.CUST_MST_ID.eq(SCS_GCGN_SALS_PIC_INFO.CUST_MST_ID))
            .leftJoin(rprsCustMst).on(SCS_CUST_MST.RPRS_CUST_CD.eq(rprsCustMst.CUST_CD))

        // 필터링이 필요한 테이블
        if (needsContractJoin) {
            queryPart = queryPart.join(SCS_CUST_CNTR).on(SCS_CUST_MST.CUST_MST_ID.eq(SCS_CUST_CNTR.CUST_MST_ID))
                .and(SCS_CUST_CNTR.USE_YN.isTrue)
        }

        if (needsHospitalJoin) {
            queryPart = queryPart
                .join(SCS_HOSP_MST).on(SCS_CUST_MST.CARE_INST_ID.eq(SCS_HOSP_MST.CARE_INST_ID))
                .and(SCS_HOSP_MST.USE_YN.isTrue)
                .join(SCS_HOSP_MEDI_SBJT).on(SCS_HOSP_MST.CARE_INST_ID.eq(SCS_HOSP_MEDI_SBJT.CARE_INST_ID))
        }

        val query = queryPart
            .where(conditions)
            .groupBy(*SCS_CUST_MST.fields(), rprsCustMst.CUST_NM)
            .orderBy(SCS_CUST_MST.CUST_CD.asc())
            .let {applyPaging(it, pageable)}

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.toCustWithSalsPicInfo() }
            .all()
            .asFlow()
    }

    override suspend fun countCusts(searchParam: CustSearchParam): Long {
        val conditions = buildConditions(searchParam)

        val needsContractJoin = !searchParam.cntrStartDt.isNullOrBlank() ||
                !searchParam.cntrEndDt.isNullOrBlank() ||
                !searchParam.cntrEndStartDt.isNullOrBlank() ||
                !searchParam.cntrEndEndDt.isNullOrBlank() ||
                !searchParam.recntrMonth.isNullOrBlank()

        val needsSalsPicInfoJoin = !searchParam.empUserId.isNullOrBlank() || searchParam.empUserIds.isNotEmpty()
        val needsHospitalJoin = !searchParam.medicalSubj.isNullOrBlank()
        val needsRprsCustJoin = !searchParam.rprsCustCdNm.isNullOrBlank()

        var queryPart = if (needsContractJoin || needsSalsPicInfoJoin || needsHospitalJoin || needsRprsCustJoin) {
            dslContext.select(countDistinct(SCS_CUST_MST.CUST_MST_ID))
        } else {
            dslContext.selectCount()
        }.from(SCS_CUST_MST)

        if (needsContractJoin) {
            queryPart = queryPart.join(SCS_CUST_CNTR).on(SCS_CUST_MST.CUST_MST_ID.eq(SCS_CUST_CNTR.CUST_MST_ID))
        }
        if (needsSalsPicInfoJoin) {
            queryPart = queryPart.join(SCS_GCGN_SALS_PIC_INFO).on(SCS_CUST_MST.CUST_MST_ID.eq(SCS_GCGN_SALS_PIC_INFO.CUST_MST_ID))
        }
        if (needsHospitalJoin) {
            queryPart = queryPart
                .join(SCS_HOSP_MST).on(SCS_CUST_MST.CARE_INST_ID.eq(SCS_HOSP_MST.CARE_INST_ID))
                .and(SCS_HOSP_MST.USE_YN.isTrue)
                .join(SCS_HOSP_MEDI_SBJT).on(SCS_HOSP_MST.CARE_INST_ID.eq(SCS_HOSP_MEDI_SBJT.CARE_INST_ID))
        }
        // Note: findCustsWithSalsPicInfo 에서는 display용으로 LEFT JOIN
        if (needsRprsCustJoin) {
            queryPart = queryPart.join(SCS_CUST_MST.`as`("RPRS_CUST_MST")).on(SCS_CUST_MST.RPRS_CUST_CD.eq(SCS_CUST_MST.`as`("RPRS_CUST_MST").CUST_CD))
        }

        val query = queryPart.where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }
            .one()
            .awaitSingle()
    }

    private fun applyPaging(q: SelectLimitStep<out Record>, pageable: Pageable?): Query {
        if(pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: CustSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        val rprsCustMst = SCS_CUST_MST.`as`("RPRS_CUST_MST")

        searchParam.bzoffiCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.BZOFFI_CD.eq(it) }
        searchParam.custCdNm?.takeIf { it.isNotBlank() }?.let { keyword -> conds += SCS_CUST_MST.CUST_CD.likeIgnoreCase("%$keyword%").or(SCS_CUST_MST.CUST_NM.likeIgnoreCase("%$keyword%")) }
        searchParam.custCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.CUST_CD.eq(it) } // 자동완성 선택시
        searchParam.rprsCustCdNm?.takeIf { it.isNotBlank() }?.let { keyword -> conds += SCS_CUST_MST.RPRS_CUST_CD.likeIgnoreCase("%$keyword%").or(rprsCustMst.CUST_NM.likeIgnoreCase("%$keyword%")) }
        searchParam.rprsCustCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.RPRS_CUST_CD.eq(it) }
        searchParam.custStatCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.CUST_STAT_CD.eq(it) }
        searchParam.regStartDt?.takeIf { it.isNotBlank() }?.let {
            val startCreateDtime = LocalDate.parse(it).atStartOfDay()
            conds += SCS_CUST_MST.CREATE_DTIME.ge(startCreateDtime)
        }
        searchParam.regEndDt?.takeIf { it.isNotBlank() }?.let {
            val endCreateDtime = LocalDate.parse(it).atTime(23, 59, 59, 999999999)
            conds += SCS_CUST_MST.CREATE_DTIME.lessOrEqual(endCreateDtime)
        }
        searchParam.custDivCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.CUST_DIV_CD.eq(it) }
        searchParam.asrtCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.ASRT_CD.likeIgnoreCase("%$it%") }
        searchParam.bizrno?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.BIZRNO.likeIgnoreCase("%$it%") }
        searchParam.careInstNo?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.CARE_INST_NO.likeIgnoreCase("%$it%") }
        searchParam.frgnAcctYn?.let { conds += SCS_CUST_MST.FRGN_ACCT_YN.eq(it) }
        searchParam.studyProjCustYn?.let { conds += SCS_CUST_MST.STUDY_PROJ_CUST_YN.eq(it) }
        searchParam.sapCustCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.SAP_CUST_CD.likeIgnoreCase("%$it%") }
        searchParam.custTypeCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.CUST_TYPE_CD.eq(it) }

        // 시스템 코드 숫자 코드 substring 비교 MDSB_SBJT_54 -> 54
        searchParam.medicalSubj?.takeIf { it.isNotBlank() }?.let { fullCode ->
            val medicalSubjCode = fullCode.substringAfterLast("_")
            conds += SCS_HOSP_MEDI_SBJT.MEDI_SBJT_CD.eq(medicalSubjCode)
            conds += SCS_HOSP_MEDI_SBJT.USE_YN.isTrue
        }

        // Contract Date filtering
        searchParam.cntrStartDt?.takeIf { it.isNotBlank() }?.let {
            val startDate = LocalDate.parse(it)
            conds += SCS_CUST_CNTR.CNTR_START_DT.ge(startDate)
        }
        searchParam.cntrEndDt?.takeIf { it.isNotBlank() }?.let {
            val endDate = LocalDate.parse(it)
            conds += SCS_CUST_CNTR.CNTR_START_DT.le(endDate)
        }

        // Contract End Date filtering
        searchParam.cntrEndStartDt?.takeIf { it.isNotBlank() }?.let {
            val startDate = LocalDate.parse(it)
            conds += SCS_CUST_CNTR.CNTR_END_DT.ge(startDate)
        }
        searchParam.cntrEndEndDt?.takeIf { it.isNotBlank() }?.let {
            val endDate = LocalDate.parse(it)
            conds += SCS_CUST_CNTR.CNTR_END_DT.le(endDate)
        }

        searchParam.recntrMonth?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_CUST_CNTR.RECNTR_MONTH.eq(it)
        }

        searchParam.empUserId?.takeIf { it.isNotBlank() }?.let { conds += SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID.eq(it) }
        // empUserIdNm으로 검색했지만 매칭되는 사용자가 없으면 empUserIds가 빈 리스트
        // 이 경우 결과가 0건이어야 하므로 불가능한 조건 추가
        if (!searchParam.empUserIdNm.isNullOrBlank() && searchParam.empUserIds.isEmpty()) {
            conds += falseCondition() // 항상 false인 조건 (결과 0건)
        } else {
            searchParam.empUserIds.takeIf { it.isNotEmpty() }?.let { conds += SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID.`in`(it) }
        }

        return conds
    }


    override suspend fun existByCustCd(custCd: String): Boolean {
        val query = dslContext.selectCount()
            .from(SCS_CUST_MST)
            .where(SCS_CUST_MST.CUST_CD.eq(custCd))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        val count = sql
            .map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }
            .one()
            .awaitSingle()

        return count > 0
    }

    override suspend fun findCustDetailInfoByCustMstId(custMstId: String): CustDetailInfo? {
        val directAcctMst = SCS_CUST_MST.`as`("DIRECT_ACCT_MST")
        val rprsCustMst = SCS_CUST_MST.`as`("RPRS_CUST_MST")

        val query = dslContext.select(
            SCS_CUST_MST.asterisk(),
            directAcctMst.CUST_NM.`as`("direct_acct_nm"),
            rprsCustMst.CUST_NM.`as`("rprs_cust_nm")
        )
            .from(SCS_CUST_MST)
            .leftJoin(directAcctMst).on(SCS_CUST_MST.DIRECT_ACCT_CD.eq(directAcctMst.CUST_CD))
            .leftJoin(rprsCustMst).on(SCS_CUST_MST.RPRS_CUST_CD.eq(rprsCustMst.CUST_CD))
            .where(SCS_CUST_MST.CUST_MST_ID.eq(custMstId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.toCustDetailInfo() }
            .one()
            .awaitSingle()
    }

    // 직접거래처 자동완성
    override fun findDirectAcctCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam): Flow<DirectAcctCdNmAutoCompleteInfo> {
        val conditions = mutableListOf<Condition>()
        val keyword = searchParam.directAcctCdNm?.takeIf { it.isNotBlank() } ?: return flowOf()

        // 검색 들어온 고객코드나 고객명이 직접거래처인 경우
        conditions += SCS_CUST_MST.CUST_CD.containsIgnoreCase(keyword).or(SCS_CUST_MST.CUST_NM.containsIgnoreCase(keyword))
        conditions += SCS_CUST_MST.CUST_DIV_CD.eq("CSDV_DA") // 직접거래처
        val query = dslContext.select(
            SCS_CUST_MST.CUST_CD.`as`("direct_acct_cd"),
            SCS_CUST_MST.CUST_NM.`as`("direct_acct_nm")
        )
            .from(SCS_CUST_MST)
            .where(conditions)
            .orderBy(SCS_CUST_MST.DIRECT_ACCT_CD.asc())

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.toDirectAcctCdNmAutoCompleteInfo() }
            .all()
            .asFlow()
    }

    override fun findAllWithCareInstId(): Flow<CustCareInstId> {
        val query = dslContext.select(
            SCS_CUST_MST.CUST_MST_ID,
            SCS_CUST_MST.CARE_INST_ID
        )
            .from(SCS_CUST_MST)
            .where(SCS_CUST_MST.CARE_INST_ID.isNotNull)
            .orderBy(SCS_CUST_MST.CUST_MST_ID.asc())

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.toCustCareInstId() }
            .all()
            .asFlow()
    }

    override fun findCustCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam): Flow<CustCdNmAutoCompleteInfo> {
        val conditions = mutableListOf<Condition>()
        val keyword = searchParam.custCdNm?.takeIf { it.isNotBlank() } ?: return flowOf()

        conditions += SCS_CUST_MST.CUST_CD.containsIgnoreCase(keyword).or(SCS_CUST_MST.CUST_NM.containsIgnoreCase(keyword))

        val selectFields = arrayOf(
            SCS_CUST_MST.CUST_MST_ID,
            SCS_CUST_MST.CUST_CD,
            SCS_CUST_MST.CUST_NM
        )
        val orderByField = SCS_CUST_MST.CUST_CD.asc()

        val query = dslContext.selectDistinct(*selectFields)
            .from(SCS_CUST_MST)
            .where(conditions)
            .orderBy(orderByField)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.toCustCdNmAutoCompleteInfo() }
            .all()
            .asFlow()
    }

    // 대표거래처 자동완성
    override fun findRprsCustCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam): Flow<RprsCustCdNmAutoCompleteInfo> {
        val conditions = mutableListOf<Condition>()
        val keyword = searchParam.rprsCustCdNm?.takeIf { it.isNotBlank() } ?: return flowOf()

        conditions += SCS_CUST_MST.CUST_CD.containsIgnoreCase(keyword).or(SCS_CUST_MST.CUST_NM.containsIgnoreCase(keyword))
        conditions += SCS_CUST_MST.RPRS_CUST_YN.isTrue()

        val query = dslContext.select(
            SCS_CUST_MST.CUST_CD.`as`("rprs_cust_cd"),
            SCS_CUST_MST.CUST_NM.`as`("rprs_cust_nm")
        )
            .from(SCS_CUST_MST)
            .where(conditions)
            .orderBy(SCS_CUST_MST.RPRS_CUST_CD.asc())

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.toRprsCustCdNmAutoCompleteInfo() }
            .all()
            .asFlow()
    }

    // 고객 테이블 join 없이 최소한의 데이터 조회
    override suspend fun findCustList(searchParam: CustSearchParam): Flow<CustBasicInfo> {
        val conditions = mutableListOf<Condition>()

        searchParam.custCdNm?.takeIf { it.isNotBlank() }?.let { keyword ->
            conditions += SCS_CUST_MST.CUST_CD.containsIgnoreCase(keyword)
                .or(SCS_CUST_MST.CUST_NM.containsIgnoreCase(keyword))
        }

        searchParam.bzoffiCd?.takeIf { it.isNotBlank() }?.let {
            conditions += SCS_CUST_MST.BZOFFI_CD.eq(it)
        }

        // 의뢰가능여부 조건
        searchParam.reqPossYn?.let { conditions += SCS_CUST_MST.REQ_POSS_YN.eq(it)}

        searchParam.custCds.takeIf { it.isNotEmpty() }?.let {
            conditions += SCS_CUST_MST.CUST_CD.`in`(it)

        }

        if (conditions.isEmpty()) return emptyFlow()

        val query = dslContext.select(
            SCS_CUST_MST.asterisk(),
        )
            .from(SCS_CUST_MST)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.toCustBasicInfo() }
            .all()
            .asFlow()
    }

    override fun findCustTstMpgsByCustMstId(custMstId: String): Flow<TestCodeMappingQuery> {
        val query = dslContext.select(
            SCS_CUST_TST_CD_MPG.asterisk(),
            SCS_CUST_MST.CUST_NM
        ).from(SCS_CUST_TST_CD_MPG)
            .leftJoin(SCS_CUST_MST).on(SCS_CUST_TST_CD_MPG.CUST_CD.eq(SCS_CUST_MST.CUST_CD))
            .where(SCS_CUST_TST_CD_MPG.CUST_MST_ID.eq(custMstId))
            .orderBy(SCS_CUST_TST_CD_MPG.CREATE_DTIME.desc())

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.toTestCodeMappingQuery() }
            .all()
            .asFlow()
    }
}