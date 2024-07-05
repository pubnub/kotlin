package com.pubnub.kmp.endpoints.files

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.files.PNFileUrlResult

/**
 * @see [PubNub.getFileUrl]
 */
interface GetFileUrl : PNFuture<PNFileUrlResult>