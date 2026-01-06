package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.response.UnsettledDemandSummary
import com.idrsys.ailis.sales.application.required.repository.billing.DemandRepository
import com.idrsys.ailis.sales.domain.model.Demand
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class DemandRepositoryImpl(
    private val demandDataRepository: DemandDataRepository,
    private val demandCustomRepository: DemandCustomRepositoryImpl
) : DemandRepository {

    // Basic CRUD operations (delegated to Spring Data R2DBC)
    override suspend fun save(demand: Demand): Demand {
        return demandDataRepository.save(demand)
    }

    override suspend fun findById(id: String): Demand? {
        return demandDataRepository.findById(id)
    }

    override suspend fun delete(demand: Demand) {
        demandDataRepository.delete(demand)
    }

    override suspend fun existsByCustCdAndDemandStartDtAndDemandStndDt(
        custCd: String,
        demandStartDt: LocalDate,
        demandStndDt: LocalDate
    ): Boolean {
        return demandDataRepository.existsByCustCdAndDemandStartDtAndDemandStndDt(
            custCd,
            demandStartDt,
            demandStndDt
        )
    }

    // Custom query operations (delegated to jOOQ implementation)
    override fun findDemands(searchParam: DemandSearchParam, pageable: Pageable): Flow<Demand> {
        return demandCustomRepository.findDemands(searchParam, pageable)
    }

    override suspend fun countDemands(searchParam: DemandSearchParam): Long {
        return demandCustomRepository.countDemands(searchParam)
    }

    override suspend fun findDemandById(demandId: String): Demand? {
        return demandCustomRepository.findDemandById(demandId)
    }

    override fun findUnsettledDemandSummary(
        searchParam: DemandSearchParam,
        pageable: Pageable
    ): Flow<UnsettledDemandSummary> {
        return demandCustomRepository.findUnsettledDemandSummary(searchParam, pageable)
    }

    override suspend fun countUnsettledDemandSummary(searchParam: DemandSearchParam): Long {
        return demandCustomRepository.countUnsettledDemandSummary(searchParam)
    }
}