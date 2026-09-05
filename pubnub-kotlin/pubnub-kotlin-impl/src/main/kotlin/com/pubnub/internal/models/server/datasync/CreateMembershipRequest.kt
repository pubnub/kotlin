package com.pubnub.internal.models.server.datasync

internal data class CreateMembershipRequest(
    val data: CreateMembershipRequestData,
)

internal data class CreateMembershipRequestData(
    // Omitted from the wire payload when null (server generates the id) — the SDK's Gson instance
    // does not serialize nulls.
    val id: String? = null,
    // The Channel (entity A) and User (entity B) this membership links — both required by the server.
    val channelId: String,
    val userId: String,
    // The relationship class version. There is no `relationshipClass` field on create — the server
    // implies the built-in "Membership" class.
    val relationshipClassVersion: Int,
    val status: String? = null,
    val payload: Any? = null,
)
