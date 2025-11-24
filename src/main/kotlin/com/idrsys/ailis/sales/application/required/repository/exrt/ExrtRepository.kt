package com.idrsys.ailis.sales.application.required.repository.exrt

import com.idrsys.ailis.sales.domain.model.Exrt

interface ExrtRepository {
    suspend fun save(exrt: Exrt): Exrt
    suspend fun findById(id: Long): Exrt?
}
