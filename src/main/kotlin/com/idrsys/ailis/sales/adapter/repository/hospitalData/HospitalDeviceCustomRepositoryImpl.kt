package com.idrsys.ailis.sales.adapter.repository.hospitalData

import com.idrsys.ailis.sales.adapter.persistence.mapper.toHospitalDevice
import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalDeviceCustomRepository
import com.idrsys.ailis.sales.domain.model.HospitalDevice
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_HOSP_DEVICE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
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

    override suspend fun findByCareInstIdAndOftCd(careInstId: String, oftCd: String): HospitalDevice? {
        val query = dslContext.selectFrom(SCS_HOSP_DEVICE)
            .where(SCS_HOSP_DEVICE.CARE_INST_ID.eq(careInstId))
            .and(SCS_HOSP_DEVICE.DEVICE_CD.eq(oftCd))

        return databaseClient.sql(query.sql)
            .bind(0, careInstId)
            .bind(1, oftCd)
            .map { row, _ -> row.toHospitalDevice() }
            .first()
            .awaitSingleOrNull()
    }

    override suspend fun findAllDeviceCdsByCareInstId(careInstId: String): List<String> {
        val query = dslContext.select(SCS_HOSP_DEVICE.DEVICE_CD)
            .from(SCS_HOSP_DEVICE)
            .where(SCS_HOSP_DEVICE.CARE_INST_ID.eq(careInstId))

        return databaseClient.sql(query.sql)
            .bind(0, careInstId)
            .map { row, _ -> row.get(SCS_HOSP_DEVICE.DEVICE_CD.name, String::class.java) }
            .all()
            .filter { it != null }
            .map { it!! }
            .collectList()
            .awaitSingle()
    }

    override suspend fun deleteByCareInstIdAndDeviceCdNotIn(careInstId: String, deviceCds: List<String>): Int {
        val query = if (deviceCds.isEmpty()) {
            dslContext.deleteFrom(SCS_HOSP_DEVICE)
                .where(SCS_HOSP_DEVICE.CARE_INST_ID.eq(careInstId))
        } else {
            dslContext.deleteFrom(SCS_HOSP_DEVICE)
                .where(SCS_HOSP_DEVICE.CARE_INST_ID.eq(careInstId))
                .and(SCS_HOSP_DEVICE.DEVICE_CD.notIn(deviceCds))
        }

        return databaseClient.sql(query.sql)
            .bindValues(query.bindValues)
            .fetch()
            .rowsUpdated()
            .awaitSingleOrNull()?.toInt() ?: 0
    }
}
