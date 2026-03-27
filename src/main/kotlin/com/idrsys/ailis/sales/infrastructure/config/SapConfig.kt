package com.idrsys.ailis.sales.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "sap")
class SapConfig {
    lateinit var connection: Connection
    lateinit var rfc: Rfc

    data class Connection(
        val ashost: String,
        val sysnr: String,
        val client: String,
        val user: String,
        val passwd: String,
        val lang: String,
        val codepage: String
    )

    data class Rfc(
        val customerIfLabs: String,
        val params: RfcParams
    )

    data class RfcParams(
        val i_bukrs: String,
        val i_gsber: String
    )
}
