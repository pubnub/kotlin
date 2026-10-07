package com.pubnub.api.models.consumer.datasync

/**
 * Cursor-based pagination metadata returned by DataSync list endpoints (e.g.
 * [com.pubnub.api.datasync.DataSync.getChannels]).
 *
 * @property cursor Opaque cursor for the next page, or `null` when there are no more results. Pass it
 *   back as the `cursor` argument to fetch the next page.
 * @property hasNext Whether there are more results after this page. This — not a `null` [cursor] — is the
 *   authoritative "no more pages" signal.
 * @property limit The limit applied to this page (may differ from the requested limit).
 */
data class PNDataSyncPage(
    val cursor: String? = null,
    val hasNext: Boolean = false,
    val limit: Int? = null,
)
