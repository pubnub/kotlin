package com.pubnub.api.endpoints.files

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.files.PNFileUploadResult

/**
 * @see [PubNub.sendFile]
 */
actual interface SendFile : ExtendedRemoteAction<PNFileUploadResult>
