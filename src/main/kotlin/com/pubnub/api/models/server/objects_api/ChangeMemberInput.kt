package com.pubnub.api.models.server.objects_api

internal data class ChangeMemberInput(
    val set: List<ChannelMemberInput> = listOf(),
    val delete: List<ChannelMemberInput> = listOf()
)
