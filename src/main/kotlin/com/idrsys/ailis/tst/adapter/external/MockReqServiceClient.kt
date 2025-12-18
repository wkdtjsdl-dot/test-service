package com.idrsys.ailis.tst.adapter.external

import com.idrsys.ailis.tst.application.dto.inner.TestItemKey
import com.idrsys.ailis.tst.application.dto.inner.TestItemStatusInfo
import com.idrsys.ailis.tst.application.dto.inner.TestRequestInfo
import com.idrsys.ailis.tst.application.dto.inner.TestRequestKey
import com.idrsys.ailis.tst.application.required.ReqServiceClient
import org.springframework.stereotype.Component

/**
 * req-service Inner API Mock 구현체
 *
 * 실제 req-service와의 통신 없이 더미 데이터를 반환합니다.
 * 향후 실제 WebClient 기반 구현으로 교체 가능합니다.
 */
@Component
class MockReqServiceClient : ReqServiceClient {

    // Mock 데이터: 거래처명 매핑
    private val custNameMap = mapOf(
        "CUST001" to "서울대학교병원",
        "CUST002" to "연세대학교병원",
        "CUST003" to "삼성서울병원",
        "CUST004" to "아산병원",
        "CUST005" to "강남세브란스병원"
    )

    // Mock 데이터: 병원명 매핑
    private val hospNameMap = mapOf(
        "HOSP001" to "본관",
        "HOSP002" to "별관",
        "HOSP003" to "암센터",
        "HOSP004" to "어린이병원",
        "HOSP005" to "재활병원"
    )

    // Mock 데이터: 환자명 풀
    private val patientNames = listOf(
        "김철수", "이영희", "박민수", "정수진", "최준호",
        "강미영", "윤서준", "임지현", "한동욱", "오나연"
    )

    override suspend fun getTestRequestsByKeys(keys: List<TestRequestKey>): List<TestRequestInfo> {
        return keys.mapIndexed { index, key ->
            val custIndex = (index % 5) + 1
            val hospIndex = (index % 5) + 1
            val custCd = "CUST%03d".format(custIndex)
            val hospCd = "HOSP%03d".format(hospIndex)

            TestRequestInfo(
                tstReqDt = key.tstReqDt,
                tstReqNo = key.tstReqNo,
                patientNm = patientNames[index % patientNames.size],
                custCd = custCd,
                hospChartNo = "H%08d".format(key.tstReqNo),
                tstReqStatCd = "REQ_COMPLETE",    // 의뢰완료
                tstReqDivCd = "NORMAL",           // 일반의뢰
                deptCd = "DEPT%03d".format((index % 3) + 1),
                custNm = custNameMap[custCd],
                hospNm = hospNameMap[hospCd]
            )
        }
    }

    override suspend fun getTestItemsStatus(keys: List<TestItemKey>): List<TestItemStatusInfo> {
        return keys.map { key ->
            TestItemStatusInfo(
                tstReqDt = key.tstReqDt,
                tstReqNo = key.tstReqNo,
                tstCd = key.tstCd,
                tstStat1Cd = "COMPLETE",          // 검사 진행 완료
                tstStat2Cd = "REPORTED"           // 보고 완료
            )
        }
    }
}