package com.pubnub.api.endpoints.files

import com.pubnub.api.models.consumer.files.PNDownloadFileResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.sendFile]
 */
expect interface DownloadFile : PNFuture<PNDownloadFileResult>
