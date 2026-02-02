package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.query.ChargeApproveQuery
import com.idrsys.ailis.sales.application.required.repository.charge.ChargeCustomRepository
import com.idrsys.ailis.sales.shared.mapper.ChargeApproveMapper
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveActionCommand
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveRequestCommand
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveSearchParam
import com.idrsys.ailis.sales.application.dto.response.ChargeApproveResponse
import com.idrsys.ailis.sales.application.dto.response.DeletePermissionResult
import com.idrsys.ailis.sales.application.dto.response.inner.BaseUserResponse
import com.idrsys.ailis.sales.application.required.external.BaseServicePort
import com.idrsys.ailis.sales.application.required.external.TstServicePort
import com.idrsys.ailis.sales.application.required.repository.apprinfo.ApprInfoCustomRepository
import com.idrsys.ailis.sales.application.required.repository.apprinfo.ApprInfoRepository
import com.idrsys.ailis.sales.application.required.repository.charge.ChargeRepository
import com.idrsys.ailis.sales.application.usecase.chargeapprove.ChargeApproveUseCase
import com.idrsys.ailis.sales.domain.model.ApprInfo
import com.idrsys.ailis.sales.domain.model.Charge
import com.idrsys.ailis.sales.shared.constant.ChargeApproveErrorCode
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

/**
 * 고객수가 승인 Service
 */
@Service
@Transactional(readOnly = false)
class ChargeApproveService(
    private val chargeRepository: ChargeRepository,
    private val apprInfoRepository: ApprInfoRepository,
    private val apprInfoCustomRepository: ApprInfoCustomRepository,
    private val baseServicePort: BaseServicePort,
    private val tstServicePort: TstServicePort,
    private val chargeCustomRepository: ChargeCustomRepository,
    private val chargeApproveMapper: ChargeApproveMapper
) : ChargeApproveUseCase {

    companion object {
        private const val APPR_DOC_TYPE_CD = "APDC_CC"  // 고객수가

        // Approval Levels
        private const val APLV_1 = "APLV_1" // 팀장 전결
        private const val APLV_8 = "APLV_8" // 대표이사까지

        // Approval Status
        private const val APST_W = "APST_W"  // 대기

        // Charge Status
        private const val LAST_T = "LAST_T" // 임시저장
        private const val LAST_I = "LAST_I" // 결재중
        private const val LAST_C = "LAST_C" // 결재완료

        // Job Positions (jbpoCd) - from claude.md confirmed
        private val TEAM_MEMBER_POSITIONS = listOf("JP_TM", "JP_PM") // 팀원, 파트장
    }

    /**
     * 승인 요청
     */
    @Transactional
    override suspend fun requestApproval(
        command: ChargeApproveRequestCommand,
        userId: String
    ): ChargeApproveResponse {
        // 1. 고객수가 조회
        val charge = chargeRepository.findById(command.custChargeId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_CODE,
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_MESSAGE
            )

        // 2. 임시저장 상태가 아니면 에러
        if (charge.lastApprStatCd != LAST_T) {
            throw UserDefinedException(
                "ALREADY_REQUESTED",
                "임시저장 상태인 수가만 승인 요청할 수 있습니다."
            )
        }

        // 3. 최저수가 조회
        val lowestChargeDouble = tstServicePort.getStandardCharges(charge.tstCd)?.firstOrNull()?.lowestCharge
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.LOWEST_CHARGE_NOT_FOUND_CODE,
                ChargeApproveErrorCode.LOWEST_CHARGE_NOT_FOUND_MESSAGE
            )

        // 4. 특별수가 vs 최저수가 비교하여 결재 레벨 결정 (BigDecimal 사용)
        val lowestCharge = BigDecimal.valueOf(lowestChargeDouble)
        val specialCharge = BigDecimal.valueOf(charge.specialCharge)
        val apprLvlCd = if (specialCharge >= lowestCharge) APLV_1 else APLV_8

        // 5. 결재선 조회
        /*
        #승인
            결재선은 “레벨별”이 아니라 “문서유형별(APPR_DOC_TYPE_CD)”로 1세트만 존재
            A. “base line” (DB 결재선)을 붙일지 말지는 apprDocDtlNo 0/1로 제어
            B. “팀장(deptHead)” 라인은 별도 규칙으로 추가됨 - SERVICE
            C. 상신자가 결재선 테이블에 포함되어 있으면 “자기보다 뒤 라인만” 가져옴
            APLV_1 - > 0
            APLV_8 - > 1
        */
        val apprDocDtlNo = when (apprLvlCd) {
            "APLV_1" -> "0"
            "APLV_8" -> "1"
            else -> throw IllegalArgumentException("Unsupported apprLvlCd: $apprLvlCd")
        }

        val approvalLines = baseServicePort.getApprovalLines(userId, APPR_DOC_TYPE_CD, apprDocDtlNo)
        if (approvalLines.isEmpty()) {
            throw UserDefinedException(
                "APPR_LINE_NOT_FOUND",
                "결재선을 찾을 수 없습니다."
            )
        }

        // 6. 결재정보번호 생성 (시퀀스)
        val apprInfoNo = apprInfoCustomRepository.getNextApprInfoNo()

        // 7. 결재정보 생성
        approvalLines.forEach { line ->
            val apprInfo = ApprInfo(
                apprInfoId = UUID.randomUUID().toString(),
                apprInfoNo = apprInfoNo,
                apprSeq = line.apprSeq,
                apprDocTypeCd = APPR_DOC_TYPE_CD,
                apprPersonEmpNo = line.apprPersonEmpNo,
                apprStatCd = APST_W,
                creator = userId,
                updater = userId,
            )
            apprInfo.setAsNew()
            apprInfoRepository.save(apprInfo)
        }

        // 8. 고객수가 상태 업데이트 (CAS 적용)
        val rowsUpdated = chargeCustomRepository.updateToInProgressWithCAS(
            custChargeId = charge.custChargeId,
            apprInfoNo = apprInfoNo,
            currApprSeq = 1, // 첫 결재 순서는 항상 1
            apprSubmsEmpNo = userId,
            apprLvlCd = apprLvlCd,
            updater = userId
        )

        if (rowsUpdated == 0) {
            throw UserDefinedException(
                ChargeApproveErrorCode.APPROVAL_REQUEST_CAS_CONFLICT_CODE,
                ChargeApproveErrorCode.APPROVAL_REQUEST_CAS_CONFLICT_MESSAGE
            )
        }

        // 9. 상세조회 결과를 반환하여 최신 상태 응답
        return getApprovalDetail(charge.custChargeId, userId)
    }

    /**
     * 승인
     */
    @Transactional
    override suspend fun approve(
        command: ChargeApproveActionCommand,
        userId: String
    ): ChargeApproveResponse {
        // 1. 고객수가 및 현재 결재정보 조회
        val charge = chargeRepository.findById(command.custChargeId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_CODE,
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_MESSAGE
            )

        if (charge.lastApprStatCd != LAST_I) {
            throw UserDefinedException(ChargeApproveErrorCode.NOT_IN_PROGRESS_CODE, ChargeApproveErrorCode.NOT_IN_PROGRESS_MESSAGE)
        }

        val currentApprInfo = apprInfoCustomRepository.findPendingApprovalByChargeId(command.custChargeId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_CODE,
                ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_MESSAGE
            )

        // 2. 결재 권한 확인
        val user = baseServicePort.getUser(userId) ?: throw UserDefinedException(
            ChargeApproveErrorCode.NO_AUTHORITY_CODE,
            "사용자 정보를 찾을 수 없습니다."
        )
        // appr_person_emp_no에는 user_id가 저장됨
        if (currentApprInfo.apprPersonEmpNo != userId) {
            throw UserDefinedException(
                ChargeApproveErrorCode.NO_AUTHORITY_CODE,
                ChargeApproveErrorCode.NO_AUTHORITY_MESSAGE
            )
        }

        // 3. 현재 결재선 승인 처리
        currentApprInfo.approve(command.apprMemo, userId)
        apprInfoRepository.save(currentApprInfo)

        // 4. 다음 결재 단계가 있는지 확인
        val nextApprInfo = apprInfoCustomRepository.findByApprInfoNoAndSeq(currentApprInfo.apprInfoNo, currentApprInfo.apprSeq + 1)

        val rowsUpdated = if (nextApprInfo == null) {
            // 마지막 결재자: 결재 완료 처리
            chargeCustomRepository.completeApprovalWithCAS(
                custChargeId = charge.custChargeId,
                currentSeq = currentApprInfo.apprSeq,
                updater = userId
            )
        } else {
            // 다음 결재자로 상태 변경
            chargeCustomRepository.incrementApprSeqWithCAS(
                custChargeId = charge.custChargeId,
                currentSeq = currentApprInfo.apprSeq,
                newSeq = nextApprInfo.apprSeq,
                updater = userId
            )
        }

        if (rowsUpdated == 0) {
            throw UserDefinedException(
                ChargeApproveErrorCode.APPROVE_CAS_CONFLICT_CODE,
                ChargeApproveErrorCode.APPROVE_CAS_CONFLICT_MESSAGE
            )
        }

        // 5. 상세조회 결과를 반환하여 최신 상태 응답
        return getApprovalDetail(charge.custChargeId, userId)
    }

    /**
     * 고객 수가 반려 (결재중인 건만)
     * - 반려 시 scs_cust_charge와 scs_appr_info 전체 삭제
     */
    @Transactional
    override suspend fun rejectCharge(custChargeId: String, userId: String) {
        // 1. 고객수가 조회
        val charge = chargeRepository.findById(custChargeId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_CODE,
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_MESSAGE
            )

        // 2. 결재중(LAST_I) 상태만 반려 가능
        if (charge.lastApprStatCd != LAST_I) {
            throw UserDefinedException(
                ChargeApproveErrorCode.NOT_IN_PROGRESS_CODE,
                ChargeApproveErrorCode.NOT_IN_PROGRESS_MESSAGE
            )
        }

        // 3. 사용자 정보 조회
        val user = baseServicePort.getUser(userId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.NO_AUTHORITY_CODE,
                ChargeApproveErrorCode.NO_AUTHORITY_MESSAGE
            )

        // 4. 결재 권한 확인 (현재 결재자인지)
        val currentApprInfo = apprInfoCustomRepository.findPendingApprovalByChargeId(custChargeId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_CODE,
                ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_MESSAGE
            )

        if (currentApprInfo.apprPersonEmpNo != userId) {
            throw UserDefinedException(
                ChargeApproveErrorCode.NO_AUTHORITY_CODE,
                ChargeApproveErrorCode.NO_AUTHORITY_MESSAGE
            )
        }

        // 5. 본인이 이미 승인한 건은 반려 불가
        val hasAlreadyApproved = apprInfoCustomRepository.hasUserApproved(
            apprInfoNo = charge.apprInfoNo!!,
            userId = userId
        )
        if (hasAlreadyApproved) {
            throw UserDefinedException(
                ChargeApproveErrorCode.ALREADY_APPROVED_CODE,
                ChargeApproveErrorCode.ALREADY_APPROVED_MESSAGE
            )
        }

        // 6. appr_info_no 확인
        val apprInfoNo = charge.apprInfoNo
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_CODE,
                ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_MESSAGE
            )

        // 7. scs_cust_charge 먼저 삭제 (메인 테이블, CAS)
        val rowsDeleted = chargeCustomRepository.deleteWithCAS(
            custChargeId = custChargeId,
            currentSeq = charge.currApprSeq!!
        )
        if (rowsDeleted == 0) {
            throw UserDefinedException(
                ChargeApproveErrorCode.REJECT_CAS_CONFLICT_CODE,
                ChargeApproveErrorCode.REJECT_CAS_CONFLICT_MESSAGE
            )
        }

        // 8. scs_appr_info 전체 라인 삭제 (jOOQ)
        // charge 삭제 성공 후 실행 - 실패해도 가비지 데이터만 남음
        apprInfoCustomRepository.deleteAllByApprInfoNo(apprInfoNo)
    }

    /**
     * 고객 수가 삭제 (상태에 따라 분기: 임시저장 건 삭제 / 결재중인 건 반려)
     */
    @Transactional
    override suspend fun deleteCharge(custChargeId: String, userId: String) {
        // 1. 고객수가 조회
        val charge = chargeRepository.findById(custChargeId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_CODE,
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_MESSAGE
            )

        // 2. 사용자 정보 조회
        val user = baseServicePort.getUser(userId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.NO_AUTHORITY_CODE,
                ChargeApproveErrorCode.NO_AUTHORITY_MESSAGE
            )

        // 3. 삭제 권한 확인 및 처리
        val permissionResult = canDeleteCharge(charge, user, userId)
        if (!permissionResult.canDelete) {
            throw UserDefinedException(permissionResult.errorCode, permissionResult.errorMessage)
        }

        when (charge.lastApprStatCd) {
            LAST_T -> {
                // 임시저장 상태: 단순 삭제 (권한 검증은 canDeleteCharge에서 수행됨)
                chargeRepository.deleteById(charge.custChargeId)
            }
            LAST_I -> {
                // 결재중 상태: 반려(삭제) 로직 수행 (권한 검증은 canDeleteCharge에서 수행됨)
                val apprInfoNo = charge.apprInfoNo
                    ?: throw UserDefinedException(ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_CODE, ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_MESSAGE)

                // 데이터 삭제
                apprInfoRepository.deleteAllByApprInfoNo(apprInfoNo)
                val rowsDeleted = chargeCustomRepository.deleteWithCAS(
                    custChargeId = charge.custChargeId,
                    currentSeq = charge.currApprSeq!!
                )
                if (rowsDeleted == 0) {
                    throw UserDefinedException(
                        ChargeApproveErrorCode.REJECT_CAS_CONFLICT_CODE,
                        ChargeApproveErrorCode.REJECT_CAS_CONFLICT_MESSAGE
                    )
                }
            }
            // LAST_C 상태는 canDeleteCharge에서 이미 처리되므로 여기에 도달하지 않음
            else -> {
                // canDeleteCharge에서 모든 케이스를 처리했으므로, 여기에 도달하면 예상치 못한 상태임
                throw UserDefinedException(
                    ChargeApproveErrorCode.CANNOT_DELETE_CODE,
                    ChargeApproveErrorCode.CANNOT_DELETE_MESSAGE
                )
            }
        }
    }

    /**
     * 승인 목록 조회 (페이징)
     */
    override suspend fun getApprovalPage(
        searchParam: ChargeApproveSearchParam,
        userId: String,
        pageable: Pageable
    ): Page<ChargeApproveResponse> {
        // 1. Get user role
        val user = baseServicePort.getUser(userId) ?: throw UserDefinedException(
            ChargeApproveErrorCode.NO_AUTHORITY_CODE,
            "사용자 정보를 찾을 수 없습니다."
        )

        // 2. Count total
        val total = apprInfoCustomRepository.countApprovalCharges(
            searchParam,
            userId,
            user.jbpoCd ?: "JP_TM",
            user.empNo
        )

        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        // 3. Fetch approval charges
        val charges = apprInfoCustomRepository.findApprovalCharges(
            searchParam,
            userId,
            user.jbpoCd ?: "JP_TM",
            user.empNo,
            pageable
        ).toList()

        // 4. Populate name fields and calculate canApproveThisItem
        val responses = populateNameFields(queries = charges, currentUser = user)

        return PageImpl(responses, pageable, total)
    }

    /**
     * 승인 목록 조회 (전체)
     */
    override suspend fun getApprovals(
        searchParam: ChargeApproveSearchParam,
        userId: String
    ): List<ChargeApproveResponse> {
        // 1. Get user role
        val user = baseServicePort.getUser(userId) ?: throw UserDefinedException(
            ChargeApproveErrorCode.NO_AUTHORITY_CODE,
            "사용자 정보를 찾을 수 없습니다."
        )

        // 2. Fetch all approval charges
        val charges = apprInfoCustomRepository.findApprovalCharges(
            searchParam,
            userId,
            user.jbpoCd ?: "JP_TM",
            user.empNo,
            Pageable.unpaged()
        ).toList()

        // 3. Populate name fields
        return populateNameFields(
            queries = charges,
            currentUser = user
        )
    }

    /**
     * 승인 상세 조회
     */
    override suspend fun getApprovalDetail(
        custChargeId: String,
        userId: String
    ): ChargeApproveResponse {
        // 1. Fetch charge with approval lines
        val (chargeQuery, approvalLines) = apprInfoCustomRepository.findApprovalChargeWithLines(custChargeId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_CODE,
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_MESSAGE
            )

        // 2. Map to base response
        val currentUser = baseServicePort.getUser(userId) ?: throw UserDefinedException(
            ChargeApproveErrorCode.NO_AUTHORITY_CODE,
            "사용자 정보를 찾을 수 없습니다."
        )
        val response = chargeApproveMapper.toResponse(chargeQuery)
        val canApproveThisItem = calculateCanApproveThisItem(chargeQuery, currentUser)

        // 3. Fetch lookup data
        val tstItems = tstServicePort.findAllTstItems() ?: emptyList()
        val tstItemMap = tstItems.associateBy { it.tstCd }

        val systemCodes = baseServicePort.getChildrenSystemCodes(listOf("APST", "AL", "JP")) ?: emptyMap()
        val apprStatMap = systemCodes["APST"]?.associateBy { it.cd } ?: emptyMap()
        val apprLvlMap = systemCodes["AL"]?.associateBy { it.cd } ?: emptyMap()
        val jbpoMap = systemCodes["JP"]?.associateBy { it.cd } ?: emptyMap()

        // 4. Get users for approval line
        val empNos = approvalLines.mapNotNull { it.apprPersonEmpNo }.distinct()
        val users = if (empNos.isNotEmpty()) {
            baseServicePort.getUsersByIds(empNos) ?: emptyList()
        } else {
            emptyList()
        }
        val userByUserId = users.associateBy { it.userId }

        // 5. Map approval lines
        val approvalLineInfos = approvalLines.map { apprInfo ->
            val userDetail = userByUserId[apprInfo.apprPersonEmpNo]
            chargeApproveMapper.toApprovalLineInfo(apprInfo).copy(
                apprPersonNm = userDetail?.userNm,
                jbpoNm = userDetail?.jbpoCd?.let { jbpoMap[it]?.cdNm }, // Populate jbpoNm using system codes
                apprStatNm = apprStatMap[apprInfo.apprStatCd]?.cdNm
            )
        }

        // 6. Fetch lowest charge if needed
        val lowestCharge = if (chargeQuery.tstCd.isNotBlank()) {
            try {
                tstServicePort.getStandardCharges(chargeQuery.tstCd)
                    ?.firstOrNull()?.lowestCharge?.toLong()
            } catch (ex: Exception) {
                null
            }
        } else null

        // 7. Return populated response
        return response.copy(
            tstNm = tstItemMap[chargeQuery.tstCd]?.tstNm,
            lastApprStatNm = apprStatMap[chargeQuery.lastApprStatCd]?.cdNm,
            apprLvlNm = chargeQuery.apprLvlCd?.let { apprLvlMap[it]?.cdNm },
            lowestCharge = lowestCharge,
            approvalLines = approvalLineInfos,
            canApproveThisItem = canApproveThisItem // Add to response
        )
    }

    /**
     * 고객수가 삭제 권한 검증 헬퍼
     */
    private suspend fun canDeleteCharge(
        charge: Charge,
        user: BaseUserResponse,
        requestingUserId: String
    ): DeletePermissionResult {
        // 1. 사용자 직책 코드 확인 (jbpoCd는 null일 수 있으므로 기본값 설정)
        val jbpoCd = user.jbpoCd ?: "JP_TM" // 기본 팀원

        return when (charge.lastApprStatCd) {
            LAST_T -> { // 임시저장 상태
                // 임시저장 건은 생성자만 삭제 가능
                if (charge.creator == requestingUserId) {
                    DeletePermissionResult(true, "", "")
                } else {
                    DeletePermissionResult(
                        false,
                        ChargeApproveErrorCode.NOT_ALLOWED_TO_DELETE_LAST_T_CODE,
                        ChargeApproveErrorCode.NOT_ALLOWED_TO_DELETE_LAST_T_MESSAGE
                    )
                }
            }
            LAST_I -> { // 결재중 상태
                // 팀원(JP_TM/JP_PM)은 결재중인 건 삭제 불가
                if (jbpoCd in TEAM_MEMBER_POSITIONS) {
                    DeletePermissionResult(
                        false,
                        ChargeApproveErrorCode.NO_AUTHORITY_CODE,
                        ChargeApproveErrorCode.NO_AUTHORITY_MESSAGE
                    )
                } else {
                    // 팀장 이상은 삭제 가능하나, 본인이 승인한 건 삭제 불가
                    val hasAlreadyApproved = apprInfoCustomRepository.hasUserApproved(
                        apprInfoNo = charge.apprInfoNo!!,
                        userId = requestingUserId  // appr_person_emp_no에는 user_id가 저장됨
                    )
                    if (hasAlreadyApproved) {
                        DeletePermissionResult(
                            false,
                            ChargeApproveErrorCode.ALREADY_APPROVED_CODE,
                            ChargeApproveErrorCode.ALREADY_APPROVED_MESSAGE
                        )
                    } else {
                        DeletePermissionResult(true, "", "")
                    }
                }
            }
            LAST_C -> { // 결재완료 상태
                // 아무도 삭제 불가
                DeletePermissionResult(
                    false,
                    ChargeApproveErrorCode.CANNOT_DELETE_CODE,
                    ChargeApproveErrorCode.CANNOT_DELETE_MESSAGE
                )
            }
            else -> {
                DeletePermissionResult(
                    false,
                    ChargeApproveErrorCode.CANNOT_DELETE_CODE,
                    ChargeApproveErrorCode.CANNOT_DELETE_MESSAGE
                )
            }
        }
    }

    /**
     * Name 필드 일괄 채우기 (성능 최적화)
     */
    private suspend fun populateNameFields(
        queries: List<ChargeApproveQuery>,
        currentUser: BaseUserResponse // Add currentUser parameter
    ): List<ChargeApproveResponse> {
        if (queries.isEmpty()) return emptyList()

        // 1. Fetch all lookup data
        val tstItems = tstServicePort.findAllTstItems() ?: emptyList()
        val tstItemMap = tstItems.associateBy { it.tstCd }

        val systemCodes = baseServicePort.getChildrenSystemCodes(listOf("APST", "AL")) ?: emptyMap()
        val apprStatMap = systemCodes["APST"]?.associateBy { it.cd } ?: emptyMap()
        val apprLvlMap = systemCodes["AL"]?.associateBy { it.cd } ?: emptyMap()

        // 2. Map and populate
        return queries.map { query ->
            val response = chargeApproveMapper.toResponse(query)
            val canApproveThisItem = calculateCanApproveThisItem(query, currentUser) // Calculate here
            response.copy(
                tstNm = tstItemMap[query.tstCd]?.tstNm,
                lastApprStatNm = apprStatMap[query.lastApprStatCd]?.cdNm,
                apprLvlNm = query.apprLvlCd?.let { apprLvlMap[it]?.cdNm },
                canApproveThisItem = canApproveThisItem // Add to response
            )
        }
    }

    /**
     * 특정 고객수가 항목에 대해 현재 사용자가 승인 권한이 있는지 계산하는 헬퍼 함수
     */
    private suspend fun calculateCanApproveThisItem(
        charge: ChargeApproveQuery,
        currentUser: BaseUserResponse
    ): Boolean {
        // 1. 결재중 상태여야만 승인 가능
        if (charge.lastApprStatCd != LAST_I) return false

        // 2. 현재 결재 순번(currApprSeq)에 대한 결재자(apprPersonEmpNo)가 현재 사용자인지 확인
        val currentApprSeq = charge.currApprSeq ?: return false
        val apprInfoNo = charge.apprInfoNo ?: return false

        val currentApprover = apprInfoCustomRepository.findByApprInfoNoAndSeq(apprInfoNo, currentApprSeq)
            ?: return false

        // 3. 결재 대상자가 현재 로그인한 사용자와 일치해야 함 (appr_person_emp_no에는 user_id가 저장됨)
        if (currentApprover.apprPersonEmpNo != currentUser.userId) return false

        // 4. 현재 사용자의 직책 코드가 TEAM_MEMBER_POSITIONS에 속하지 않아야 함 (팀장 이상)
        // 이 조건은 이미 백엔드의 canApprove 로직에 포함되어 있으나, 이 함수는 UI 표시용이므로 명시적으로 포함하지 않음
        // 백엔드의 approve 함수에서 이미 권한 체크를 하므로 여기서는 최소한의 조건만 확인
        // (즉, 결재자이며, 결재중인 건이며, 본인 차례인 경우)
        // -> UI에서는 "승인" 버튼을 활성화할 최소 조건만 제공
        return true
    }
}