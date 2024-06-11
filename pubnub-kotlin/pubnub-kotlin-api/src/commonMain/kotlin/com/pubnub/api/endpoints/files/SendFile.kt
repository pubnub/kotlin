package com.pubnub.api.endpoints.files

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.sendFile]
 */
expect interface SendFile : PNFuture<PNFileUploadResult>
