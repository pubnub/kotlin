package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNRemoveEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [EntityApi.delete]
 */
actual interface RemoveEntity : PNFuture<PNRemoveEntityResult>
