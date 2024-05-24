package com.pubnub.api.endpoints.files

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNDeleteFileResult

/**
 * @see [PubNub.deleteFile]
 */
actual interface DeleteFile : Endpoint<PNDeleteFileResult>