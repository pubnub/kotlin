package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncSetEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.setEntity]
 */
actual interface SetEntity : PNFuture<PNDataSyncSetEntityResult>
