package com.pubnub.api.models.consumer.datasync.relationship

import com.pubnub.api.models.consumer.datasync.PNDataSyncPage

/**
 * Result of `pubnub.dataSync.getRelationships`.
 *
 * DataSync list endpoints are **cursor-based** (unlike App Context's page-based `PNPage`).
 *
 * @property status HTTP status code of the response.
 * @property data The relationships on this page.
 * @property next Cursor-based pagination metadata. Always present (an empty page when the server sends
 *   no `meta`); read [PNDataSyncPage.hasNext] to detect the last page, never `next == null`.
 */
data class PNDataSyncGetRelationshipsResult(
    val status: Int,
    val data: List<PNDataSyncRelationship>,
    val next: PNDataSyncPage,
)
