package com.pubnub.api.models.server.objects_api

data class ChannelId(val id: String)

internal data class MembershipInput(
    val channel: ChannelId,
    val custom: Any? = null
)

internal data class ChangeMembershipInput(
    val set: List<MembershipInput> = listOf(),
    val delete: List<MembershipInput> = listOf()
)

internal data class UUIDId(val id: String)

internal data class MemberInput(
    val uuid: UUIDId,
    val custom: Any? = null
)

internal data class ChangeMemberInput(
    val set: List<MemberInput> = listOf(),
    val delete: List<MemberInput> = listOf()
)
