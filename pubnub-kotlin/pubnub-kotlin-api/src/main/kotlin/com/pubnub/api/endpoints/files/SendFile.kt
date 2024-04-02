package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNFileUploadResult

/**
 * @see [PubNub.sendFile]
 */
interface SendFile : Endpoint<PNFileUploadResult>
