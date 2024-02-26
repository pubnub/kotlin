package com.pubnub.internal.endpoints.files

import com.pubnub.api.endpoints.files.SendFile
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.sendFile]
 */
class SendFileImpl internal constructor(sendFile: ISendFile) : ISendFile by sendFile, SendFile
