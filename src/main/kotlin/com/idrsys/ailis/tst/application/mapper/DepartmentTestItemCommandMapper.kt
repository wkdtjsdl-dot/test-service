package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.DepartmentGroupRegisterRequest
import com.idrsys.ailis.tst.application.dto.DepartmentGroupUpdateRequest
import com.idrsys.ailis.tst.application.dto.DepartmentTestItemRegisterRequest
import com.idrsys.ailis.tst.application.dto.DepartmentTestItemUpdateRequest
import com.idrsys.ailis.tst.domain.command.DepartmentGroupCreateCommand
import com.idrsys.ailis.tst.domain.command.DepartmentGroupUpdateCommand
import com.idrsys.ailis.tst.domain.command.DepartmentTestItemCreateCommand
import com.idrsys.ailis.tst.domain.command.DepartmentTestItemUpdateCommand
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface DepartmentTestItemCommandMapper {
    
    fun toCreateCommand(request: DepartmentGroupRegisterRequest): DepartmentGroupCreateCommand
    
    fun toUpdateCommand(request: DepartmentGroupUpdateRequest): DepartmentGroupUpdateCommand
    
    fun toCreateCommand(request: DepartmentTestItemRegisterRequest): DepartmentTestItemCreateCommand
    
    fun toUpdateCommand(request: DepartmentTestItemUpdateRequest): DepartmentTestItemUpdateCommand
}
