package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.TestCategoryRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestCategoryUpdateRequest
import com.idrsys.ailis.tst.domain.command.TestCategoryCreateCommand
import com.idrsys.ailis.tst.domain.command.TestCategoryUpdateCommand
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface TestCategoryCommandMapper {
    fun toCreateCommand(request: TestCategoryRegisterRequest): TestCategoryCreateCommand
    fun toUpdateCommand(request: TestCategoryUpdateRequest): TestCategoryUpdateCommand
}
