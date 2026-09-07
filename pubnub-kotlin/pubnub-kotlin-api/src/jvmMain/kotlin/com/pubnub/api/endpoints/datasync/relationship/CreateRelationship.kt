package com.pubnub.api.endpoints.datasync.relationship

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncCreateRelationshipResult

/**
 * @see [com.pubnub.api.datasync.DataSync.createRelationship]
 */
actual interface CreateRelationship : Endpoint<PNDataSyncCreateRelationshipResult>
