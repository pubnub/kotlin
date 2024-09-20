package com.pubnub.api.endpoints.files

import com.pubnub.api.models.consumer.files.PNDeleteFileResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.deleteFile]
 */
expect interface DeleteFile : PNFuture<PNDeleteFileResult>
