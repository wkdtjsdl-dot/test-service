package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.RequestDocumentRegisterRequest
import com.idrsys.ailis.tst.application.dto.RequestDocumentUpdateRequest
import com.idrsys.ailis.tst.domain.command.RequestDocumentCreateCommand
import com.idrsys.ailis.tst.domain.command.RequestDocumentUpdateCommand
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface RequestDocumentCommandMapper {
    
    fun toCreateCommand(request: RequestDocumentRegisterRequest): RequestDocumentCreateCommand
    
    fun toUpdateCommand(request: RequestDocumentUpdateRequest): RequestDocumentUpdateCommand
}
