package com.pubnub.api.endpoints.files

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.kmp.endpoints.files.SendFile

/**
 * @see [PubNub.sendFile]
 */
interface SendFile : SendFile, ExtendedRemoteAction<PNFileUploadResult>
