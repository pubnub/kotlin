package com.pubnub.entities.model

data class PNUser(
    val id: String,
    val name: String?,
    val externalId: String?,
    val profileUrl: String?,
    val email: String?,
    val custom: Any?,
    val updated: String?,
    val eTag: String?
)
