package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.DataSyncSetEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.setEntity]
 */
actual interface SetEntity : PNFuture<DataSyncSetEntityResult>
