package com.idrsys.ailis.tst.application.dto.inner

import java.time.LocalDateTime

data class SysCodeResponse(
  val cd: String,
  val cdNm: String,
  val cdDesc: String?,
  val cateCd: String?,
  val depth: Int,
  val upCd: String?,
  val sortOrder: Int,
  val etc1: String?,
  val etc2: String?,
  val etc3: String?,
  val remark: String?,
  val useYn: Boolean,
  val creator: String,
  val createDtime: LocalDateTime,
  val updater: String,
  val updateDtime: LocalDateTime
)