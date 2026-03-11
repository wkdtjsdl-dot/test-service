package com.idrsys.ailis.sales.domain.enums

/**
 * 결재라인 상태 코드
 * - DB: csy_system_code.cate_cd = 'APST'
 */
enum class ApprovalLineStatus(val code: String) {
    WAITING("APST_W"),    // 대기
    COMPLETED("APST_C"),  // 승인완료
    REJECTED("APST_R");   // 반려

    companion object {
        fun fromCode(code: String?): ApprovalLineStatus? = entries.find { it.code == code }
    }
}
