package com.pubnub.api.models.server.objects_api

data class ChangeMembershipInput(
    val set: List<ServerMembershipInput> = listOf(),
    val delete: List<ServerMembershipInput> = listOf()
)
