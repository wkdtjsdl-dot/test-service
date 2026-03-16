package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.RequestDocumentRegisterRequest
import com.idrsys.ailis.tst.application.dto.RequestDocumentResponse
import com.idrsys.ailis.tst.domain.model.RequestDocument
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface RequestDocumentMapper {
    fun toResponse(requestDocument: RequestDocument): RequestDocumentResponse
    
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "createDtime", ignore = true)
    @Mapping(target = "updater", ignore = true)
    @Mapping(target = "updateDtime", ignore = true)
    fun toDomain(request: RequestDocumentRegisterRequest): RequestDocument
}
