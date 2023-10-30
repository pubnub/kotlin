package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.internal.endpoints.files.ISendFile

/**
 * @see [PubNub.sendFile]
 */
class SendFile internal constructor(sendFile: ISendFile) : Endpoint<PNFileUploadResult>(), ISendFile by sendFile