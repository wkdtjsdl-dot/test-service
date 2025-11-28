package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.SpecimenContainerRegisterRequest
import com.idrsys.ailis.tst.application.dto.SpecimenContainerUpdateRequest
import com.idrsys.ailis.tst.domain.command.SpecimenContainerCreateCommand
import com.idrsys.ailis.tst.domain.command.SpecimenContainerUpdateCommand
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface SpecimenContainerCommandMapper {
    
    fun toCreateCommand(request: SpecimenContainerRegisterRequest): SpecimenContainerCreateCommand
    
    fun toUpdateCommand(request: SpecimenContainerUpdateRequest): SpecimenContainerUpdateCommand
}
