package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.TestCategoryRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestCategoryResponse
import com.idrsys.ailis.tst.domain.model.TestCategory
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface TestCategoryMapper {
    fun toResponse(testCategory: TestCategory): TestCategoryResponse
    
    @Mapping(target = "tstCateId", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "createDtime", ignore = true)
    @Mapping(target = "updater", ignore = true)
    @Mapping(target = "updateDetime", ignore = true)
    fun toDomain(request: TestCategoryRegisterRequest): TestCategory
}
