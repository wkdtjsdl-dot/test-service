package com.idrsys.ailis.sales.shared.constant

object ChargeErrorCode {
    // UK 중복
    const val UK_DUPLICATE_CODE = "CHARGE_UK_DUPLICATE"
    const val UK_DUPLICATE_MESSAGE = "동일 시작일자의 동일 검사코드가 이미 존재합니다."

    // 기간 겹침
    const val PERIOD_OVERLAP_CODE = "CHARGE_PERIOD_OVERLAP"
    const val PERIOD_OVERLAP_MESSAGE = "동일 고객/검사코드의 다른 수가 기간과 겹칩니다"

    // 수정 불가
    const val UPDATE_NOT_ALLOWED_CODE = "CHARGE_UPDATE_NOT_ALLOWED"
    const val UPDATE_NOT_ALLOWED_MESSAGE = "해당 구간은 수정할 수 없습니다"

    // 삭제 불가
    const val DELETION_NOT_ALLOWED_CODE = "CHARGE_DELETION_NOT_ALLOWED"
    const val DELETION_NOT_ALLOWED_MESSAGE = "해당 구간은 삭제할 수 없습니다"

    // 날짜 범위 오류
    const val INVALID_DATE_RANGE_CODE = "CHARGE_INVALID_DATE_RANGE"
    const val INVALID_DATE_RANGE_MESSAGE = "적용시작일은 적용종료일보다 이전이어야 합니다"

    // 미존재
    const val NOT_FOUND_CODE = "CHARGE_NOT_FOUND"
    const val NOT_FOUND_MESSAGE = "수가 정보를 찾을 수 없습니다"

    // 고객 미존재 (Excel 업로드 시)
    const val CUSTOMER_NOT_FOUND_CODE = "CHARGE_CUSTOMER_NOT_FOUND"
    const val CUSTOMER_NOT_FOUND_MESSAGE = "존재하지 않는 고객 코드입니다"

    // Excel 등록 실패
    const val EXCEL_REGISTRATION_FAILED_CODE = "CHARGE_EXCEL_REGISTRATION_FAILED"
    const val EXCEL_REGISTRATION_FAILED_MESSAGE = "엑셀 등록 중 오류가 발생했습니다"

    // 검사코드 미존재
    const val TEST_NOT_FOUND_CODE = "CHARGE_TEST_NOT_FOUND"
    const val TEST_NOT_FOUND_MESSAGE = "존재하지 않는 검사 코드입니다"
}
