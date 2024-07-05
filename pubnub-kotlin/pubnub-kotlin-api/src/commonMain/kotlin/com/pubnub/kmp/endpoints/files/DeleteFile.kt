package com.pubnub.kmp.endpoints.files

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.files.PNDeleteFileResult

/**
 * @see [PubNub.deleteFile]
 */
interface DeleteFile : PNFuture<PNDeleteFileResult>