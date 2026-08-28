package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.DataSyncCreateEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.createEntity]
 */
expect interface CreateEntity : PNFuture<DataSyncCreateEntityResult>
