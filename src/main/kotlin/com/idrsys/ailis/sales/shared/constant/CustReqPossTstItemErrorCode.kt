package com.idrsys.ailis.sales.shared.constant

object CustReqPossTstItemErrorCode {
    // 검사코드 미존재
    const val TST_CD_NOT_FOUND_CODE = "CUST_REQ_POSS_TST_ITEM_TST_CD_NOT_FOUND"
    const val TST_CD_NOT_FOUND_MESSAGE = "존재하지 않는 검사코드입니다"

    // 중복
    const val DUPLICATE_CODE = "CUST_REQ_POSS_TST_ITEM_DUPLICATE"
    const val DUPLICATE_MESSAGE = "이미 등록된 검사코드입니다"

    // 미존재
    const val NOT_FOUND_CODE = "CUST_REQ_POSS_TST_ITEM_NOT_FOUND"
    const val NOT_FOUND_MESSAGE = "의뢰가능검사 항목을 찾을 수 없습니다"
}
