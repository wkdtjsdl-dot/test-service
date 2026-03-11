package com.idrsys.ailis.sales.application.dto.request.contract

import com.idrsys.ailis.sales.application.dto.request.attachedfile.AttachedFileGroupCreateCommand
import java.time.LocalDate

data class ContractCommand(
    val custCd: String,
    val cntrNo: String?,
    val cntrDt: LocalDate?,
    val cntrStartDt: LocalDate?,
    val cntrEndDt: LocalDate?,
    val cntrType: String?,
    val recntrMonth: String?,
    val cntrNm: String?,
    val cntrCont: String?,
    val cntrPicId: String?,
    val atchGrupId: String = "",
    val useYn: Boolean = true,

    /**
     * 첨부파일 정보 (신규 업로드 또는 기존 파일 재사용)
     * - null이면 atchGrupId 사용
     * - 값이 있으면 base-service 호출하여 새 그룹 생성
     */
    val atchFiles: AttachedFileGroupCreateCommand? = null
)
