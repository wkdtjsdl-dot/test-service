package com.idrsys.ailis.sales.application.required.sap

import com.idrsys.ailis.sales.application.dto.request.ifre010.SapIfRe010Row
import com.idrsys.ailis.sales.application.dto.response.sap.SapIfRe010Result

interface CollectionErpPort {
    fun sendCollection(rtype: String, row: SapIfRe010Row): SapIfRe010Result
}
