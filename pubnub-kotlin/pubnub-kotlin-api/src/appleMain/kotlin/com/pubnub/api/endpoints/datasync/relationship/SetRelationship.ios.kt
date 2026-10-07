package com.pubnub.api.endpoints.datasync.relationship

import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncSetRelationshipResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.setRelationship]
 */
actual interface SetRelationship : PNFuture<PNDataSyncSetRelationshipResult>
