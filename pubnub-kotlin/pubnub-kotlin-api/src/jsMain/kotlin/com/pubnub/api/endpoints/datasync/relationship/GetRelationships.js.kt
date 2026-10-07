package com.pubnub.api.endpoints.datasync.relationship

import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncGetRelationshipsResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getRelationships]
 */
actual interface GetRelationships : PNFuture<PNDataSyncGetRelationshipsResult>
