package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNPatchEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [EntityApi.patch]
 */
expect interface PatchEntity : PNFuture<PNPatchEntityResult>
