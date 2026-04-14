package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.WorkListItemRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListRegisterRequest
import com.idrsys.ailis.tst.domain.command.WorkListCreateCommand
import com.idrsys.ailis.tst.domain.command.WorkListItemCreateCommand
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface WorkListCommandMapper {
    fun toCreateCommand(request: WorkListRegisterRequest): WorkListCreateCommand
    fun toCreateItemCommand(request: WorkListItemRegisterRequest): WorkListItemCreateCommand
}
