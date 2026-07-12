package com.alxdmk.yandxtv.domain.model

data class Credential(
    val siteId: Long,
    val username: String = "",
    val password: String = "",
    val note: String = ""
)
