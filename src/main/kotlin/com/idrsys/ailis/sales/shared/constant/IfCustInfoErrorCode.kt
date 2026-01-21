package com.idrsys.ailis.sales.shared.constant

object IfCustInfoErrorCode {
    // 미존재
    const val NOT_FOUND_CODE = "IF_CUST_INFO_NOT_FOUND"
    const val NOT_FOUND_MESSAGE = "고객 Excel 설정 정보를 찾을 수 없습니다"

    // 필드 중복 매핑 (UK 위반: if_cust_info_id + if_field_info_id)
    const val DUPLICATE_FIELD_CODE = "IF_CONF_DUPLICATE_FIELD"
    const val DUPLICATE_FIELD_MESSAGE = "동일한 필드가 이미 다른 컬럼에 매핑되어 있습니다"

    // 컬럼 인덱스 중복 (UK 위반: if_cust_info_id + col_idx)
    const val DUPLICATE_COL_IDX_CODE = "IF_CONF_DUPLICATE_COL_IDX"
    const val DUPLICATE_COL_IDX_MESSAGE = "동일한 컬럼 인덱스가 이미 다른 필드에 사용되고 있습니다"

    // 빈 매핑 목록
    const val EMPTY_CONF_LIST_CODE = "IF_CUST_INFO_EMPTY_CONF_LIST"
    const val EMPTY_CONF_LIST_MESSAGE = "컬럼 매핑 정보를 1개 이상 등록해야 합니다"
}
