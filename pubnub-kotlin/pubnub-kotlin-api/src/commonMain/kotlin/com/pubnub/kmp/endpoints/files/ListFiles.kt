package com.pubnub.kmp.endpoints.files

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.files.PNListFilesResult

/**
 * @see [PubNub.listFiles]
 */
interface ListFiles : PNFuture<PNListFilesResult>