package com.idrsys.ailis.sales.shared.util

import java.time.LocalDate

enum class PeriodType {
    PAST,    // apply_end_dt < today
    CURRENT, // apply_start_dt <= today <= apply_end_dt
    FUTURE   // apply_start_dt > today
}

object PeriodClassifier {
    fun classifyPeriod(
        startDt: LocalDate,
        endDt: LocalDate,
        today: LocalDate = LocalDate.now()
    ): PeriodType {
        return when {
            endDt < today -> PeriodType.PAST
            startDt >= today -> PeriodType.FUTURE  // 오늘 시작하는 구간도 미래로 분류 (UK 충돌 방지)
            else -> PeriodType.CURRENT
        }
    }
}
