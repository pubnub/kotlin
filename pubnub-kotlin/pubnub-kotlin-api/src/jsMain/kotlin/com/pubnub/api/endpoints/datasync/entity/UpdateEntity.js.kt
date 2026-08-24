package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNUpdateEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.updateEntity]
 */
actual interface UpdateEntity : PNFuture<PNUpdateEntityResult>
