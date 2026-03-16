package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.domain.model.TestReference
import com.idrsys.ailis.tst.domain.model.TestReferenceGroup
import com.idrsys.ailis.tst.domain.model.TestReferenceGroupItem
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface TestReferenceMapper {

    // --- TestReference ---
    fun toDomain(request: TestReferenceRegisterRequest): TestReference
    fun toResponse(domain: TestReference): TestReferenceResponse

    // --- TestReferenceGroup ---
    fun toDomain(request: TestReferenceGroupRegisterRequest): TestReferenceGroup
    fun toResponse(domain: TestReferenceGroup): TestReferenceGroupResponse

    // --- TestReferenceGroupItem ---
    fun toDomain(request: TestReferenceGroupItemRegisterRequest): TestReferenceGroupItem
    fun toResponse(domain: TestReferenceGroupItem): TestReferenceGroupItemResponse
}
