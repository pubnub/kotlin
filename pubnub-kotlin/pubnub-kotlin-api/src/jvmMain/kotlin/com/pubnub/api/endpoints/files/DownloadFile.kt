package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNDownloadFileResult

/**
 * @see [PubNub.downloadFile]
 */
actual interface DownloadFile : Endpoint<PNDownloadFileResult>
