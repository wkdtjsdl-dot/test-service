package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.apprinfo.ApprInfoCreateRequest
import com.idrsys.ailis.sales.application.required.repository.apprinfo.ApprInfoCustomRepository
import com.idrsys.ailis.sales.application.required.repository.apprinfo.ApprInfoRepository
import com.idrsys.ailis.sales.application.usecase.apprinfo.ApprInfoInnerUseCase
import com.idrsys.ailis.sales.domain.model.ApprInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ApprInfoInnerService(
    private val apprInfoRepository: ApprInfoRepository,
    private val apprInfoCustomRepository: ApprInfoCustomRepository
) : ApprInfoInnerUseCase {

    @Transactional
    override suspend fun getNextApprInfoNo(): Long {
        return apprInfoCustomRepository.getNextApprInfoNo()
    }

    @Transactional
    override suspend fun saveApprInfoBatch(requests: List<ApprInfoCreateRequest>) {
        val now = LocalDateTime.now()

        requests.forEach { request ->
            val apprInfo = ApprInfo(
                apprInfoId = UUID.randomUUID().toString(),
                apprInfoNo = request.apprInfoNo,
                apprSeq = request.apprSeq,
                apprDocTypeCd = request.apprDocTypeCd,
                apprPersonEmpNo = request.apprPersonEmpNo,
                apprStatCd = request.apprStatCd,
                apprCmplDtime = if (request.apprStatCd == "APST_C") now else null,
                apprMemo = request.apprMemo,
                creator = request.creator,
                createDtime = now,
                updater = request.creator,
                updateDtime = now
            )
            apprInfo.setAsNew()
            apprInfoRepository.save(apprInfo)
        }
    }

    @Transactional
    override suspend fun deleteByApprInfoNo(apprInfoNo: Long) {
        apprInfoRepository.deleteAllByApprInfoNo(apprInfoNo)
    }

    override suspend fun findApproverByApprInfoNoAndSeq(apprInfoNo: Long, apprSeq: Int): String? {
        val apprInfo = apprInfoCustomRepository.findByApprInfoNoAndSeq(apprInfoNo, apprSeq)
        return apprInfo?.apprPersonEmpNo
    }

    override suspend fun findMyApprovalInfoNos(userId: String, apprDocTypeCds: List<String>): List<Long> {
        return apprInfoCustomRepository.findMyApprovalInfoNos(userId, apprDocTypeCds)
    }

    @Transactional
    override suspend fun approveApprInfo(apprInfoNo: Long, apprSeq: Int, apprMemo: String?, userId: String) {
        val apprInfo = apprInfoCustomRepository.findByApprInfoNoAndSeq(apprInfoNo, apprSeq)
            ?: throw IllegalStateException("결재정보를 찾을 수 없습니다.")

        apprInfo.approve(apprMemo, userId)
        apprInfoRepository.save(apprInfo)
    }

    override suspend fun hasNextApprover(apprInfoNo: Long, currentSeq: Int): Boolean {
        val nextApprInfo = apprInfoCustomRepository.findByApprInfoNoAndSeq(apprInfoNo, currentSeq + 1)
        return nextApprInfo != null
    }

}
