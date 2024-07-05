package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNDeleteFileResult

/**
 * @see [PubNub.deleteFile]
 */
interface DeleteFile : com.pubnub.kmp.endpoints.files.DeleteFile, Endpoint<PNDeleteFileResult>
