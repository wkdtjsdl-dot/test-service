package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoCommand
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.GcgnSalsPicInfoResponse
import com.idrsys.ailis.sales.application.required.repository.gcgnSalsPicInfo.GcgnSalsPicInfoCustomRepository
import com.idrsys.ailis.sales.application.required.repository.gcgnSalsPicInfo.GcgnSalsPicInfoRepository
import com.idrsys.ailis.sales.application.usecase.gcgnSalsPicInfo.GcgnSalsPicInfoUseCase
import com.idrsys.ailis.sales.adapter.external.BaseServiceClient
import com.idrsys.ailis.sales.domain.model.GcgnSalsPicInfo
import com.idrsys.ailis.sales.shared.mapper.GcgnSalsPicInfoMapper
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GcgnSalsPicInfoService(
    private val gcgnSalsPicInfoRepository: GcgnSalsPicInfoRepository,
    private val gcgnSalsPicInfoCustomRepository: GcgnSalsPicInfoCustomRepository,
    private val gcgnSalsPicInfoMapper: GcgnSalsPicInfoMapper,
    private val baseServiceClient: BaseServiceClient,
) : GcgnSalsPicInfoUseCase {

    override suspend fun getGcgnSalsPicInfoPage(searchParam: GcgnSalsPicInfoSearchParam, pageable: Pageable): Page<GcgnSalsPicInfoResponse> {
        val total = gcgnSalsPicInfoCustomRepository.countGcgnSalsPicInfos(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val gcgnSalsPicInfos = gcgnSalsPicInfoCustomRepository.findGcgnSalsPicInfos(searchParam, pageable).map { dto ->
            val empNm = dto.empUserId.let { baseServiceClient.getUser(it)?.userNm }
            gcgnSalsPicInfoMapper.toResponse(dto.copy(empNm = empNm))
        }.toList()

        return PageImpl(gcgnSalsPicInfos, pageable, total)
    }

    override suspend fun getGcgnSalsPicInfoDetail(gcgnSalsPicInfoId: Long): GcgnSalsPicInfoResponse {
        val dto = gcgnSalsPicInfoCustomRepository.findGcgnSalsPicInfoById(gcgnSalsPicInfoId)
            ?: throw NoSuchElementException("GcgnSalsPicInfo not found with id: $gcgnSalsPicInfoId")

        val empNm = dto.empUserId.let { baseServiceClient.getUser(it)?.userNm }
        return gcgnSalsPicInfoMapper.toResponse(dto.copy(empNm = empNm))
    }

    override suspend fun createGcgnSalsPicInfo(command: GcgnSalsPicInfoCommand, adminId: String): GcgnSalsPicInfoResponse {
        val now = LocalDateTime.now()
        val gcgnSalsPicInfo = GcgnSalsPicInfo(
            custMstId = command.custMstId,
            applyStartDt = command.applyStartDt,
            salsTeamCd = command.salsTeamCd,
            empUserId = command.empUserId,
            custCd = command.custCd,
            applyEndDt = command.applyEndDt,
            creator = adminId,
            createDtime = now,
            updater = adminId,
            updateDtime = now
        ).apply { setAsNew() }

        val savedGcgnSalsPicInfo = gcgnSalsPicInfoRepository.save(gcgnSalsPicInfo)
        val response = gcgnSalsPicInfoMapper.toResponse(savedGcgnSalsPicInfo)
        val empNm = response.empUserId.let { baseServiceClient.getUser(it)?.userNm }
        return response.copy(empNm = empNm)
    }

    override suspend fun updateGcgnSalsPicInfo(gcgnSalsPicInfoId: Long, command: GcgnSalsPicInfoCommand, adminId: String): GcgnSalsPicInfoResponse {
        val gcgnSalsPicInfo = gcgnSalsPicInfoCustomRepository.findDomainById(gcgnSalsPicInfoId)
            ?: throw NoSuchElementException("GcgnSalsPicInfo not found with id: $gcgnSalsPicInfoId")

        gcgnSalsPicInfo.update(command, adminId)

        val updatedGcgnSalsPicInfo = gcgnSalsPicInfoRepository.save(gcgnSalsPicInfo)
        val response = gcgnSalsPicInfoMapper.toResponse(updatedGcgnSalsPicInfo)
        val empNm = response.empUserId.let { baseServiceClient.getUser(it)?.userNm }
        return response.copy(empNm = empNm)
    }

    override suspend fun deleteGcgnSalsPicInfo(gcgnSalsPicInfoId: Long) {
        gcgnSalsPicInfoRepository.deleteById(gcgnSalsPicInfoId)
    }
}
