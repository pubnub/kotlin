package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNPatchEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.patchEntity]
 */
actual interface PatchEntity : PNFuture<PNPatchEntityResult>
