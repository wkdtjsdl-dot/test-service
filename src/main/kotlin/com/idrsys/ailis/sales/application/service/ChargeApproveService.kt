package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.required.repository.charge.ChargeCustomRepository
import com.idrsys.ailis.sales.shared.mapper.ChargeApproveMapper
import com.idrsys.ailis.sales.adapter.external.BaseServiceClient
import com.idrsys.ailis.sales.adapter.external.TstServiceClient
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveActionCommand
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveRequestCommand
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveSearchParam
import com.idrsys.ailis.sales.application.dto.response.ChargeApproveResponse
import com.idrsys.ailis.sales.application.required.client.ApprovalLineClient
import com.idrsys.ailis.sales.application.required.client.UserClient
import com.idrsys.ailis.sales.application.required.repository.apprinfo.ApprInfoCustomRepository
import com.idrsys.ailis.sales.application.required.repository.apprinfo.ApprInfoRepository
import com.idrsys.ailis.sales.application.required.repository.charge.ChargeRepository
import com.idrsys.ailis.sales.application.usecase.chargeapprove.ChargeApproveUseCase
import com.idrsys.ailis.sales.domain.model.ApprInfo
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
    private val userClient: UserClient,
    private val approvalLineClient: ApprovalLineClient,
    private val tstServiceClient: TstServiceClient,
    private val baseServiceClient: BaseServiceClient,
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
                "ALREADY_REQUESTED", // TODO: Add to ErrorCode
                "임시저장 상태인 수가만 승인 요청할 수 있습니다."
            )
        }

        // 3. 최저수가 조회
        val lowestChargeDouble = tstServiceClient.getStandardCharges(charge.tstCd)?.firstOrNull()?.lowestCharge
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.LOWEST_CHARGE_NOT_FOUND_CODE,
                ChargeApproveErrorCode.LOWEST_CHARGE_NOT_FOUND_MESSAGE
            )

        // 4. 특별수가 vs 최저수가 비교하여 결재 레벨 결정 (BigDecimal 사용)
        val lowestCharge = BigDecimal.valueOf(lowestChargeDouble)
        val specialCharge = BigDecimal.valueOf(charge.specialCharge)
        val apprLvlCd = if (specialCharge >= lowestCharge) APLV_1 else APLV_8

        // 5. 결재선 조회
        val apprDocDtlNo = apprLvlCd.substringAfterLast('_') // "APLV_1" -> "1"
        val approvalLines = approvalLineClient.getApprovalLines(userId, APPR_DOC_TYPE_CD, apprDocDtlNo)
        if (approvalLines.isEmpty()) {
            throw UserDefinedException(
                "APPR_LINE_NOT_FOUND", // TODO: Add to ErrorCode
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
        val user = userClient.getUser(userId) ?: throw UserDefinedException(
            ChargeApproveErrorCode.NO_AUTHORITY_CODE,
            "사용자 정보를 찾을 수 없습니다."
        )
        if (currentApprInfo.apprPersonEmpNo != user.empNo) {
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
     * 고객 수가 삭제 (상태에 따라 분기: 임시저장 건 삭제 / 결재중인 건 반려)
     */
    @Transactional
    override suspend fun deleteCharge(custChargeId: String, userId: String) {
        val charge = chargeRepository.findById(custChargeId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_CODE,
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_MESSAGE
            )

        val user = userClient.getUser(userId) ?: throw UserDefinedException(
            ChargeApproveErrorCode.NO_AUTHORITY_CODE,
            "사용자 정보를 찾을 수 없습니다."
        )

        when (charge.lastApprStatCd) {
            LAST_T -> {
                // 임시저장 상태: 단순 삭제
                // TODO: 임시저장도 본인만 삭제 가능한지 등 권한 정책 확정 필요
                chargeRepository.deleteById(charge.custChargeId)
            }
            LAST_I -> {
                // 결재중 상태: 반려(삭제) 로직 수행
                val apprInfoNo = charge.apprInfoNo?.toLongOrNull()
                    ?: throw UserDefinedException(ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_CODE, "결재 정보가 없는 수가입니다.")

                // 현재 결재자 또는 상위 결재자만 반려 가능 (정책에 따라)
                // 여기서는 현재 결재자만 가능하다고 가정
                val currentApprInfo = apprInfoCustomRepository.findPendingApprovalByChargeId(charge.custChargeId)
                    ?: throw UserDefinedException(
                        ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_CODE,
                        ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_MESSAGE
                    )
                if (currentApprInfo.apprPersonEmpNo != user.empNo) {
                    throw UserDefinedException(
                        ChargeApproveErrorCode.NO_AUTHORITY_CODE,
                        "현재 결재 담당자가 아니므로 반려할 수 없습니다."
                    )
                }

                // 이미 승인한 건은 반려 불가
                val hasAlreadyApproved = apprInfoCustomRepository.hasUserApproved(apprInfoNo, user.empNo!!)
                if (hasAlreadyApproved) {
                    throw UserDefinedException(
                        ChargeApproveErrorCode.ALREADY_APPROVED_CODE,
                        ChargeApproveErrorCode.ALREADY_APPROVED_MESSAGE
                    )
                }

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
            else -> {
                throw UserDefinedException("CANNOT_DELETE", "임시저장 또는 결재중인 수가만 삭제할 수 있습니다.")
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
        val user = userClient.getUser(userId) ?: throw UserDefinedException(
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

        // 4. Populate name fields
        val responses = populateNameFields(charges)

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
        val user = userClient.getUser(userId) ?: throw UserDefinedException(
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
        return populateNameFields(charges)
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
        val response = chargeApproveMapper.toResponse(chargeQuery)

        // 3. Fetch lookup data
        val tstItems = tstServiceClient.findAllTstItems() ?: emptyList()
        val tstItemMap = tstItems.associateBy { it.tstCd }

        val systemCodes = baseServiceClient.getChildrenSystemCodes(listOf("APST", "AL")) ?: emptyMap()
        val apprStatMap = systemCodes["APST"]?.associateBy { it.cd } ?: emptyMap()
        val apprLvlMap = systemCodes["AL"]?.associateBy { it.cd } ?: emptyMap()

        // 4. Get users for approval line
        val empNos = approvalLines.mapNotNull { it.apprPersonEmpNo }.distinct()
        val users = if (empNos.isNotEmpty()) {
            baseServiceClient.getUsers() ?: emptyList()
        } else {
            emptyList()
        }
        val userByEmpNo = users.associateBy { it.empNo }

        // 5. Map approval lines
        val approvalLineInfos = approvalLines.map { apprInfo ->
            chargeApproveMapper.toApprovalLineInfo(apprInfo).copy(
                apprPersonNm = userByEmpNo[apprInfo.apprPersonEmpNo]?.userNm,
                apprStatNm = apprStatMap[apprInfo.apprStatCd]?.cdNm
            )
        }

        // 6. Fetch lowest charge if needed
        val lowestCharge = if (chargeQuery.tstCd.isNotBlank()) {
            try {
                tstServiceClient.getStandardCharges(chargeQuery.tstCd)
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
            approvalLines = approvalLineInfos
        )
    }

    /**
     * Name 필드 일괄 채우기 (성능 최적화)
     */
    private suspend fun populateNameFields(
        queries: List<com.idrsys.ailis.sales.application.dto.query.ChargeApproveQuery>
    ): List<ChargeApproveResponse> {
        if (queries.isEmpty()) return emptyList()

        // 1. Fetch all lookup data
        val tstItems = tstServiceClient.findAllTstItems() ?: emptyList()
        val tstItemMap = tstItems.associateBy { it.tstCd }

        val systemCodes = baseServiceClient.getChildrenSystemCodes(listOf("APST", "AL")) ?: emptyMap()
        val apprStatMap = systemCodes["APST"]?.associateBy { it.cd } ?: emptyMap()
        val apprLvlMap = systemCodes["AL"]?.associateBy { it.cd } ?: emptyMap()

        // 2. Map and populate
        return queries.map { query ->
            val response = chargeApproveMapper.toResponse(query)
            response.copy(
                tstNm = tstItemMap[query.tstCd]?.tstNm,
                lastApprStatNm = apprStatMap[query.lastApprStatCd]?.cdNm,
                apprLvlNm = query.apprLvlCd?.let { apprLvlMap[it]?.cdNm }
            )
        }
    }
}
