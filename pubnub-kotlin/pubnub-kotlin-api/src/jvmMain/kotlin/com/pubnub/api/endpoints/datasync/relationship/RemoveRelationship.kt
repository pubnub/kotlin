package com.pubnub.api.endpoints.datasync.relationship

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRemoveRelationshipResult

/**
 * @see [com.pubnub.api.datasync.DataSync.removeRelationship]
 */
actual interface RemoveRelationship : Endpoint<PNDataSyncRemoveRelationshipResult>
