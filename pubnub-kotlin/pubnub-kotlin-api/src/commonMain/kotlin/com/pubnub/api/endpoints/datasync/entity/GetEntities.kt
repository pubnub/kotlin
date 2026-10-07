package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncGetEntitiesResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getEntities]
 */
expect interface GetEntities : PNFuture<PNDataSyncGetEntitiesResult>
