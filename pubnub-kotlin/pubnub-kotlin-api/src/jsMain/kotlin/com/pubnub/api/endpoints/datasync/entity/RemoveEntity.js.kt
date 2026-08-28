package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.DataSyncRemoveEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.removeEntity]
 */
actual interface RemoveEntity : PNFuture<DataSyncRemoveEntityResult>
