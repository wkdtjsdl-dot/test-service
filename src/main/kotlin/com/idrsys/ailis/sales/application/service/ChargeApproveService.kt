package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveActionCommand
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveRequestCommand
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveSearchParam
import com.idrsys.ailis.sales.application.dto.response.ChargeApproveResponse
import com.idrsys.ailis.sales.application.required.client.ApprovalLineClient
import com.idrsys.ailis.sales.application.required.client.TestChargeClient
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
    private val testChargeClient: TestChargeClient,
    private val baseServiceClient: com.idrsys.ailis.sales.adapter.external.BaseServiceClient,
    private val tstServiceClient: com.idrsys.ailis.sales.adapter.external.TstServiceClient,
    private val chargeApproveMapper: com.idrsys.ailis.sales.shared.mapper.ChargeApproveMapper
) : ChargeApproveUseCase {

    companion object {
        private const val APPR_DOC_TYPE_CD = "APDC_CC"  // 고객수가
        private const val APPR_LVL_TEAM_LEADER = "AL_TL"  // 팀장 전결
        private const val APPR_LVL_MULTI = "AL_MULTI"  // 다단계 승인
        private const val APPR_STAT_WAITING = "APST_W"  // 대기중
    }

    /**
     * 승인 요청
     */
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

        // 2. 이미 승인 요청된 경우 체크
        if (charge.lastApprStatCd != "LAST_T") {  // 임시저장이 아니면
            throw UserDefinedException(
                ChargeApproveErrorCode.ALREADY_REQUESTED_CODE,
                ChargeApproveErrorCode.ALREADY_REQUESTED_MESSAGE
            )
        }

        // 3. 최저수가 조회
        val standardCharges = testChargeClient.getStandardCharges(charge.tstCd)
        val lowestCharge = standardCharges.firstOrNull()?.lowestCharge?.toLong()
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.LOWEST_CHARGE_FETCH_FAILED_CODE,
                ChargeApproveErrorCode.LOWEST_CHARGE_FETCH_FAILED_MESSAGE
            )

        // 4. 특별수가 vs 최저수가 비교하여 결재 레벨 결정
        val apprLvlCd = if (charge.specialCharge >= lowestCharge) {
            APPR_LVL_TEAM_LEADER  // 팀장 전결
        } else {
            APPR_LVL_MULTI  // 다단계 승인
        }

        // 5. 결재선 조회
        val apprDocDtlNo = if (apprLvlCd == APPR_LVL_TEAM_LEADER) "0" else "1"
        val approvalLines = approvalLineClient.getApprovalLines(userId, APPR_DOC_TYPE_CD, apprDocDtlNo)

        if (approvalLines.isEmpty()) {
            throw UserDefinedException(
                ChargeApproveErrorCode.APPROVAL_LINE_FETCH_FAILED_CODE,
                ChargeApproveErrorCode.APPROVAL_LINE_FETCH_FAILED_MESSAGE
            )
        }

        // 6. 결재정보번호 생성 (시퀀스)
        val apprInfoNo = System.currentTimeMillis()  // TODO: 실제 시퀀스로 교체 필요

        // 7. 결재정보 생성
        approvalLines.forEach { line ->
            val apprInfo = ApprInfo(
                apprInfoId = UUID.randomUUID().toString(),
                apprInfoNo = apprInfoNo,
                apprSeq = line.apprSeq,
                apprDocTypeCd = APPR_DOC_TYPE_CD,
                apprPersonEmpNo = line.apprPersonEmpNo,
                apprStatCd = APPR_STAT_WAITING,
                creator = userId,
                updater = userId,
            )
            apprInfo.setAsNew()
            apprInfoRepository.save(apprInfo)
        }

        // 8. 고객수가 상태 업데이트
        charge.requestApproval(apprInfoNo, userId, apprLvlCd, userId)
        chargeRepository.save(charge)

        // 9. 응답 생성
        return ChargeApproveResponse(
            custChargeId = charge.custChargeId,
            custCd = charge.custCd,
            custNm = null,
            tstCd = charge.tstCd,
            tstNm = null,
            applyStartDt = charge.applyStartDt,
            applyEndDt = charge.applyEndDt,
            specialCharge = charge.specialCharge,
            stndPrice = charge.stndPrice,
            lowestCharge = lowestCharge,
            supval = charge.supval,
            addtax = charge.addtax,
            remark = charge.remark,
            apprInfoNo = apprInfoNo,
            currApprSeq = charge.currApprSeq,
            apprSubmsEmpNo = charge.apprSubmsEmpNo,
            apprSubmsDtime = charge.apprSubmsDtime,
            lastApprStatCd = charge.lastApprStatCd,
            lastApprStatNm = null,
            apprLvlCd = charge.apprLvlCd,
            apprLvlNm = null,
        )
    }

    /**
     * 승인
     */
    override suspend fun approve(
        command: ChargeApproveActionCommand,
        userId: String
    ): ChargeApproveResponse {
        // 1. 고객수가 조회
        val charge = chargeRepository.findById(command.custChargeId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_CODE,
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_MESSAGE
            )

        // 2. 현재 결재 대기 중인 ApprInfo 조회
        val currentApprInfo = apprInfoCustomRepository.findPendingApprovalByChargeId(command.custChargeId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_CODE,
                ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_MESSAGE
            )

        // 3. 결재 권한 확인
        val user = userClient.getUser(userId)
        if (currentApprInfo.apprPersonEmpNo != user.empNo) {
            throw UserDefinedException(
                ChargeApproveErrorCode.NO_APPROVAL_AUTHORITY_CODE,
                ChargeApproveErrorCode.NO_APPROVAL_AUTHORITY_MESSAGE
            )
        }

        // 4. 승인 처리
        currentApprInfo.approve(command.apprMemo, userId)
        apprInfoRepository.save(currentApprInfo)

        // 5. 전체 결재선 조회
        val allApprInfos = apprInfoRepository.findAllByApprInfoNo(currentApprInfo.apprInfoNo)

        // 6. 마지막 결재자인지 확인
        val isLastApprover = allApprInfos.size == currentApprInfo.apprSeq

        if (isLastApprover) {
            // 결재 완료
            charge.completeApproval(userId)
        } else {
            // 다음 결재자로 이동
            charge.proceedToNextApprover(userId)
        }
        chargeRepository.save(charge)

        // 7. 응답 생성
        return ChargeApproveResponse(
            custChargeId = charge.custChargeId,
            custCd = charge.custCd,
            custNm = null,
            tstCd = charge.tstCd,
            tstNm = null,
            applyStartDt = charge.applyStartDt,
            applyEndDt = charge.applyEndDt,
            specialCharge = charge.specialCharge,
            stndPrice = charge.stndPrice,
            lowestCharge = null,
            supval = charge.supval,
            addtax = charge.addtax,
            remark = charge.remark,
            apprInfoNo = charge.apprInfoNo,
            currApprSeq = charge.currApprSeq,
            apprSubmsEmpNo = charge.apprSubmsEmpNo,
            apprSubmsDtime = charge.apprSubmsDtime,
            lastApprStatCd = charge.lastApprStatCd,
            lastApprStatNm = null,
            apprLvlCd = charge.apprLvlCd,
            apprLvlNm = null,
        )
    }

    /**
     * 반려
     */
    override suspend fun reject(
        command: ChargeApproveActionCommand,
        userId: String
    ): ChargeApproveResponse {
        // 1. 고객수가 조회
        val charge = chargeRepository.findById(command.custChargeId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_CODE,
                ChargeApproveErrorCode.CHARGE_NOT_FOUND_MESSAGE
            )

        // 2. 현재 결재 대기 중인 ApprInfo 조회
        val currentApprInfo = apprInfoCustomRepository.findPendingApprovalByChargeId(command.custChargeId)
            ?: throw UserDefinedException(
                ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_CODE,
                ChargeApproveErrorCode.APPROVAL_INFO_NOT_FOUND_MESSAGE
            )

        // 3. 결재 권한 확인
        val user = userClient.getUser(userId)
        if (currentApprInfo.apprPersonEmpNo != user.empNo) {
            throw UserDefinedException(
                ChargeApproveErrorCode.NO_APPROVAL_AUTHORITY_CODE,
                ChargeApproveErrorCode.NO_APPROVAL_AUTHORITY_MESSAGE
            )
        }

        // 4. 반려 처리
        currentApprInfo.reject(command.apprMemo, userId)
        apprInfoRepository.save(currentApprInfo)

        // 5. 고객수가 반려 처리
        charge.rejectApproval(userId)
        chargeRepository.save(charge)

        // 6. 응답 생성
        return ChargeApproveResponse(
            custChargeId = charge.custChargeId,
            custCd = charge.custCd,
            custNm = null,
            tstCd = charge.tstCd,
            tstNm = null,
            applyStartDt = charge.applyStartDt,
            applyEndDt = charge.applyEndDt,
            specialCharge = charge.specialCharge,
            stndPrice = charge.stndPrice,
            lowestCharge = null,
            supval = charge.supval,
            addtax = charge.addtax,
            remark = charge.remark,
            apprInfoNo = null,
            currApprSeq = null,
            apprSubmsEmpNo = null,
            apprSubmsDtime = null,
            lastApprStatCd = charge.lastApprStatCd,
            lastApprStatNm = null,
            apprLvlCd = null,
            apprLvlNm = null,
        )
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
        val user = userClient.getUser(userId)

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
        val user = userClient.getUser(userId)

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
                testChargeClient.getStandardCharges(chargeQuery.tstCd)
                    .firstOrNull()?.lowestCharge?.toLong()
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
