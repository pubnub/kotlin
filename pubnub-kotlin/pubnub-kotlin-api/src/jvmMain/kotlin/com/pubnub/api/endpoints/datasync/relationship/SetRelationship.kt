package com.pubnub.api.endpoints.datasync.relationship

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncSetRelationshipResult

/**
 * @see [com.pubnub.api.datasync.DataSync.setRelationship]
 */
actual interface SetRelationship : Endpoint<PNDataSyncSetRelationshipResult>
