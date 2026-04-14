package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.WorkListItemResponse
import com.idrsys.ailis.tst.application.dto.WorkListResponse
import com.idrsys.ailis.tst.domain.model.WorkList
import com.idrsys.ailis.tst.domain.model.WorkListItem
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface WorkListMapper {
    fun toResponse(domain: WorkList): WorkListResponse
    fun toResponse(domain: WorkListItem): WorkListItemResponse
}
