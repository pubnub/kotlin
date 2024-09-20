package com.pubnub.api.endpoints.files

import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.listFiles]
 */
actual interface ListFiles : PNFuture<PNListFilesResult>
