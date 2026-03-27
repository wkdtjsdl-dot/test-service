package com.idrsys.ailis.sales.application.dto.request.sap

/**
 * SAP RFC 'ZFI_CUSTOMER_IF_LABS' Table 'T_ZFIS703' row structure for requests.
 */
data class CustomerIfLabsRow(
    val lisgc: String?,      // LIS/GreenChart의 송신 SEQ
    val indcf: String?,      // * 법인/개인 구분자
    val stcd1: String?,      // * 사업자등록번호/주민등록번호
    val name1: String?,      // 이름
    val ort01: String?,      // 도시(도/시/구/동)
    val pstlz: String?,      // 우편번호
    val stras: String?,      // 상세주소(번지,호수)
    val land1: String?,      // *국가키
    val telf1: String?,      // 이동전화
    val smtp_addr: String?,  // 이메일
    val j_1kfrepre: String?, // 대표이름
    val j_1kftbus: String?,  // 업태
    val j_1kftind: String?,  // 종목
    val gricd: String?,      // 세금계산서 발행기준
    val gridt: String?,      // 전자계산서 여부
    val zterm: String?,      // 지급조건
    val kunnr: String?,      // SAP고객코드
    val zbdnum: String?,     // 건물관리번호
    val rtc: String?,         // 리턴코드
    val msg: String?         // 리턴메세지
)
