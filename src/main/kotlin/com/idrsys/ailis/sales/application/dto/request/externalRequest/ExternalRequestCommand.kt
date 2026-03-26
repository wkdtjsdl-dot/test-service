package com.idrsys.ailis.sales.application.dto.request.externalRequest

import java.time.LocalDate
import java.time.LocalDateTime

data class InnerRequestCommand(
    val id: String,
    val department: String? = null,
    val ward: String? = null,
    val physician: String? = null,
    val genomePrice: Long? = null,
    val labsPrice: Long? = null,
    val memo: String? = null,
    val createAt: LocalDateTime,
    val organization: InnerRequestOrganizationResponse,
    val patient: InnerRequestPatientResponse,
    val service: InnerRequestServiceResponse,
    val sample: InnerRequestSampleResponse,
    val extensions: List<InnerRequestExtensionResponse>? = null
)

data class InnerRequestSearchParam(
    val requestDataFrom: LocalDate,
    val requestDataTo: LocalDate,
    val sample: SampleSearch? = null,
    val service: ServiceSearch? = null,
    val organization: OrganizationSearch? = null,
    val patient: PatientSearch? = null,
)

data class SampleSearch(
    val serial: String? = null // 의료재단 검체 식별자
)

data class ServiceSearch(
    val serial: String? = null // 의료재단 검사 식별자
)

data class OrganizationSearch(
    val serial: String? = null // 의료재단 기관 식별자
)

data class PatientSearch(
    val serial: String? = null, // 거래처 수진자 식별자
    val name: String? = null    // 수진자명
)

data class InnerRequestOrganizationResponse(
    val serial: String,
    val name: String? = null,
    val registrationNumber: String? = null,
    val nursingNumber: String? = null,
    val branchCode: String? = null,
    val branchName: String? = null,
    val employeeId: String? = null,
    val employeeName: String? = null,
    val employeePhone: String? = null,
    val createAt: LocalDateTime? = null
)

data class InnerRequestPatientResponse(
    val serial: String? = null,
    val name: String,
    val sex: String? = null,
    val birth: LocalDate? = null,
    val age: Int? = null
)

data class InnerRequestServiceResponse(
    val code: String,
    val serial: String,
    val nameKr: String? = null
)

data class InnerRequestSampleResponse(
    val serial: String,
    val count: Int?,
    val samplingOn: LocalDate? = null,
    val age: Int?,
    val type: RequestSampleTypeResponse? = null
)

data class RequestSampleTypeResponse(
    val code: String,
    val serial: String? = null
)

data class InnerRequestExtensionResponse(
    val code: String,
    val nameKr: String? = null,
    val nameEn: String? = null,
    val value: String
)

data class InnerRequestExtensions(
    val refCd: String,
    val refCont: String
)