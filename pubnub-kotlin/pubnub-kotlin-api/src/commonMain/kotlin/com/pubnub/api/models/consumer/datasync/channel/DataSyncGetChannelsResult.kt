package com.pubnub.api.models.consumer.datasync.channel

import com.pubnub.api.models.consumer.datasync.PNDataSyncPage

/**
 * Result of `pubnub.dataSync.getChannels`.
 *
 * DataSync list endpoints are **cursor-based** (unlike App Context's page-based `PNPage`).
 *
 * @property status HTTP status code of the response.
 * @property data The channels on this page.
 * @property next Cursor-based pagination metadata. Always present (an empty page when the server sends
 *   no `meta`); read [PNDataSyncPage.hasNext] to detect the last page, never `next == null`.
 */
data class DataSyncGetChannelsResult(
    val status: Int,
    val data: List<DataSyncChannel>,
    val next: PNDataSyncPage,
)
