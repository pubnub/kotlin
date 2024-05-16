package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNDeleteFileResult

/**
 * @see [PubNub.deleteFile]
 */
expect interface DeleteFile : Endpoint<PNDeleteFileResult>