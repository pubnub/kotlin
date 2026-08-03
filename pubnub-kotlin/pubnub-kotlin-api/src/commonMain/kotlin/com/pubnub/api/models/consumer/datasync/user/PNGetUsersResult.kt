package com.pubnub.api.models.consumer.datasync.user

/**
 * Result of `pubnub.dataSync.user.getAll`.
 *
 * DataSync list endpoints are **cursor-based** (unlike App Context's page-based `PNPage`).
 *
 * @property status HTTP status code of the response.
 * @property data The users on this page.
 * @property next Opaque cursor for the next page, or `null` when there are no more results.
 * @property hasNext Whether there are more results after this page.
 * @property limit The limit applied to this page (may differ from the requested limit).
 */
data class PNGetUsersResult(
    val status: Int,
    val data: List<PNUser>,
    val next: String? = null,
    val hasNext: Boolean = false,
    val limit: Int? = null,
)
