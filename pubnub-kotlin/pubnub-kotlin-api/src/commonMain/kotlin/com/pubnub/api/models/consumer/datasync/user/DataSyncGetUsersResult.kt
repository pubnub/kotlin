package com.pubnub.api.models.consumer.datasync.user

import com.pubnub.api.models.consumer.datasync.PNDataSyncPage

/**
 * Result of `pubnub.dataSync.getUsers`.
 *
 * DataSync list endpoints are **cursor-based** (unlike App Context's page-based `PNPage`).
 *
 * @property status HTTP status code of the response.
 * @property data The users on this page.
 * @property next Cursor-based pagination metadata. Always present (an empty page when the server sends
 *   no `meta`); read [PNDataSyncPage.hasNext] to detect the last page, never `next == null`.
 */
data class DataSyncGetUsersResult(
    val status: Int,
    val data: List<DataSyncUser>,
    val next: PNDataSyncPage,
)
