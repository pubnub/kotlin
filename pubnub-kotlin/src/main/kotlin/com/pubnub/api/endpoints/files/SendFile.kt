package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.internal.PubNubImpl
import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.internal.endpoints.files.ISendFile

/**
 * @see [PubNubImpl.sendFile]
 */
class SendFile internal constructor(sendFile: ISendFile) : Endpoint<PNFileUploadResult>(), ISendFile by sendFile
