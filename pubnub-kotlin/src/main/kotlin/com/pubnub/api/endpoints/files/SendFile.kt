package com.pubnub.api.endpoints.files

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.internal.DelegatingRemoteAction
import com.pubnub.internal.endpoints.files.SendFile

/**
 * @see [PubNub.sendFile]
 */
class SendFile internal constructor(private val sendFile: SendFile) : DelegatingRemoteAction<PNFileUploadResult>() {
    override fun createAction(): ExtendedRemoteAction<PNFileUploadResult> = sendFile
}