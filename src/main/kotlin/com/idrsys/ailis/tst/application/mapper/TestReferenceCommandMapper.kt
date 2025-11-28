package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.TestReferenceGroupItemRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestReferenceGroupRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestReferenceRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestReferenceUpdateRequest
import com.idrsys.ailis.tst.domain.command.TestReferenceCreateCommand
import com.idrsys.ailis.tst.domain.command.TestReferenceGroupCreateCommand
import com.idrsys.ailis.tst.domain.command.TestReferenceGroupItemCreateCommand
import com.idrsys.ailis.tst.domain.command.TestReferenceUpdateCommand
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface TestReferenceCommandMapper {
    
    fun toCreateCommand(request: TestReferenceRegisterRequest): TestReferenceCreateCommand
    
    fun toUpdateCommand(request: TestReferenceUpdateRequest): TestReferenceUpdateCommand
    
    fun toCreateCommand(request: TestReferenceGroupRegisterRequest): TestReferenceGroupCreateCommand
    
    fun toCreateCommand(request: TestReferenceGroupItemRegisterRequest): TestReferenceGroupItemCreateCommand
}
