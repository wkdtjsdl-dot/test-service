package com.idrsys.ailis.tst.adapter.persistence.mapper

import com.idrsys.ailis.tst.application.dto.TestItemListRow
import io.r2dbc.spi.Row

internal fun Row.toTestItemListRow(): TestItemListRow {
    return TestItemListRow(
        tstCd = this.get("tst_cd", String::class.java)!!,
        tstNm = this.get("tst_nm", String::class.java)!!,
        tstLargeCateCd = this.get("tst_large_cate_cd", String::class.java)!!,
        useYn = this.get("use_yn", Boolean::class.javaObjectType)!!,
        tstSubYn = this.get("tst_sub_yn", Boolean::class.javaObjectType)!!,
        deptCd = this.get("dept_cd", String::class.java)
    )
}
