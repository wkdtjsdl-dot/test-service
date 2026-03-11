package com.idrsys.ailis.sales.application.required.repository.reqrstifmethod

import com.idrsys.ailis.sales.domain.model.ReqRstIfMethod

interface ReqRstIfMethodRepository {
    suspend fun findById(id: String): ReqRstIfMethod?
    suspend fun save(reqRstIfMethod: ReqRstIfMethod): ReqRstIfMethod
}
