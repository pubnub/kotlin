package com.pubnub.api.endpoints.files

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.files.PNFileUrlResult

/**
 * @see [PubNub.getFileUrl]
 */
actual interface GetFileUrl : PNFuture<PNFileUrlResult>