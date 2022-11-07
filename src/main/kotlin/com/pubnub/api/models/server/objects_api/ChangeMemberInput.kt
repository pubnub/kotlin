package com.pubnub.api.models.server.objects_api

internal data class ChangeMemberInput(
    val set: List<MemberInput> = listOf(),
    val delete: List<MemberInput> = listOf()
)
