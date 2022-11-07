package com.pubnub.api.models.server.objects_api

internal data class ChangeMembershipInput(
    val set: List<MembershipInput> = listOf(),
    val delete: List<MembershipInput> = listOf()
)
