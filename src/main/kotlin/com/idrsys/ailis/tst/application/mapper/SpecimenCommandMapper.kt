package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.SpecimenRegisterRequest
import com.idrsys.ailis.tst.application.dto.SpecimenUpdateRequest
import com.idrsys.ailis.tst.domain.command.SpecimenCreateCommand
import com.idrsys.ailis.tst.domain.command.SpecimenUpdateCommand
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface SpecimenCommandMapper {
    
    fun toCreateCommand(request: SpecimenRegisterRequest): SpecimenCreateCommand
    
    fun toUpdateCommand(request: SpecimenUpdateRequest): SpecimenUpdateCommand
}
