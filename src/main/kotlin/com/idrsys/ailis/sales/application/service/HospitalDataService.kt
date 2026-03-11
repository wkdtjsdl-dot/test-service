package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.hospitalData.HospitalDataSearchParam
import com.idrsys.ailis.sales.application.dto.response.HospitalMstResponse
import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalDeviceCustomRepository
import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalMediSbjtCustomRepository
import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalMstCustomRepository
import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalMstRepository
import com.idrsys.ailis.sales.application.usecase.hospitalData.HospitalDataUseCase
import com.idrsys.ailis.sales.domain.model.HospitalDevice
import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt
import com.idrsys.ailis.sales.domain.model.HospitalMst
import com.idrsys.ailis.sales.shared.mapper.HospitalDataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class HospitalDataService(
    private val hospitalMstRepository: HospitalMstRepository,
    private val hospitalMstCustRepository : HospitalMstCustomRepository,
    private val hospitalDeviceCustomRepository: HospitalDeviceCustomRepository,
    private val hospitalMediSbjtCustomRepository: HospitalMediSbjtCustomRepository,
    private val hospitalDataMapper: HospitalDataMapper,
) : HospitalDataUseCase {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override suspend fun getHospitalMstListPage(searchParam: HospitalDataSearchParam, pageable: Pageable): Page<HospitalMstResponse> {
        val total = hospitalMstCustRepository.countHospitalMstList(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val hospitalMstList = hospitalMstCustRepository.findHospitalMstList(searchParam, pageable).map { domain ->
            hospitalDataMapper.toResponse(domain)
        }.toList()

        return PageImpl(hospitalMstList, pageable, total)
    }

    override suspend fun getHospitalMstDetail(careInstId: String): HospitalMst {
        return hospitalMstRepository.findById(careInstId)
            ?: throw IllegalArgumentException("존재하지 않는 ID입니다: $careInstId")
    }

    override suspend fun getHospitalDevice(careInstId: String): Flow<HospitalDevice> {
        if (!hospitalMstRepository.existsById(careInstId)) {
            throw IllegalArgumentException("존재하지 않는 ID입니다: $careInstId")
        }
        return hospitalDeviceCustomRepository.getHospitalDevice(careInstId)
    }

    override suspend fun getHospitalMediSbjt(careInstId: String): Flow<HospitalMediSbjt> {
        if (!hospitalMstRepository.existsById(careInstId)) {
            throw IllegalArgumentException("존재하지 않는 ID입니다: $careInstId")
        }
        return hospitalMediSbjtCustomRepository.getHospitalMediSbjt(careInstId)
    }

}
