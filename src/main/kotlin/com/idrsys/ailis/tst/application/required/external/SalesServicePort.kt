package com.idrsys.ailis.tst.application.required.external

import com.idrsys.ailis.tst.application.dto.inner.CustCdNmResponse

interface SalesServicePort {
    /**
     * 고객 코드 목록으로 고객명 조회
     * @param custCds 고객 코드 목록
     * @return 고객 코드 -> 고객명 맵
     */
    suspend fun findCustNmByCustCd(custCds:List<String>): Map<String, CustCdNmResponse>
}