package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncRemoveEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.removeEntity]
 */
expect interface RemoveEntity : PNFuture<PNDataSyncRemoveEntityResult>
