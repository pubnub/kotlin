package com.pubnub.api.models.consumer.datasync.entity

/**
 * A single JSON Patch (RFC-6902) operation used by `pubnub.dataSync.updateEntity`.
 *
 * @property op The operation to perform (e.g. `add`, `remove`, `replace`, `move`, `copy`, `test`).
 *   Modeled as a [String] rather than an enum to stay source-compatible with future values.
 * @property path JSON Pointer (RFC-6901) to the target location, e.g. `/status` or `/payload/role`.
 * @property value The value to add, replace, or test. Required for `add`/`replace`/`test`.
 * @property from JSON Pointer to the source location. Required for `move`/`copy`.
 */
data class PNJsonPatchOperation(
    val op: String,
    val path: String,
    val value: Any? = null,
    val from: String? = null,
)
