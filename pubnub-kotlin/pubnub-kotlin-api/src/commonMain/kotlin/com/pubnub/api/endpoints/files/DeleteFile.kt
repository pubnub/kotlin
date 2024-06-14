package com.pubnub.api.endpoints.files

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.files.PNDeleteFileResult

/**
 * @see [PubNub.deleteFile]
 */
expect interface DeleteFile : PNFuture<PNDeleteFileResult>