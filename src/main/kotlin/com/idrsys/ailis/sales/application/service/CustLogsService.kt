package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.response.CustDetailLogs
import com.idrsys.ailis.sales.application.dto.response.CustLogsEditResponse
import com.idrsys.ailis.sales.application.dto.response.CustLogsSearchParam
import com.idrsys.ailis.sales.application.required.repository.custLogs.CustLogsCustomRepository
import com.idrsys.ailis.sales.application.required.repository.custLogs.CustLogsRepository
import com.idrsys.ailis.sales.application.usecase.custLogs.CustLogsUseCase
import com.idrsys.ailis.sales.domain.model.Cust
import com.idrsys.ailis.sales.domain.model.CustMstHst
import com.idrsys.ailis.sales.shared.mapper.CustLogsMapper
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.full.memberProperties

@Service
class CustLogsService(
    private val custLogsCustomRepository: CustLogsCustomRepository,
    private val custLogsRepository: CustLogsRepository,
    private val custLogsMapper: CustLogsMapper,
    private val custService: CustService,
) : CustLogsUseCase {

    private val propertyNameMap: Map<String, String> = mapOf(
        "custMstHstId" to "고객마스터이력아이디",
        "custMstId" to "고객마스터아이디",
        "custCd" to "고객코드",
        "custNm" to "고객명",
        "rstOutputCustNm" to "결과출력고객명",
        "rprsNm" to "대표명",
        "rprsCustYn" to "대표고객여부",
        "rprsCustCd" to "대표고객코드",
        "custDivCd" to "고객구분코드",
        "directAcctCd" to "직접거래처코드",
        "directAcctAcctCd" to "직접거래처거래처코드",
        "frgnAcctYn" to "해외거래처여부",
        "studyProjCustYn" to "연구과제고객여부",
        "studyProjNm" to "연구과제명",
        "natnCd" to "국가코드",
        "useLangCd" to "사용언어코드",
        "crcyCd" to "통화(화폐) 코드",
        "custStatCd" to "고객상태코드",
        "reqPossYn" to "의뢰가능여부",
        "custTypeCd" to "고객유형코드",
        "custGradeCd" to "고객등급코드",
        "branchCd" to "지점코드",
        "bzoffiCd" to "영업소코드",
        "bzoffiPicId" to "영업소담당자아이디",
        "asrtCd" to "종별코드",
        "zipcd" to "우편번호",
        "addr1" to "주소1",
        "addr2" to "주소2",
        "hpUrl" to "홈페이지URL",
        "careInstNo" to "요양기관번호",
        "careInstId" to "요양기관아이디",
        "bizrno" to "사업자등록번호",
        "corpNo" to "법인번호",
        "bzse" to "업종",
        "bztp" to "업태",
        "biznm" to "사업자명",
        "bizregRprsNm" to "사업자등록대표(자) 명",
        "openDt" to "개설(개업) 일자",
        "billPublYn" to "계산서발행여부",
        "billAutoPublTargetYn" to "계산서자동발행대상여부",
        "addtaxInclYn" to "부가세포함여부",
        "taxDivCd" to "과세구분코드",
        "billPic" to "계산서담당자",
        "billPicEmaiAddr" to "계산서담당자주소",
        "billPicTelno" to "계산서담당자전화번호",
        "billPublDt" to "계산서발행일자",
        "rprsAcctBillCombPublYn" to "대표(자) 거래처계산서통합(일반) 발행여부",
        "payRtday" to "결제회전일수",
        "payPlanDt" to "결제예정(계획) 일자",
        "payMethodCd" to "결제메소드(방법) 코드",
        "gccStmtMethodCd" to "GCCell정산메소드(방법) 코드",
        "sapCustCd" to "SAP고객코드",
        "spcmPickupMethodCd" to "검체수거수가코드",
        "gcgPickupPicEmpNo" to "GC Genome수거담당자사원번호",
        "truncUnitCd" to "절사단위코드",
        "invcEmailRecpYn" to "청구서이메일수신여부",
        "invcRecpEmailAddr" to "청구서수신이메일계정",
        "outamtWritingYn" to "미수금표기여부",
        "sotOutputYn" to "거래명세서출력여부",
        "sotOutputQnty" to "거래명세서출력수량",
        "rstNtcnRecpYn" to "결과알림수신여부",
        "rstNtcnRecpEmailAddr" to "결과알림수신메일",
        "reqMethodCd" to "의뢰메소드(방법) 코드",
        "reqIfTypeCd" to "의뢰연동유형코드",
        "creator" to "생성자",
        "createDtime" to "생성일시",
        "updater" to "수정자",
        "updateDtime" to "수정일시",
        "reqDivCd" to "의뢰구분코드",
        "atchFileGrupId" to "첨부파일그룹아이디",
        "reqPossTstLimitYn" to "의뢰가능검사제한여부",
        "telNo" to "전화번호",
        "faxNo" to "팩스번호"
    )

    override suspend fun getCustomerLogList(searchParam: CustLogsSearchParam): List<CustLogsEditResponse> {
        val logs = custLogsCustomRepository.findAllByCustMstId(searchParam.custMstId).toList()

        if (logs.size < 2) {
            return emptyList()
        }

        return logs.windowed(size = 2, step = 1).map { (newLog, oldLog) ->
            val diffString = generateDiffString(oldLog, newLog)
            custLogsMapper.toEditResponse(oldLog, newLog, diffString)
        }
    }

    private fun generateDiffString(old: CustMstHst, new: CustMstHst): String {
        val diffs = StringBuilder()
        val propertiesToIgnore = setOf("custMstHstId", "updater", "updateDtime", "creator", "createDtime", "isNew", "updateReason")

        CustMstHst::class.memberProperties.forEach { prop ->
            if (prop.name !in propertiesToIgnore) {
                val oldValue = prop.get(old)
                val newValue = prop.get(new)
                if (oldValue != newValue) {
                    val koreanName = propertyNameMap[prop.name] ?: prop.name
                    diffs.append("${koreanName}: '${oldValue ?: ""}' -> '${newValue ?: ""}'\n")
                }
            }
        }
        return diffs.toString().trimEnd()
    }

    override suspend fun getCustomerLogDetail(logId: String): List<CustDetailLogs> {
        val logs = custLogsCustomRepository.findDiffCustLogByCustMstHstId(logId.toLong()).toList()
        if (logs.size < 2) {
            return emptyList()
        }
        val (previousLog, currentLog) = logs

        val diffDetails = mutableListOf<CustDetailLogs>()
        val propertiesToIgnore = setOf("custMstHstId", "updater", "updateDtime", "creator", "createDtime", "isNew", "updateReason")

        CustMstHst::class.memberProperties.forEach { prop ->
            if (prop.name !in propertiesToIgnore) {
                val oldValue = prop.get(previousLog)
                val newValue = prop.get(currentLog)

                if (oldValue != newValue) {
                    diffDetails.add(
                        CustDetailLogs(
                            custMstId = currentLog.custMstId,
                            fieldName = propertyNameMap[prop.name] ?: prop.name,
                            oldValue = oldValue?.toString() ?: "",
                            newValue = newValue?.toString() ?: ""
                        )
                    )
                }
            }
        }
        return diffDetails
    }

    override suspend fun getCustomerLogRecover(logId: String): CustMstHst {
        TODO("Not yet implemented")
    }

    @Transactional
    override suspend fun putCustomerLogRecover(logId: Long, adminId: String): Cust {
        val custLogs = custLogsRepository.findById(logId)
            ?: throw NoSuchElementException("변경 이력을 찾을 수 없습니다: $logId")

        val custMapper = custLogsMapper.toUpdateCommand(custLogs)

        return custService.updateCust(custLogs.custMstId, custMapper, adminId)
    }
}
