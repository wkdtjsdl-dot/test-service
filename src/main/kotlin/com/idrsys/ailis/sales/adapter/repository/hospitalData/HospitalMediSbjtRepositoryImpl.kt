package com.idrsys.ailis.sales.adapter.repository.hospitalData

import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalMediSbjtRepository
import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HospitalMediSbjtDataImpl : CoroutineCrudRepository<HospitalMediSbjt, Long>

@Repository
class HospitalMediSbjtRepositoryImpl(
    private val hospitalMediSbjtRepository: HospitalMediSbjtDataImpl,
) : HospitalMediSbjtRepository {

    override suspend fun save(hospitalMediSbjt: HospitalMediSbjt): HospitalMediSbjt {
        return hospitalMediSbjtRepository.save(hospitalMediSbjt)
    }

    override suspend fun findById(hospMediSbjtId: Long): HospitalMediSbjt? {
        return hospitalMediSbjtRepository.findById(hospMediSbjtId)
    }

    override suspend fun existsById(hospMediSbjtId: Long): Boolean {
        return hospitalMediSbjtRepository.existsById(hospMediSbjtId)
    }
}
