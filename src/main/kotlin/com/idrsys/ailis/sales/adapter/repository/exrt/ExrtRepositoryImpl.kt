package com.idrsys.ailis.sales.adapter.repository.exrt

import com.idrsys.ailis.sales.application.required.repository.exrt.ExrtRepository
import com.idrsys.ailis.sales.domain.model.Exrt
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ExrtDataRepository : CoroutineCrudRepository<Exrt, Long> {
    suspend fun existsByStndDtAndCrcyCd(stndDt: LocalDate, crcyCd: String): Boolean
}

@Repository
class ExrtRepositoryImpl(
    private val dataRepository: ExrtDataRepository
) : ExrtRepository {

    override suspend fun save(exrt: Exrt): Exrt {
        return dataRepository.save(exrt)
    }

    override suspend fun findById(id: Long): Exrt? {
        return dataRepository.findById(id)
    }

    override suspend fun deleteById(id: Long) {
        dataRepository.deleteById(id)
    }

    override suspend fun existsByStndDtAndCrcyCd(stndDt: LocalDate, crcyCd: String): Boolean {
        return dataRepository.existsByStndDtAndCrcyCd(stndDt, crcyCd)
    }
}
