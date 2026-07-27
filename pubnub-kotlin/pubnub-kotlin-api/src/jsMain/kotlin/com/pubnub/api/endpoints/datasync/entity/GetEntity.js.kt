package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNGetEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [EntityApi.get]
 */
actual interface GetEntity : PNFuture<PNGetEntityResult>
