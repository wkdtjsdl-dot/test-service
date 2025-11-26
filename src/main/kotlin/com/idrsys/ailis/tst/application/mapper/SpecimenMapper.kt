package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.domain.model.Specimen
import com.idrsys.ailis.tst.domain.model.SpecimenContainer
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface SpecimenContainerMapper {
    fun toResponse(specimenContainer: SpecimenContainer): SpecimenContainerResponse
    
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "createDtime", ignore = true)
    @Mapping(target = "updater", ignore = true)
    @Mapping(target = "updateDetime", ignore = true)
    fun toDomain(request: SpecimenContainerRegisterRequest): SpecimenContainer
}

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface SpecimenMapper {
    fun toResponse(specimen: Specimen): SpecimenResponse
    
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "createDtime", ignore = true)
    @Mapping(target = "updater", ignore = true)
    @Mapping(target = "updateDetime", ignore = true)
    fun toDomain(request: SpecimenRegisterRequest): Specimen
}
