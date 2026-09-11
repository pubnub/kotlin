package com.pubnub.api.endpoints.datasync.relationship

import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncUpdateRelationshipResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.updateRelationship]
 */
expect interface UpdateRelationship : PNFuture<PNDataSyncUpdateRelationshipResult>
