package com.pubnub.api.models.server.objects_api

internal data class MembershipInput(
    val channel: ChannelId,
    val custom: Any? = null,
    val status: String? = null
)

internal data class ChannelId(
    val id: String
)
