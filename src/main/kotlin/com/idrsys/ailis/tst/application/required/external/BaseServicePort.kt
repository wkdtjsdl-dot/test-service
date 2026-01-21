package com.idrsys.ailis.tst.application.required.external

/**
 * base-service 연동을 위한 Port 인터페이스
 * - Clean Architecture에 따라 application 계층에서 정의
 * - adapter.external.BaseServiceClient가 구현
 */
interface BaseServicePort {
    /**
     * 부서 코드 목록으로 부서 정보 조회
     * @param deptCds 부서 코드 목록
     * @return 부서 코드 -> 부서명 맵
     */
    suspend fun getDepartmentsByDeptCds(deptCds: List<String>): Map<String, String>

    /**
     * 단일 부서 코드로 부서명 조회
     * @param deptCd 부서 코드
     * @return 부서명 (조회 실패 시 null)
     */
    suspend fun getDepartmentName(deptCd: String): String?
}
