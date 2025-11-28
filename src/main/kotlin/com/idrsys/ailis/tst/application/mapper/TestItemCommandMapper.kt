package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.StandardChargeRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestItemRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestItemSpecimenRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestItemUpdateRequest
import com.idrsys.ailis.tst.domain.command.StandardChargeCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemSpecimenCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemUpdateCommand
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface TestItemCommandMapper {
    
    fun toCreateCommand(request: TestItemRegisterRequest): TestItemCreateCommand
    
    fun toUpdateCommand(request: TestItemUpdateRequest): TestItemUpdateCommand
    
    fun toCreateCommand(request: StandardChargeRegisterRequest): StandardChargeCreateCommand
    
    fun toCreateCommand(request: TestItemSpecimenRegisterRequest): TestItemSpecimenCreateCommand
}
