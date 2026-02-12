package com.idrsys.ailis.sales.domain.enums

/**
 * 결재 상태 코드
 * - DB: csy_system_code.cate_cd = 'LAST'
 */
enum class ApprovalStatus(val code: String) {
    TEMPORARY("LAST_T"),      // 임시저장
    IN_PROGRESS("LAST_I"),    // 결재중
    COMPLETED("LAST_C");      // 결재완료

    companion object {
        fun fromCode(code: String?): ApprovalStatus? = entries.find { it.code == code }
    }
}
