package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.application.dto.query.TestCodeMappingQuery
import com.idrsys.ailis.sales.application.dto.response.InnerTestCodeMappingResponse
import io.r2dbc.spi.Row
import java.time.LocalDateTime

internal fun Row.toTestCodeMappingQuery(): TestCodeMappingQuery = TestCodeMappingQuery(
    custTstCdMpgId = this.get("cust_tst_cd_mpg_id", String::class.java)!!,
    custMstId = this.get("cust_mst_id", String::class.java),
    custCd = this.get("cust_cd", String::class.java)!!,
    custTstCd = this.get("cust_tst_cd", String::class.java)!!,
    custSubTstCd = this.get("cust_sub_tst_cd", String::class.java),
    custTstNm = this.get("cust_tst_nm", String::class.java),
    tstCd = this.get("tst_cd", String::class.java),
    tstNm = this.get("tst_nm", String::class.java),
    creator = this.get("creator", String::class.java)!!,
    createDtime = this.get("create_dtime", LocalDateTime::class.java)!!,
    updater = this.get("updater", String::class.java)!!,
    updateDtime = this.get("update_dtime", LocalDateTime::class.java)!!,
    custNm = this.get("cust_nm", String::class.java)
)

internal fun Row.toTestCodeMappingInnerTestCode(): InnerTestCodeMappingResponse = InnerTestCodeMappingResponse(
    code = this.get("tst_cd", String::class.java)!!,
    serial = this.get("cust_tst_cd", String::class.java)!!,
    nameKr = this.get("tst_nm", String::class.java),
    sampleType = emptyList(),
    extensions = null
)