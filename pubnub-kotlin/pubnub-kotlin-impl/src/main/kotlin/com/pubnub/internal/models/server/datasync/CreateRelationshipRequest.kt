package com.pubnub.internal.models.server.datasync

internal data class CreateRelationshipRequest(
    val data: CreateRelationshipRequestData,
)

internal data class CreateRelationshipRequestData(
    // Omitted from the wire payload when null (server generates the id) — the SDK's Gson instance
    // does not serialize nulls.
    val id: String? = null,
    // The two entities this relationship links — both required by the server.
    val entityAId: String,
    val entityBId: String,
    // The relationship class and version. These properties are named after the wire keys directly, so the
    // SDK's Gson instance serializes them verbatim as `relationshipClass` / `relationshipClassVersion`
    // without any `@SerializedName`. The public API param is `className` / `classVersion`; the endpoint maps
    // those args into these DTO properties.
    val relationshipClass: String,
    val relationshipClassVersion: Int,
    val status: String? = null,
    val payload: Any? = null,
)
