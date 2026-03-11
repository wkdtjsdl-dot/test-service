package com.idrsys.ailis.sales.shared.vo

import com.idrsys.ailis.sales.domain.enums.UserStatEnum
import com.idrsys.web.resolver.dto.JwtAuthenticationUser
import io.jsonwebtoken.Claims
import org.bouncycastle.asn1.x500.style.RFC4519Style.name

data class AuthenticationAdmin(
    val adminId: String = "",
    val email: String? = null,
    val userStat: UserStatEnum = UserStatEnum.ES_CW,
    val roleCodes: List<String> = emptyList(),
    val authorities: List<String> = emptyList(),
) : JwtAuthenticationUser {
    override fun jwtAuthenticationUserByJwtClaims(claims: Claims): JwtAuthenticationUser =
        this.copy(
            adminId = claims["adminId"].toString(),
            email = claims["email"].toString(),
            userStat = UserStatEnum.valueOf(claims["userStat"]?.toString() ?: UserStatEnum.ES_BT.name),
            roleCodes = claims["roleCodes"]?.toString()?.split(",") ?: emptyList(),
            authorities = claims["authorities"]?.toString()?.split(",") ?: emptyList()
        )
}
