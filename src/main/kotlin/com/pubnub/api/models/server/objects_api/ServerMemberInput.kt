package com.pubnub.api.models.server.objects_api

data class ServerMemberInput(
    val uuid: UUIDId,
    val custom: Any? = null,
    val status: String? = null
)

data class UUIDId(val id: String)
