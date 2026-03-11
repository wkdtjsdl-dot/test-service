package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.TestCodeMappingQuery
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingCommand
import com.idrsys.ailis.sales.application.dto.response.TestCodeMappingResponse
import com.idrsys.ailis.sales.domain.model.CustTestCodeMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
import java.time.LocalDateTime

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface TestCodeMappingMapper {

    fun toResponse(dto: TestCodeMappingQuery): TestCodeMappingResponse

    fun toResponse(domain: CustTestCodeMapping): TestCodeMappingResponse

    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "updater", source = "creator")
    @Mapping(target = "createDtime", source = "now")
    @Mapping(target = "updateDtime", source = "now")
    fun toDomain(command: TestCodeMappingCommand, creator: String, now: LocalDateTime): CustTestCodeMapping
}