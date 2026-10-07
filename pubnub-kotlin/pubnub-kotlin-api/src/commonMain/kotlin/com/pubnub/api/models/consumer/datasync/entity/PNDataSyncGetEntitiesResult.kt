package com.pubnub.api.models.consumer.datasync.entity

import com.pubnub.api.models.consumer.datasync.PNDataSyncPage

/**
 * Result of `pubnub.dataSync.getEntities`.
 *
 * DataSync list endpoints are cursor-based.
 *
 * @property status HTTP status code of the response.
 * @property data The entities on this page.
 * @property next Cursor-based pagination metadata. Always present (an empty page when the server sends
 *   no `meta`); read [PNDataSyncPage.hasNext] to detect the last page, never `next == null`.
 */
data class PNDataSyncGetEntitiesResult(
    val status: Int,
    val data: List<PNDataSyncEntity>,
    val next: PNDataSyncPage,
)
