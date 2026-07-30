package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNCreateEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [EntityApi.create]
 */
expect interface CreateEntity : PNFuture<PNCreateEntityResult>
