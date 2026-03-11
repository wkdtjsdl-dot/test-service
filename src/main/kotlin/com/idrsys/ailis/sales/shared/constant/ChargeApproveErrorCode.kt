package com.idrsys.ailis.sales.shared.constant

object ChargeApproveErrorCode {
    const val LOWEST_CHARGE_NOT_FOUND_CODE = "CA004"
    const val LOWEST_CHARGE_NOT_FOUND_MESSAGE = "최저수가를 조회할 수 없어 승인 요청이 불가능합니다."

    const val APPROVAL_REQUEST_CAS_CONFLICT_CODE = "CA005"
    const val APPROVAL_REQUEST_CAS_CONFLICT_MESSAGE = "승인 요청에 실패했습니다. 다른 사용자가 수정했거나 상태가 변경되었습니다."

    const val CHARGE_NOT_FOUND_CODE = "CA006"
    const val CHARGE_NOT_FOUND_MESSAGE = "해당 고객수가를 찾을 수 없습니다."

    const val NOT_IN_PROGRESS_CODE = "CA007"
    const val NOT_IN_PROGRESS_MESSAGE = "결재 중인 건만 처리할 수 있습니다."

    const val APPROVAL_INFO_NOT_FOUND_CODE = "CA008"
    const val APPROVAL_INFO_NOT_FOUND_MESSAGE = "현재 결재 정보를 찾을 수 없습니다."

    const val NO_AUTHORITY_CODE = "CA009"
    const val NO_AUTHORITY_MESSAGE = "처리 권한이 없습니다."

    const val ALREADY_APPROVED_CODE = "CA010"
    const val ALREADY_APPROVED_MESSAGE = "이미 승인한 문서는 반려하거나 삭제할 수 없습니다."

    const val REJECT_CAS_CONFLICT_CODE = "CA011"
    const val REJECT_CAS_CONFLICT_MESSAGE = "반려 처리에 실패했습니다. 다른 사용자가 이미 처리했을 수 있습니다."

    const val APPROVE_CAS_CONFLICT_CODE = "CA012"
    const val APPROVE_CAS_CONFLICT_MESSAGE = "승인 처리 중 충돌이 발생했습니다."

    const val ALREADY_REQUESTED_CODE = "CA013"
    const val ALREADY_REQUESTED_MESSAGE = "임시저장 상태인 수가만 승인 요청할 수 있습니다."

    const val APPR_LINE_NOT_FOUND_CODE = "CA014"
    const val APPR_LINE_NOT_FOUND_MESSAGE = "결재선을 찾을 수 없습니다."

    const val CANNOT_DELETE_CODE = "CA015"
    const val CANNOT_DELETE_MESSAGE = "임시저장 또는 결재중인 수가만 삭제할 수 있습니다."

    const val NOT_ALLOWED_TO_DELETE_LAST_T_CODE = "CA016"
    const val NOT_ALLOWED_TO_DELETE_LAST_T_MESSAGE = "임시저장 상태의 수가는 등록자만 삭제할 수 있습니다."

    const val PAST_PERIOD_NOT_ALLOWED_CODE = "CA017"
    const val PAST_PERIOD_NOT_ALLOWED_MESSAGE = "과거 구간은 결재 요청할 수 없습니다."

    const val DUPLICATE_CHARGE_CODE = "CA018"
    const val DUPLICATE_CHARGE_MESSAGE = "동일 고객의 동일 시작일자, 동일 검사코드가 이미 존재합니다."
}