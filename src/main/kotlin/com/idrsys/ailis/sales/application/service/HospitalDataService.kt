package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.hospitalData.HospitalDataSearchParam
import com.idrsys.ailis.sales.application.dto.response.DeviceInfo
import com.idrsys.ailis.sales.application.dto.response.HospitalInfo
import com.idrsys.ailis.sales.application.dto.response.HospitalMstResponse
import com.idrsys.ailis.sales.application.dto.response.MediSbjtInfo
import com.idrsys.ailis.sales.application.required.port.HospitalDataApiPort
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
import com.idrsys.ailis.sales.application.required.repository.hospitalData.*
import com.idrsys.ailis.sales.application.usecase.hospitalData.HospitalDataUseCase
import com.idrsys.ailis.sales.domain.model.HospitalDevice
import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt
import com.idrsys.ailis.sales.domain.model.HospitalMst
import com.idrsys.ailis.sales.shared.mapper.HospitalDataMapper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

@Service
class HospitalDataService(
    private val hospitalMstRepository: HospitalMstRepository,
    private val hospitalMstCustRepository : HospitalMstCustomRepository,
    private val hospitalDeviceCustomRepository: HospitalDeviceCustomRepository,
    private val hospitalMediSbjtCustomRepository: HospitalMediSbjtCustomRepository,
    private val transactionalOperator: TransactionalOperator,
    private val hospitalDataApiPort: HospitalDataApiPort,
    private val hospitalMstCustomRepository: HospitalMstCustomRepository,
    private val hospitalMediSbjtRepository: HospitalMediSbjtRepository,
    private val hospitalDeviceRepository: HospitalDeviceRepository,
    private val custCustomRepository: CustCustomRepository,
    private val hospitalDataMapper: HospitalDataMapper,
) : HospitalDataUseCase {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val BATCH_USER = "batch"

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

    @OptIn(DelicateCoroutinesApi::class)
    override fun executeHospitalDataSynchronization(): Boolean {
        log.info("Hospital data synchronization requested.")
        GlobalScope.launch {
            log.info("Hospital data synchronization started.")
            val hasFailed = AtomicBoolean(false)

            custCustomRepository.findAllWithCareInstId()
                .collect { custCareInstId ->
                    try {
                        log.info("synchronization careInstId: ${custCareInstId.careInstId} started.")
                        val hospitalMst = hospitalMstCustomRepository.findByCareInstId(custCareInstId.careInstId)
                        if (hospitalMst == null) {
                            log.warn("HospitalMst not found for careInstId: ${custCareInstId.careInstId}")
                            return@collect
                        }

                        val ykiho = hospitalMst.encpCareInstNo
                        val hospNm = hospitalMst.careInstNm
                        val sgguNm = hospitalMst.sgguNm
                        val emd = hospitalMst.emd

                        if (sgguNm.isNullOrBlank() || emd.isNullOrBlank()) {
                            log.warn("sgguNm or emd is blank for hospNm: {}. Skipping API call.", hospNm)
                            return@collect
                        }

                        hospitalDataApiPort.fetchHospitalDataByAddrAndName(hospNm, sgguNm, emd)
                            .collect { hospitalInfo ->
                                transactionalOperator.executeAndAwait {
                                    upsertHospitalMst(hospitalInfo)

                                    val actualCareInstId = hospitalMst.careInstId

                                    // Sync subjects
                                    val apiSbjtCds = mutableListOf<String>()
                                    hospitalDataApiPort.fetchMediSbjtData(ykiho)
                                        .onEach { sbjtInfo ->
                                            apiSbjtCds.add(sbjtInfo.dgsbjtCd)
                                            upsertHospitalMediSbjt(actualCareInstId, sbjtInfo)
                                        }
                                        .collect()

                                    val sbjtCdsToDelete = hospitalMediSbjtCustomRepository.findAllMediSbjtCdsByCareInstId(actualCareInstId).toList() - apiSbjtCds.toSet()
                                    if (sbjtCdsToDelete.isNotEmpty()) {
                                        hospitalMediSbjtCustomRepository.deleteByCareInstIdAndMediSbjtCdNotIn(actualCareInstId, sbjtCdsToDelete)
                                    }

                                    // Sync devices
                                    val apiDeviceCds = mutableListOf<String>()
                                    hospitalDataApiPort.fetchDeviceData(ykiho)
                                        .onEach { deviceInfo ->
                                            apiDeviceCds.add(deviceInfo.oftCd)
                                            upsertHospitalDevice(actualCareInstId, deviceInfo)
                                        }
                                        .collect()

                                    val deviceCdsToDelete = hospitalDeviceCustomRepository.findAllDeviceCdsByCareInstId(actualCareInstId).toList() - apiDeviceCds.toSet()
                                    if (deviceCdsToDelete.isNotEmpty()) {
                                        hospitalDeviceCustomRepository.deleteByCareInstIdAndDeviceCdNotIn(actualCareInstId, deviceCdsToDelete)
                                    }
                                }
                            }
                        log.info("synchronization careInstId: ${custCareInstId.careInstId} end.")
                    } catch (e: Exception) {
                        log.error("Failed to sync hospital data for custMstId: ${custCareInstId.custMstId}", e)
                        hasFailed.set(true)
                    }
                }

            // --- New Closure Data Sync Logic ---
            try {
                log.info("Hospital closure data synchronization started.")
                val currentYearMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"))

                hospitalDataApiPort.fetchHospitalClosureData(currentYearMonth)
                    .collect { closureInfo ->
                        transactionalOperator.executeAndAwait {
                            val hospitalMst = hospitalMstCustomRepository.findByEncpCareInstNo(closureInfo.ykiho)
                            if (hospitalMst != null) {
                                val closureDate = closureInfo.cnclDd?.toString()?.let {
                                    LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyyMMdd"))
                                        .format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                                }
                                hospitalMst.apply {
                                    this.closeDt = closureDate
                                    this.updater = BATCH_USER
                                    this.updateDtime = LocalDateTime.now()
                                }
                                hospitalMstRepository.save(hospitalMst)
                                log.debug("Updated closure date for hospital: ${closureInfo.yadmNm} to $closureDate")
                            } else {
                                log.warn("HospitalMst not found for closure info ykiho: ${closureInfo.ykiho}")
                            }
                        }
                    }
                log.info("Hospital closure data synchronization finished.")
            } catch (e: Exception) {
                log.error("Failed to sync hospital closure data", e)
                hasFailed.set(true)
            }
        }
        return true
    }

    private suspend fun upsertHospitalMst(hospitalInfo: HospitalInfo) {
        val existing = hospitalMstCustomRepository.findByEncpCareInstNo(hospitalInfo.ykiho)
        val now = LocalDateTime.now()

        if (existing != null) {
            val updated = existing.apply {
                this.asrtCd = hospitalInfo.clCd
                this.encpCareInstNo = hospitalInfo.ykiho
                this.careInstNm = hospitalInfo.yadmNm
                this.addr = hospitalInfo.addr
                this.sidoCd = hospitalInfo.sidoCd
                this.sidoNm = hospitalInfo.sidoCdNm
                this.sgguCd = hospitalInfo.sgguCd
                this.sgguNm = hospitalInfo.sgguCdNm
                this.emd = hospitalInfo.emdongNm
                this.zipcd = hospitalInfo.postNo
                this.telno = hospitalInfo.telno
                this.hpUrl = hospitalInfo.hospUrl
                this.openDt = hospitalInfo.estbDd
                this.drCnt = hospitalInfo.drTotCnt
                this.mapCodnX = hospitalInfo.xPos
                this.mapCodnY = hospitalInfo.yPos
                this.updater = BATCH_USER
                this.updateDtime = LocalDateTime.now()
            }
            hospitalMstRepository.save(updated)
            log.debug("Updated hospital: ${hospitalInfo.yadmNm}")
        } else {
            val newHospital = HospitalMst(
                careInstId = UUID.randomUUID().toString(),
                careInstNm = hospitalInfo.yadmNm,
                encpCareInstNo = hospitalInfo.ykiho,
                addr = hospitalInfo.addr,
                telno = hospitalInfo.telno,
                hpUrl = hospitalInfo.hospUrl,
                sidoCd = hospitalInfo.sidoCd,
                sidoNm = hospitalInfo.sidoCdNm,
                sgguCd = hospitalInfo.sgguCd,
                sgguNm = hospitalInfo.sgguCdNm,
                zipcd = hospitalInfo.postNo,
                openDt = hospitalInfo.estbDd,
                drCnt = hospitalInfo.drTotCnt,
                mapCodnX = hospitalInfo.xPos,
                mapCodnY = hospitalInfo.yPos,
                useYn = true,
                creator = BATCH_USER,
                createDtime = now,
                updater = BATCH_USER,
                updateDtime = now,
                emd = hospitalInfo.emdongNm,
                asrtCd = hospitalInfo.clCd,
                closeDt = null,    // 문닫은 날 없음
                estbDivNm = null,  // 설립구분명이라는데 이런 값이 없음
                sickbedCnt = null, // 병상수 없음
                careInstNo = null  // 요양기관 번호인데 알수가 없음
            ).apply { setAsNew() }
            hospitalMstRepository.save(newHospital)
            log.debug("Insert hospital: ${hospitalInfo.yadmNm}")
        }
    }

    private suspend fun upsertHospitalMediSbjt(careInstId: String, sbjtInfo: MediSbjtInfo) {
        val existing = hospitalMediSbjtCustomRepository.findByCareInstIdAndDgsbjtCd(careInstId, sbjtInfo.dgsbjtCd)
        val now = LocalDateTime.now()

        if (existing != null) {
            val updated = existing.apply {
                this.mediSbjtCd = sbjtInfo.dgsbjtCd
                this.mediSbjtNm = sbjtInfo.dgsbjtCdNm
                this.mediSbjtMdspCnt = sbjtInfo.dgsbjtPrSdrCnt
                this.updater = BATCH_USER
                this.updateDtime = now
            }
            hospitalMediSbjtRepository.save(updated)
        } else {
            val newSubject = HospitalMediSbjt(
                careInstId = careInstId.take(50),
                mediSbjtCd = sbjtInfo.dgsbjtCd,
                mediSbjtNm = sbjtInfo.dgsbjtCdNm,
                mediSbjtMdspCnt = sbjtInfo.dgsbjtPrSdrCnt,
                useYn = true,
                creator = BATCH_USER,
                createDtime = now,
                updater = BATCH_USER,
                updateDtime = now,
                selcareDrCnt = null
            )
            hospitalMediSbjtRepository.save(newSubject)
        }
    }

    private suspend fun upsertHospitalDevice(careInstId: String, deviceInfo: DeviceInfo) {
        val existing = hospitalDeviceCustomRepository.findByCareInstIdAndOftCd(careInstId, deviceInfo.oftCd)
        val now = LocalDateTime.now()

        if (existing != null) {
            val updated = existing.apply {
                this.deviceCd = deviceInfo.oftCd
                this.deviceNm = deviceInfo.oftCdNm
                this.deviceCnt = deviceInfo.oftCnt
                this.updater = BATCH_USER
                this.updateDtime = now
            }
            hospitalDeviceRepository.save(updated)
        } else {
            val newDevice = HospitalDevice(
                careInstId = careInstId,
                deviceCd = deviceInfo.oftCd,
                deviceNm = deviceInfo.oftCdNm,
                deviceCnt = deviceInfo.oftCnt,
                useYn = true,
                creator = BATCH_USER,
                createDtime = now,
                updater = BATCH_USER,
                updateDtime = now
            )
            hospitalDeviceRepository.save(newDevice)
        }
    }

}