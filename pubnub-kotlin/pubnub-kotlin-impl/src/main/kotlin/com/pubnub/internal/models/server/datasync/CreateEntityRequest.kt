package com.pubnub.internal.models.server.datasync

internal data class CreateEntityRequest(
    val data: CreateEntityRequestData,
)

internal data class CreateEntityRequestData(
    // Omitted from the wire payload when null (server generates the id) — the SDK's Gson instance
    // does not serialize nulls.
    val id: String? = null,
    val entityClass: String,
    val entityClassVersion: Int,
    val status: String? = null,
    val payload: Any? = null,
)
