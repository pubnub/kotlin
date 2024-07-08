package com.pubnub.api.endpoints.files

import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.listFiles]
 */
expect interface ListFiles : PNFuture<PNListFilesResult>
