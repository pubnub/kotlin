package com.pubnub.api.models.server.objects_api

internal data class ChangeMemberInput(
    val set: List<ServerMemberInput> = listOf(),
    val delete: List<ServerMemberInput> = listOf()
)
