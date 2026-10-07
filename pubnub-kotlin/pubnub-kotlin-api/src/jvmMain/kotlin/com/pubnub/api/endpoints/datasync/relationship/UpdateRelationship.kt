package com.pubnub.api.endpoints.datasync.relationship

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncUpdateRelationshipResult

/**
 * @see [com.pubnub.api.datasync.DataSync.updateRelationship]
 */
actual interface UpdateRelationship : Endpoint<PNDataSyncUpdateRelationshipResult>
