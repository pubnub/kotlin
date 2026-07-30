package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNUpdateEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [EntityApi.update]
 */
actual interface UpdateEntity : PNFuture<PNUpdateEntityResult>
