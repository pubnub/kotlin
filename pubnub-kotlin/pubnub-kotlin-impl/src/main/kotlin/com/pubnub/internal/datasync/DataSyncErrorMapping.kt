package com.pubnub.internal.datasync

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.datasync.dataSyncErrors

/**
 * Replaces the generic [PubNubError.HTTP_ERROR] with a `DATASYNC_*` category when the response body is a DataSync
 * error envelope (at least one `DS-xxxx` code). The category follows the HTTP status, which the server maps 1:1 from
 * the code, so new server codes need no SDK change; the exact code stays available via [dataSyncErrors].
 *
 * Returned unchanged (still [PubNubError.HTTP_ERROR]):
 * - exceptions without a DataSync body (e.g. a `403` produced in front of DataSync, or a `text/plain` `406`);
 * - DataSync bodies with a status the `when` below doesn't map (e.g. a future `429`), so no category is guessed; the code
 *   is still available via [dataSyncErrors].
 */
internal fun PubNubException.withDataSyncError(): PubNubException {
    if (dataSyncErrors().isEmpty()) {
        return this
    }
    val error =
        when (statusCode) {
            400 -> PubNubError.DATASYNC_BAD_REQUEST
            401, 403 -> PubNubError.DATASYNC_ACCESS_DENIED
            404 -> PubNubError.DATASYNC_NOT_FOUND
            409 -> PubNubError.DATASYNC_CONFLICT
            412 -> PubNubError.DATASYNC_PRECONDITION_FAILED
            in 500..599 -> PubNubError.DATASYNC_SERVER_ERROR
            else -> return this
        }
    return copy(pubnubError = error)
}
