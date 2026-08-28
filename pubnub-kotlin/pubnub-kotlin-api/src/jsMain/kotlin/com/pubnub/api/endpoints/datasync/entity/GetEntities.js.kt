package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.DataSyncGetEntitiesResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getEntities]
 */
actual interface GetEntities : PNFuture<DataSyncGetEntitiesResult>
