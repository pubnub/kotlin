package com.pubnub.api.models.server.objects_api

data class ServerMembershipInput(
    val channel: ChannelId,
    val custom: Any? = null,
    val status: String? = null
)

data class ChannelId(
    val id: String
)
