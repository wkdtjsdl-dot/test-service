package com.idrsys.ailis.tst.domain.model

import com.idrsys.ailis.tst.domain.command.TestReportCreateCommand
import com.idrsys.ailis.tst.domain.command.TestReportUpdateCommand
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 검사결과 보고서 Domain Model
 *
 * 테이블: tst_scm.tbs_tst_report
 */
@Table("tst_scm.tbs_tst_report")
class TestReport(
    tstReportId: String? = null,
    tstReqDt: LocalDate,
    tstReqNo: Long,
    tstCd: String,
    rstShort: String? = null,
    rstTxt: String? = null,
    rstUrl: String? = null,
    atchGrupId: String? = null,
    deliveryYn: Boolean = false,
    deliveryCd: String? = null,
    deliveryDtime: LocalDateTime? = null,
    deliverer: String? = null,
    limsRcvDtime: LocalDateTime? = null,
    memo: String? = null,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
    transmissionId: String? = null,
) : Persistable<String> {

    @Id
    @Column("tst_report_id")
    val tstReportId: String? = tstReportId

    @Column("tst_req_dt")
    var tstReqDt: LocalDate = tstReqDt
        private set

    @Column("tst_req_no")
    var tstReqNo: Long = tstReqNo
        private set

    @Column("tst_cd")
    var tstCd: String = tstCd
        private set

    @Column("rst_short")
    var rstShort: String? = rstShort
        private set

    @Column("rst_txt")
    var rstTxt: String? = rstTxt
        private set

    @Column("rst_url")
    var rstUrl: String? = rstUrl
        private set

    @Column("atch_grup_id")
    var atchGrupId: String? = atchGrupId
        private set

    @Column("delivery_yn")
    var deliveryYn: Boolean = deliveryYn
        private set

    @Column("delivery_cd")
    var deliveryCd: String? = deliveryCd
        private set

    @Column("delivery_dtime")
    var deliveryDtime: LocalDateTime? = deliveryDtime
        private set

    @Column("deliverer")
    var deliverer: String? = deliverer
        private set

    @Column("lims_rcv_dtime")
    var limsRcvDtime: LocalDateTime? = limsRcvDtime
        private set

    @Column("memo")
    var memo: String? = memo
        private set

    @Column("creator")
    var creator: String = creator
        private set

    @Column("create_dtime")
    var createDtime: LocalDateTime = createDtime
        private set

    @Column("updater")
    var updater: String = updater
        private set

    @Column("update_dtime")
    var updateDtime: LocalDateTime = updateDtime
        private set

    @Column("transmission_id")
    var transmissionId: String? = transmissionId
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = tstReportId

    override fun isNew(): Boolean = _isNew

    /**
     * 보고서 정보 업데이트
     */
    fun update(
        command: TestReportUpdateCommand,
        updater: String,
        updateDtime: LocalDateTime
    ) {
        this.atchGrupId = command.atchGrupId
        this.rstShort = command.rstShort
        this.rstTxt = command.rstTxt
        this.rstUrl = command.rstUrl
        this.memo = command.memo

        // deliveryYn이 true일 때 초기화 로직 추가
        if (command.deliveryYn == true) {
            this.deliveryYn = false
            this.deliveryCd = null
            this.deliveryDtime = null
            this.deliverer = null
        }

        this.updater = updater
        this.updateDtime = updateDtime
    }

    /**
     * 보고서 배포 처리
     */
    fun deliver(
        deliveryCd: String,
        deliverer: String,
        deliveryDtime: LocalDateTime,
        updater: String,
        updateDtime: LocalDateTime
    ) {
        this.deliveryYn = true
        this.deliveryCd = deliveryCd
        this.deliverer = deliverer
        this.deliveryDtime = deliveryDtime
        this.updater = updater
        this.updateDtime = updateDtime
    }

    companion object {
        /**
         * 새 보고서 생성
         */
        fun create(
            command: TestReportCreateCommand,
            creator: String,
            now: LocalDateTime
        ): TestReport {
            return TestReport(
                tstReportId = command.tstReportId,
                tstReqDt = command.tstReqDt,
                tstReqNo = command.tstReqNo,
                tstCd = command.tstCd,
                rstShort = command.rstShort,
                rstTxt = command.rstTxt,
                rstUrl = command.rstUrl,
                atchGrupId = command.atchGrupId,
                deliveryYn = false,
                deliveryCd = null,
                deliveryDtime = null,
                deliverer = null,
                limsRcvDtime = command.limsRcvDtime,
                memo = command.memo,
                creator = creator,
                createDtime = now,
                updater = creator,
                updateDtime = now,
                transmissionId = null
            ).apply { setAsNew() }
        }
    }
}