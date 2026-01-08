package com.idrsys.ailis.sales.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.util.*

/**
 * Integration test를 위한 JWT 토큰 생성 헬퍼
 */
object JwtTestHelper {

    /**
     * 테스트용 JWT access token 생성
     *
     * @param adminId 관리자 ID
     * @param secretKey JWT secret key (application.yml의 jwt.secretkey 값 사용)
     * @param expirationMs 만료 시간 (밀리초, 기본값: 1시간)
     * @return JWT access token
     */
    fun createAccessToken(
        adminId: String = "test-admin",
        secretKey: String,
        expirationMs: Long = 3600000
    ): String {
        val now = Date()
        val expiration = Date(now.time + expirationMs)

        val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

        /*
        this.jwtSecretKey = jwtSecretKey;
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);

        Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentTime)
                .setExpiration(new Date(currentTime.getTime() + this.accessTokenValidTime * 1000L))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
         */
        return Jwts.builder()
            .setSubject(adminId)
            .claim("adminId", adminId)
            .claim("email", "test@example.com")
            .claim("userStat", "ES_CW")
            .claim("roleCodes", listOf("RO_DPP"))
            .claim("authorities", listOf("AU_BS","AU_SY","AU_CSM","AU_RM","AU_SM","AU_DM","AU_CHM","AU_CMM","AU_GM","AU_TTM"))
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
}
