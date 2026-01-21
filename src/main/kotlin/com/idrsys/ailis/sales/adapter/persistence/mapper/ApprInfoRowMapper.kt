package com.idrsys.ailis.sales.adapter.persistence.mapper

import com.idrsys.ailis.sales.domain.model.ApprInfo
import io.r2dbc.spi.Row
import java.time.LocalDateTime

object ApprInfoRowMapper {
    fun mapRow(row: Row): ApprInfo {
        return ApprInfo(
            apprInfoId = row.get("appr_info_id", String::class.java)!!,
            apprInfoNo = row.get("appr_info_no", Long::class.java)!!,
            apprSeq = row.get("appr_seq", Integer::class.java)!!.toInt(),
            apprDocTypeCd = row.get("appr_doc_type_cd", String::class.java)!!,
            apprPersonEmpNo = row.get("appr_person_emp_no", String::class.java)!!,
            apprStatCd = row.get("appr_stat_cd", String::class.java)!!,
            apprCmplDtime = row.get("appr_cmpl_dtime", LocalDateTime::class.java),
            apprMemo = row.get("appr_memo", String::class.java),
            creator = row.get("creator", String::class.java)!!,
            createDtime = row.get("create_dtime", LocalDateTime::class.java)!!,
            updater = row.get("updater", String::class.java)!!,
            updateDtime = row.get("update_dtime", LocalDateTime::class.java)!!,
        )
    }
}
