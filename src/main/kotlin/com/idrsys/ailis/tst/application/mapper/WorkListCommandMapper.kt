package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.WorkListItemRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListItemUpdateRequest
import com.idrsys.ailis.tst.application.dto.WorkListRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListUpdateRequest
import com.idrsys.ailis.tst.domain.command.WorkListCreateCommand
import com.idrsys.ailis.tst.domain.command.WorkListItemCreateCommand
import com.idrsys.ailis.tst.domain.command.WorkListItemUpdateCommand
import com.idrsys.ailis.tst.domain.command.WorkListUpdateCommand
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface WorkListCommandMapper {
    fun toCreateCommand(request: WorkListRegisterRequest): WorkListCreateCommand
    fun toUpdateCommand(request: WorkListUpdateRequest): WorkListUpdateCommand
    fun toCreateItemCommand(request: WorkListItemRegisterRequest): WorkListItemCreateCommand
    fun toUpdateItemCommand(request: WorkListItemUpdateRequest): WorkListItemUpdateCommand
}
