package com.pubnub.api.endpoints.datasync.relationship

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncGetRelationshipsResult

/**
 * @see [com.pubnub.api.datasync.DataSync.getRelationships]
 */
actual interface GetRelationships : Endpoint<PNDataSyncGetRelationshipsResult>
