package com.pubnub.api.endpoints.files

import com.pubnub.api.models.consumer.files.PNFileUrlResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.getFileUrl]
 */
actual interface GetFileUrl : PNFuture<PNFileUrlResult>
