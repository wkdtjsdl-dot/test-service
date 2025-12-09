package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.StandardChargeRegisterRequest
import com.idrsys.ailis.tst.application.dto.StandardChargeUpdateRequest
import com.idrsys.ailis.tst.application.dto.TestItemEssentialDocRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestItemEssentialDocUpdateRequest
import com.idrsys.ailis.tst.application.dto.TestItemGeneRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestItemRefItemRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestItemRefItemUpdateRequest
import com.idrsys.ailis.tst.application.dto.TestItemRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestItemSpecimenRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestItemUpdateRequest
import com.idrsys.ailis.tst.domain.command.StandardChargeCreateCommand
import com.idrsys.ailis.tst.domain.command.StandardChargeUpdateCommand
import com.idrsys.ailis.tst.domain.command.TestItemCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemEssentialDocCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemEssentialDocUpdateCommand
import com.idrsys.ailis.tst.domain.command.TestItemGeneCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemRefItemCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemRefItemUpdateCommand
import com.idrsys.ailis.tst.domain.command.TestItemSpecimenCreateCommand
import com.idrsys.ailis.tst.domain.command.TestItemUpdateCommand
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface TestItemCommandMapper {

    fun toCreateCommand(request: TestItemRegisterRequest): TestItemCreateCommand

    fun toUpdateCommand(request: TestItemUpdateRequest): TestItemUpdateCommand

    fun toCreateCommand(request: StandardChargeRegisterRequest): StandardChargeCreateCommand

    fun toUpdateCommand(request: StandardChargeUpdateRequest): StandardChargeUpdateCommand

    fun toCreateCommand(request: TestItemSpecimenRegisterRequest): TestItemSpecimenCreateCommand

    fun toCreateCommand(request: TestItemRefItemRegisterRequest): TestItemRefItemCreateCommand

    fun toUpdateCommand(request: TestItemRefItemUpdateRequest): TestItemRefItemUpdateCommand

    fun toCreateCommand(request: TestItemGeneRegisterRequest): TestItemGeneCreateCommand

    fun toCreateCommand(request: TestItemEssentialDocRegisterRequest): TestItemEssentialDocCreateCommand

    fun toUpdateCommand(request: TestItemEssentialDocUpdateRequest): TestItemEssentialDocUpdateCommand
}
