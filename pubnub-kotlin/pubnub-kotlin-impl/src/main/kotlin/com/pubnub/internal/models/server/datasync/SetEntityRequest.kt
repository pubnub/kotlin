package com.pubnub.internal.models.server.datasync

internal data class SetEntityRequest(
    val data: SetEntityRequestData,
)

internal data class SetEntityRequestData(
    val entityClassVersion: Int,
    // Omitted from the wire payload when null — the SDK's Gson instance does not serialize nulls.
    val status: String? = null,
    val payload: Any? = null,
)
