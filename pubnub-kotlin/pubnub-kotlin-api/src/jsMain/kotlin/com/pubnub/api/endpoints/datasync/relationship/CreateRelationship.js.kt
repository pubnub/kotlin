package com.pubnub.api.endpoints.datasync.relationship

import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncCreateRelationshipResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.createRelationship]
 */
actual interface CreateRelationship : PNFuture<PNDataSyncCreateRelationshipResult>
