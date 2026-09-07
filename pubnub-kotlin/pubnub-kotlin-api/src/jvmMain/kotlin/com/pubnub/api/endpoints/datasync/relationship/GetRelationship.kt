package com.pubnub.api.endpoints.datasync.relationship

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncGetRelationshipResult

/**
 * @see [com.pubnub.api.datasync.DataSync.getRelationship]
 */
actual interface GetRelationship : Endpoint<PNDataSyncGetRelationshipResult>
