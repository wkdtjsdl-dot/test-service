package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoCommand
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.GcgnSalsPicInfoResponse
import com.idrsys.ailis.sales.application.required.repository.gcgnSalsPicInfo.GcgnSalsPicInfoCustomRepository
import com.idrsys.ailis.sales.application.required.repository.gcgnSalsPicInfo.GcgnSalsPicInfoRepository
import com.idrsys.ailis.sales.application.usecase.gcgnSalsPicInfo.GcgnSalsPicInfoUseCase
import com.idrsys.ailis.sales.adapter.external.BaseServiceClient
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalaPicInfoAutoSearchParam
import com.idrsys.ailis.sales.application.dto.response.GcgnSalsPicInfoAutoResponse
import com.idrsys.ailis.sales.shared.mapper.GcgnSalsPicInfoMapper
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
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

        val serializablePageable = if (pageable.isUnpaged) {
            if (total == 0L) PageRequest.of(0, 1)
            else PageRequest.of(0, total.toInt())
        } else {
            pageable
        }

        if (total == 0L) return PageImpl(emptyList(), serializablePageable, 0)

        val gcgnSalsPicInfoDtos = gcgnSalsPicInfoCustomRepository.findGcgnSalsPicInfos(searchParam, pageable).toList()

        // Fetch user names in batch to avoid N+1 problem
        val empUserIds = gcgnSalsPicInfoDtos.map { it.empUserId }.distinct()
        val userNameById = if (empUserIds.isNotEmpty()) {
            baseServiceClient.getUsersByIds(empUserIds)
                ?.associate { it.userId to it.userNm }
                ?: emptyMap()
        } else {
            emptyMap()
        }

        val gcgnSalsPicInfos = gcgnSalsPicInfoDtos.map { dto ->
            val response = gcgnSalsPicInfoMapper.toResponse(dto)
            response.copy(empUserNm = userNameById[dto.empUserId])
        }

        return PageImpl(gcgnSalsPicInfos, serializablePageable, total)
    }

    override suspend fun getGcgnSalsPicInfoDetail(gcgnSalsPicInfoId: Long): GcgnSalsPicInfoResponse {
        val dto = gcgnSalsPicInfoCustomRepository.findGcgnSalsPicInfoById(gcgnSalsPicInfoId)
            ?: throw NoSuchElementException("GcgnSalsPicInfo not found with id: $gcgnSalsPicInfoId")

        val response = gcgnSalsPicInfoMapper.toResponse(dto)
        val empUserNm = dto.empUserId.let { baseServiceClient.getUser(it)?.userNm }
        return response.copy(empUserNm = empUserNm)
    }

    override suspend fun createGcgnSalsPicInfo(command: GcgnSalsPicInfoCommand, adminId: String): GcgnSalsPicInfoResponse {
        val user = baseServiceClient.getUser(command.empUserId)
            ?: throw IllegalArgumentException("User not found with id: ${command.empUserId}")
        val now = LocalDateTime.now()
        val gcgnSalsPicInfo = gcgnSalsPicInfoMapper.toDomain(command, adminId, now).apply { setAsNew() }
        val savedGcgnSalsPicInfo = gcgnSalsPicInfoRepository.save(gcgnSalsPicInfo)
        val response = gcgnSalsPicInfoMapper.toResponse(savedGcgnSalsPicInfo)
        return response.copy(empUserNm = user.userNm)
    }

    override suspend fun updateGcgnSalsPicInfo(gcgnSalsPicInfoId: Long, command: GcgnSalsPicInfoCommand, adminId: String): GcgnSalsPicInfoResponse {
        val gcgnSalsPicInfo = gcgnSalsPicInfoCustomRepository.findDomainById(gcgnSalsPicInfoId)
            ?: throw NoSuchElementException("GcgnSalsPicInfo not found with id: $gcgnSalsPicInfoId")

        gcgnSalsPicInfo.update(command, adminId)

        val updatedGcgnSalsPicInfo = gcgnSalsPicInfoRepository.save(gcgnSalsPicInfo)
        val response = gcgnSalsPicInfoMapper.toResponse(updatedGcgnSalsPicInfo)
        val empUserNm = response.empUserId.let { baseServiceClient.getUser(it)?.userNm }
        return response.copy(empUserNm = empUserNm)
    }

    override suspend fun deleteGcgnSalsPicInfo(gcgnSalsPicInfoId: Long) {
        gcgnSalsPicInfoRepository.deleteById(gcgnSalsPicInfoId)
    }

    override suspend fun getSalsPicAutoCompleteList(searchParam: GcgnSalaPicInfoAutoSearchParam): List<GcgnSalsPicInfoAutoResponse> {

        val users = baseServiceClient.getUsers() ?: emptyList()
        val userNameById = users.associate { it.userId to it.userNm }

        val keyword = searchParam.empUserIdNm?.trim()?.takeIf { it.isNotEmpty() }

        val nameMatchedIds: List<String> =
            if (keyword != null)
                users.asSequence()
                    .filter { it.userNm.contains(keyword, ignoreCase = true) }
                    .map { it.userId }
                    .toList()
            else emptyList()

        val ids: List<String> =
            gcgnSalsPicInfoCustomRepository
                .findEmpUserIdsForAutoComplete(searchParam, includeUserIds = nameMatchedIds)
                .toList()

        return ids.map { userId ->
            GcgnSalsPicInfoAutoResponse(
                empUserId = userId,
                empUserNm = userNameById[userId] ?: ""
            )
        }
    }
}
