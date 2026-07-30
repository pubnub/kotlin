package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNGetEntitiesResult
import com.pubnub.kmp.PNFuture

/**
 * @see [EntityApi.getAll]
 */
expect interface GetEntities : PNFuture<PNGetEntitiesResult>
