package com.pubnub.api.models.consumer.datasync

/**
 * A single error item from a DataSync error response (`{"errors":[...]}`). One response may carry several items,
 * e.g. one per invalid field for `DS-0004`.
 *
 * @property code Stable, machine-readable error code, e.g. `DS-0302`. Branch on this, not on [message].
 * @property message Human-readable message from the server. Not stable; don't parse it.
 * @property path Where the problem is, if known: a JSON Pointer, a query-parameter name (`filter`, `sort`, `cursor`…)
 *   or a field name.
 * @property location Position of the problem inside the [path] value. Only set for filter-expression errors
 *   (`DS-1000`..`DS-1003`).
 */
data class PNDataSyncError(
    val code: String,
    val message: String,
    val path: String? = null,
    val location: PNDataSyncErrorLocation? = null,
)

/**
 * Character range inside a filter expression that a [PNDataSyncError] points at.
 *
 * @property offset 0-based start of the range.
 * @property length Length of the range.
 */
data class PNDataSyncErrorLocation(
    val offset: Int,
    val length: Int,
)
