package com.pubnub.internal.endpoints.files

import com.pubnub.api.endpoints.files.SendFile
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.sendFile]
 */
class SendFileImpl internal constructor(sendFile: SendFileInterface) : SendFileInterface by sendFile, SendFile
