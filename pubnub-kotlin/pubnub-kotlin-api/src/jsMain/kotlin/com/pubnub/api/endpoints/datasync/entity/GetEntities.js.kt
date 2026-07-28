package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNGetEntitiesResult
import com.pubnub.kmp.PNFuture

/**
 * @see [EntityApi.getAll]
 */
actual interface GetEntities : PNFuture<PNGetEntitiesResult>
