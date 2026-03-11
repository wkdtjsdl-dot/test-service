package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.query.HpLoginUserQuery
import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserCommand
import com.idrsys.ailis.sales.application.dto.response.HpLoginUserResponse
import com.idrsys.ailis.sales.domain.model.HpLoginUser
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.time.LocalDateTime

@Mapper(componentModel = "spring")
interface HpLoginUserMapper {

    @Mapping(target = "hpLoginUserId", source = "command.hpLoginUserId")
    @Mapping(target = "custMstId", source = "command.custMstId")
    @Mapping(target = "custCd", source = "command.custCd")
    @Mapping(target = "hpCustDiv", source = "command.hpCustDiv")
    @Mapping(target = "loginId", source = "command.loginId")
    @Mapping(target = "loginPswd", source = "command.loginPswd")
    @Mapping(target = "loginFailNum", source = "command.loginFailNum")
    @Mapping(target = "pswdChngDtime", source = "command.pswdChngDtime")
    @Mapping(target = "lastLoginDtime", source = "command.lastLoginDtime")
    @Mapping(target = "loginNm", source = "command.loginNm")
    @Mapping(target = "loginPersonContact", source = "command.loginPersonContact")
    @Mapping(target = "useYn", source = "command.useYn")
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "updater", source = "creator")
    @Mapping(target = "createDtime", source = "now")
    @Mapping(target = "updateDtime", source = "now")
    fun toDomain(command: HpLoginUserCommand, creator: String, now: LocalDateTime): HpLoginUser

    fun toResponse(domain: HpLoginUser): HpLoginUserResponse

    fun toResponseFromQuery(query: HpLoginUserQuery): HpLoginUserResponse
}
