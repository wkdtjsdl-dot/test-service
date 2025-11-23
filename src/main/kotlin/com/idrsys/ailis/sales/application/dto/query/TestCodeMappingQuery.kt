package com.idrsys.ailis.sales.application.dto.query

import java.time.LocalDateTime

data class TestCodeMappingQuery (
    // 테이블 필드
    val custTstCdMpgId: String,          // 고객검사코드맵핑아이디
    val custMstId: String? = null,       // 고객마스터아이디
    val custCd: String,                  // 고객코드
    val custTstCd: String,               // 고객검사코드
    val custSubTstCd: String? = null,    // 고객부속검사코드
    val custTstNm: String? = null,       // 고객검사명
    val tstCd: String? = null,           // 검사코드
    val tstNm: String? = null,           // 검사명
    val creator: String,                 // 생성자
    val createDtime: LocalDateTime,      // 생성일시
    val updater: String,                 // 수정자
    val updateDtime: LocalDateTime,      // 수정일시

    // Joined fields
    val custNm: String? = null,          // 고객명
)