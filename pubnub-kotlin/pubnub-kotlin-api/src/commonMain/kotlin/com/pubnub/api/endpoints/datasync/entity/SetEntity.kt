package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNSetEntityResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.setEntity]
 */
expect interface SetEntity : PNFuture<PNSetEntityResult>
