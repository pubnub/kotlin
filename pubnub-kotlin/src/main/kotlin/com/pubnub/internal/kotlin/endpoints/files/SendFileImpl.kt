package com.pubnub.internal.kotlin.endpoints.files

import com.pubnub.api.endpoints.files.SendFile
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.files.ISendFile

/**
 * @see [PubNubImpl.sendFile]
 */
class SendFileImpl internal constructor(sendFile: ISendFile) : ISendFile by sendFile, SendFile
