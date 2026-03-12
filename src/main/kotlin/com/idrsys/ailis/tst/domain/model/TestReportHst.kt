package com.idrsys.ailis.tst.domain.model

import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 검사결과 보고서 이력 Domain Model
 *
 * 테이블: tst_scm.tbs_tst_report_hst
 */
@Table("tst_scm.tbs_tst_report_hst")
class TestReportHst(
  tstReportHstId: String? = null,
  hstCd: String,
  hstMemo: String,
  worker: String,
  workDtime: LocalDateTime,
  tstReqDt: LocalDate,
  tstReqNo: Long,
  tstCd: String,
  memo: String? = null,
  limsRcvDtime: LocalDateTime? = null,
  rstShort: String? = null,
  rstTxt: String? = null,
  atchGrupId: String? = null,
  rstUrl: String? = null,
  deliveryYn: Boolean? = false,
  deliveryCd: String? = null,
  deliveryDtime: LocalDateTime? = null,
  deliverer: String? = null,
  creator: String,
  createDtime: LocalDateTime,
  updater: String,
  updateDtime: LocalDateTime,
) : Persistable<String> {

  @Id
  @UuidGeneratedId(idFieldName = "tstReportHstId")
  @Column("tst_report_hst_id")
  val tstReportHstId: String? = tstReportHstId

  @Column("hst_cd")
  var hstCd: String = hstCd
    private set

  @Column("hst_memo")
  var hstMemo: String = hstMemo
    private set

  @Column("worker")
  var worker: String = worker
    private set

  @Column("work_dtime")
  var workDtime: LocalDateTime? = workDtime
    private set

  @Column("tst_req_dt")
  var tstReqDt: LocalDate? = tstReqDt
    private set

  @Column("tst_req_no")
  var tstReqNo: Long? = tstReqNo
    private set

  @Column("tst_cd")
  var tstCd: String? = tstCd
    private set

  @Column("memo")
  var memo: String? = memo
    private set

  @Column("lims_rcv_dtime")
  var limsRcvDtime: LocalDateTime? = limsRcvDtime
    private set

  @Column("rst_short")
  var rstShort: String? = rstShort
    private set

  @Column("rst_txt")
  var rstTxt: String? = rstTxt
    private set

  @Column("atch_grup_id")
  var atchGrupId: String? = atchGrupId
    private set

  @Column("rst_url")
  var rstUrl: String? = rstUrl
    private set

  @Column("delivery_yn")
  var deliveryYn: Boolean? = deliveryYn
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

  @Transient
  private var _isNew: Boolean = false

  fun setAsNew() {
    this._isNew = true
  }

  override fun getId(): String? = tstReportHstId

  override fun isNew(): Boolean = _isNew
}