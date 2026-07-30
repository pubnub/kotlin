package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNGetEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [EntityApi.get]
 */
expect interface GetEntity : PNFuture<PNGetEntityResult>
