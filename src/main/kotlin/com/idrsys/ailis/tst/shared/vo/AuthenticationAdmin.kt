package com.idrsys.ailis.tst.shared.vo

import com.idrsys.web.resolver.dto.JwtAuthenticationUser
import io.jsonwebtoken.Claims

data class AuthenticationAdmin(
    val adminId: String = "",
    val email: String? = null,
    val roleCodes: List<String> = emptyList(),
    val authorities: List<String> = emptyList(),
) : JwtAuthenticationUser {
    override fun jwtAuthenticationUserByJwtClaims(claims: Claims): JwtAuthenticationUser =
        this.copy(
            adminId = claims["adminId"].toString(),
            email = claims["email"]?.toString(),
            roleCodes = claims["roleCodes"]?.toString()?.split(",") ?: emptyList(),
            authorities = claims["authorities"]?.toString()?.split(",") ?: emptyList()
        )
}
