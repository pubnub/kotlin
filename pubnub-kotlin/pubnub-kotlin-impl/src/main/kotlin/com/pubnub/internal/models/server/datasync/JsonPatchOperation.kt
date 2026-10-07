package com.pubnub.internal.models.server.datasync

/**
 * Wire model for a single JSON Patch (RFC-6902) operation. `value`/`from` are dropped from the
 * payload when null (the SDK's Gson instance does not serialize nulls).
 */
internal data class JsonPatchOperation(
    val op: String,
    val path: String,
    val value: Any? = null,
    val from: String? = null,
)
