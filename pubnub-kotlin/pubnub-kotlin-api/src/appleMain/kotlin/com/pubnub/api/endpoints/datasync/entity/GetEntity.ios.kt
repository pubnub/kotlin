package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNGetEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getEntity]
 */
actual interface GetEntity : PNFuture<PNGetEntityResult>
