package com.pubnub.api.endpoints.datasync.relationship

import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRemoveRelationshipResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.removeRelationship]
 */
expect interface RemoveRelationship : PNFuture<PNDataSyncRemoveRelationshipResult>
