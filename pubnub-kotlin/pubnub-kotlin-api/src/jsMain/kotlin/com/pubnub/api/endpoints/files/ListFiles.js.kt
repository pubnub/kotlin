package com.pubnub.api.endpoints.files

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.files.PNListFilesResult

/**
 * @see [PubNub.listFiles]
 */
actual interface ListFiles : PNFuture<PNListFilesResult>