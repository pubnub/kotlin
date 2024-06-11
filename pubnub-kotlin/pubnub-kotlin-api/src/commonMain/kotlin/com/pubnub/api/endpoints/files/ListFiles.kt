package com.pubnub.api.endpoints.files

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.files.PNListFilesResult

/**
 * @see [PubNub.listFiles]
 */
expect interface ListFiles : PNFuture<PNListFilesResult>