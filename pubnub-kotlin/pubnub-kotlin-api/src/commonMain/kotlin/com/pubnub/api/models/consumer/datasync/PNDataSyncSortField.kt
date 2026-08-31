package com.pubnub.api.models.consumer.datasync

/**
 * A single DataSync sort criterion applied by the list endpoints (e.g.
 * [com.pubnub.api.datasync.DataSync.getChannels]).
 *
 * Unlike App Context v2's `PNSortKey` (which fixes the sortable fields to an enum), DataSync sorts on
 * arbitrary payload properties, so [property] is free-form.
 *
 * @property property The payload property name to sort by.
 * @property ascending Sort direction; `true` (default) is ascending, `false` is descending.
 */
data class PNDataSyncSortField(val property: String, val ascending: Boolean = true)
