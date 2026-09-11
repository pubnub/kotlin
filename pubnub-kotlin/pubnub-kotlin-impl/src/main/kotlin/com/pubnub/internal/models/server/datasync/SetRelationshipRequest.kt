package com.pubnub.internal.models.server.datasync

internal data class SetRelationshipRequest(
    val data: SetRelationshipRequestData,
)

internal data class SetRelationshipRequestData(
    // The relationship wire key — cannot reuse SetEntityRequest, which emits `entityClassVersion`.
    val relationshipClassVersion: Int,
    // Omitted from the wire payload when null — the SDK's Gson instance does not serialize nulls.
    val status: String? = null,
    val payload: Any? = null,
)
