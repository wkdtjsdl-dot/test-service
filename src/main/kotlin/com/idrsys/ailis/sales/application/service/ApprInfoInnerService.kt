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
}
