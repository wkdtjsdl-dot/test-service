package com.idrsys.ailis.sales.adapter.repository.hospitalData

import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalMstRepository
import com.idrsys.ailis.sales.domain.model.HospitalMst
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HospitalMstDataImpl : CoroutineCrudRepository<HospitalMst, String>

@Repository
class HospitalMstRepositoryImpl(
    private val hospitalMstRepository: HospitalMstDataImpl
) : HospitalMstRepository {

    override suspend fun save(hospitalMst: HospitalMst): HospitalMst {
        return hospitalMstRepository.save(hospitalMst)
    }

    override suspend fun findById(careInstId: String): HospitalMst? {
        return hospitalMstRepository.findById(careInstId)
    }

    override suspend fun existsById(careInstId: String): Boolean {
        return hospitalMstRepository.existsById(careInstId)
    }

    override suspend fun deleteAll() {
        hospitalMstRepository.deleteAll()
    }
}
