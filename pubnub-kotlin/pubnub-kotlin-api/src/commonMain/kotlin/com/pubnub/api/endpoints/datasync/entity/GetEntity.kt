package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.DataSyncGetEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getEntity]
 */
expect interface GetEntity : PNFuture<DataSyncGetEntityResult>
