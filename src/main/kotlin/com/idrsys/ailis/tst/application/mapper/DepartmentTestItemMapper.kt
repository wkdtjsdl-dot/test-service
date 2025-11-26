package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.domain.model.DepartmentGroup
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItem
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItemTest
import com.idrsys.ailis.tst.domain.model.DepartmentTestItem
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface DepartmentTestItemMapper {

    // --- DepartmentGroup ---
    fun toDomain(request: DepartmentGroupRegisterRequest): DepartmentGroup
    fun toResponse(domain: DepartmentGroup): DepartmentGroupResponse

    // --- DepartmentGroupItem ---
    fun toDomain(request: DepartmentGroupItemRegisterRequest): DepartmentGroupItem
    fun toResponse(domain: DepartmentGroupItem): DepartmentGroupItemResponse

    // --- DepartmentGroupItemTest ---
    fun toDomain(request: DepartmentGroupItemTestRegisterRequest): DepartmentGroupItemTest
    fun toResponse(domain: DepartmentGroupItemTest): DepartmentGroupItemTestResponse

    // --- DepartmentTestItem ---
    fun toDomain(request: DepartmentTestItemRegisterRequest): DepartmentTestItem
    fun toResponse(domain: DepartmentTestItem): DepartmentTestItemResponse
}
