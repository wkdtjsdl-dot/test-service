package com.idrsys.ailis.sales.application.dto.response

data class IfFieldInfoResponse(
    val ifFieldInfoId: String,
    val ifFieldNm: String,
    val ifFieldColNm: String?,
    val ifFieldDesc: String?,
    val colType: String?,       // CT_DIRECT, CT_REF, CT_TST
    val targetPath: String?,    // patNm, refItems[].refCd:refCont 등
    val statDtlCd: String?,     // TA0001 등 (CT_REF용)
    val ifFieldExps: String?,   // M→GD_M,F→GD_FM 등 (값 변환식)
    val colIdx: Int?            // col_idx (scs_if_conf_info에서 조인)
)
