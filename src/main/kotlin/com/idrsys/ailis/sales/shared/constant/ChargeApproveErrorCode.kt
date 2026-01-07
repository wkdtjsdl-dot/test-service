package com.idrsys.ailis.sales.shared.constant

object ChargeApproveErrorCode {
    // 고객수가 없음
    const val CHARGE_NOT_FOUND_CODE = "CHARGE_APPROVE_CHARGE_NOT_FOUND"
    const val CHARGE_NOT_FOUND_MESSAGE = "승인할 고객수가 정보를 찾을 수 없습니다"

    // 이미 승인 요청됨
    const val ALREADY_REQUESTED_CODE = "CHARGE_APPROVE_ALREADY_REQUESTED"
    const val ALREADY_REQUESTED_MESSAGE = "이미 승인 요청된 고객수가입니다"

    // 결재 권한 없음
    const val NO_APPROVAL_AUTHORITY_CODE = "CHARGE_APPROVE_NO_AUTHORITY"
    const val NO_APPROVAL_AUTHORITY_MESSAGE = "해당 고객수가를 승인할 권한이 없습니다"

    // 결재 정보 없음
    const val APPROVAL_INFO_NOT_FOUND_CODE = "CHARGE_APPROVE_INFO_NOT_FOUND"
    const val APPROVAL_INFO_NOT_FOUND_MESSAGE = "결재 정보를 찾을 수 없습니다"

    // 잘못된 결재 순서
    const val INVALID_APPROVAL_SEQUENCE_CODE = "CHARGE_APPROVE_INVALID_SEQUENCE"
    const val INVALID_APPROVAL_SEQUENCE_MESSAGE = "잘못된 결재 순서입니다"

    // 이미 승인 완료됨
    const val ALREADY_APPROVED_CODE = "CHARGE_APPROVE_ALREADY_COMPLETED"
    const val ALREADY_APPROVED_MESSAGE = "이미 승인이 완료된 고객수가입니다"

    // 최저수가 미만
    const val BELOW_LOWEST_CHARGE_CODE = "CHARGE_APPROVE_BELOW_LOWEST"
    const val BELOW_LOWEST_CHARGE_MESSAGE = "특별수가가 최저수가보다 낮아 다단계 승인이 필요합니다"

    // 사용자 정보 없음
    const val USER_NOT_FOUND_CODE = "CHARGE_APPROVE_USER_NOT_FOUND"
    const val USER_NOT_FOUND_MESSAGE = "사용자 정보를 찾을 수 없습니다"

    // 결재선 조회 실패
    const val APPROVAL_LINE_FETCH_FAILED_CODE = "CHARGE_APPROVE_LINE_FETCH_FAILED"
    const val APPROVAL_LINE_FETCH_FAILED_MESSAGE = "결재선 조회에 실패했습니다"

    // 최저수가 조회 실패
    const val LOWEST_CHARGE_FETCH_FAILED_CODE = "CHARGE_APPROVE_LOWEST_FETCH_FAILED"
    const val LOWEST_CHARGE_FETCH_FAILED_MESSAGE = "최저수가 조회에 실패했습니다"
}
