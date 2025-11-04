package com.idrsys.ailis.sales.adapter.repository.hospitalData

import com.idrsys.ailis.sales.adapter.persistence.mapper.toHospitalDevice
import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalDeviceCustomRepository
import com.idrsys.ailis.sales.domain.model.HospitalDevice
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_HOSP_DEVICE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class HospitalDeviceCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : HospitalDeviceCustomRepository {

    override fun getHospitalDevice(careInstId: String): Flow<HospitalDevice> {
        val query = dslContext
            .select(*SCS_HOSP_DEVICE.fields())
            .from(SCS_HOSP_DEVICE)
            .where(SCS_HOSP_DEVICE.CARE_INST_ID.eq(careInstId))

        return databaseClient.sql(query.sql)
            .bind(0, careInstId)
            .map { row, _ -> row.toHospitalDevice() }
            .all()
            .asFlow()
    }
}
