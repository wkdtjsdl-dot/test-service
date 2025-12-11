package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.domain.model.StandardCharge
import com.idrsys.ailis.tst.domain.model.TestGene
import com.idrsys.ailis.tst.domain.model.TestItem
import com.idrsys.ailis.tst.domain.model.TestItemEssentialDoc
import com.idrsys.ailis.tst.domain.model.TestItemGene
import com.idrsys.ailis.tst.domain.model.TestItemHst
import com.idrsys.ailis.tst.domain.model.TestItemRefItem
import com.idrsys.ailis.tst.domain.model.TestItemSpecimen
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
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

    // --- TestItemRefItem ---
    fun toResponse(domain: TestItemRefItem): TestItemRefItemResponse

    // --- TestGene ---
    fun  toResponse(domain: TestGene): TestGeneResponse
    // --- TestItemGene ---
    fun toResponse(domain: TestItemGene): TestItemGeneResponse

    // --- TestItemEssentialDoc ---
    fun toResponse(domain: TestItemEssentialDoc): TestItemEssentialDocResponse

    // --- TestItemHst ---
    @Mappings(
        Mapping(target = "itemHstId", ignore = true),
        Mapping(target = "hstDesc", source = "updateReason")
    )
    fun toDomain(testItem: TestItem, updateReason: String = ""): TestItemHst
}
