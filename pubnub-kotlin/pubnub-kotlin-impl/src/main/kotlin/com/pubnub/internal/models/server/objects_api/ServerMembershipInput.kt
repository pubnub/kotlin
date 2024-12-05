package com.pubnub.internal.models.server.objects_api

internal data class ServerMembershipInput(
    val channel: ChannelId,
    val custom: Any? = null,
    val status: String? = null,
    val type: String? = null
)

internal data class ChannelId(
    val id: String,
)
