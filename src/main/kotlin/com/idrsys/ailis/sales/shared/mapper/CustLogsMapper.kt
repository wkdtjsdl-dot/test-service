package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.cust.CustUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.CustLogsEditResponse
import com.idrsys.ailis.sales.domain.model.CustMstHst
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface CustLogsMapper {

    @Mapping(target = "directAcctAcctCd", source = "directAcctAcctCd", defaultValue = "")
    fun toUpdateCommand(command: CustMstHst): CustUpdateCommand

    @Mappings(
        Mapping(source = "newLog.custMstHstId", target = "custMstHstId"),
        Mapping(source = "newLog.updater", target = "editBy"),
        Mapping(source = "newLog.updateDtime", target = "editAt"),
        Mapping(source = "diffString", target = "editContents"),
        Mapping(target = "editReason", expression = "java(\"추후 추가 예정\")"),
        Mapping(source = "oldLog.custMstId", target = "custMstId"),
        Mapping(source = "oldLog.custCd", target = "custCd"),
        Mapping(source = "oldLog.custNm", target = "custNm")
    )
    fun toEditResponse(oldLog: CustMstHst, newLog: CustMstHst, diffString: String): CustLogsEditResponse
}
