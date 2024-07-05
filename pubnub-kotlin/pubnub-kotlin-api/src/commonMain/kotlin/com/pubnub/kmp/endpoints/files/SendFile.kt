package com.pubnub.kmp.endpoints.files

import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.sendFile]
 */
interface SendFile : PNFuture<PNFileUploadResult>
