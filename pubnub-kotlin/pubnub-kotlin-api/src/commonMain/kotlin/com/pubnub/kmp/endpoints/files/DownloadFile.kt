package com.pubnub.kmp.endpoints.files

import com.pubnub.api.models.consumer.files.PNDownloadFileResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.sendFile]
 */
interface DownloadFile : PNFuture<PNDownloadFileResult>
