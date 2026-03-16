package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.domain.model.DepartmentGroup
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItem
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItemTest
import com.idrsys.ailis.tst.domain.model.DepartmentTestItem
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.ReportingPolicy
import java.time.LocalDateTime

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface DepartmentTestItemMapper {

    // --- DepartmentGroup ---
    fun toDomain(request: DepartmentGroupRegisterRequest): DepartmentGroup
    fun toResponse(domain: DepartmentGroup): DepartmentGroupResponse

    // --- DepartmentGroupItem ---
    @Mappings(
        Mapping(target = "deptGrpItmId", ignore = true),
        Mapping(target = "creator", source = "creator"),
        Mapping(target = "createDtime", source = "now"),
        Mapping(target = "updater", source = "creator"),
        Mapping(target = "updateDtime", source = "now"),
    )
    fun toDomain(request: DepartmentGroupItemRegisterRequest, creator: String, now: LocalDateTime): DepartmentGroupItem
    fun toResponse(domain: DepartmentGroupItem): DepartmentGroupItemResponse

    // --- DepartmentGroupItemTest ---
    @Mappings(
        Mapping(target = "deptGrpItmTstId", ignore = true),
        Mapping(target = "creator", source = "creator"),
        Mapping(target = "createDtime", source = "now"),
    )
    fun toDomain(request: DepartmentGroupItemTestRegisterRequest, creator: String, now: LocalDateTime): DepartmentGroupItemTest
    fun toResponse(domain: DepartmentGroupItemTest): DepartmentGroupItemTestResponse

    // --- DepartmentTestItem ---
    fun toDomain(request: DepartmentTestItemRegisterRequest): DepartmentTestItem
    fun toResponse(domain: DepartmentTestItem): DepartmentTestItemResponse
}
