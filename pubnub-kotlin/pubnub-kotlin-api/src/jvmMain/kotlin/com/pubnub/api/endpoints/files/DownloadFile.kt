package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNDownloadFileResult

/**
 * @see [PubNub.downloadFile]
 */
interface DownloadFile : com.pubnub.kmp.endpoints.files.DownloadFile, Endpoint<PNDownloadFileResult>
