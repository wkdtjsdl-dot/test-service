package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.domain.model.StandardCharge
import com.idrsys.ailis.tst.domain.model.TestItem
import com.idrsys.ailis.tst.domain.model.TestItemSpecimen
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface TestItemMapper {

    // --- TestItem ---
    fun toDomain(request: TestItemRegisterRequest): TestItem
    fun toResponse(domain: TestItem): TestItemResponse

    // --- StandardCharge ---
    fun toDomain(request: StandardChargeRegisterRequest): StandardCharge
    fun toResponse(domain: StandardCharge): StandardChargeResponse

    // --- TestItemSpecimen ---
    fun toDomain(request: TestItemSpecimenRegisterRequest): TestItemSpecimen
    fun toResponse(domain: TestItemSpecimen): TestItemSpecimenResponse
}
