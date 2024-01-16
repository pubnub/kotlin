package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.files.PNDownloadFileResult
import com.pubnub.internal.endpoints.files.IDownloadFile

/**
 * @see [PubNub.downloadFile]
 */
class DownloadFile internal constructor(downloadFile: IDownloadFile) : Endpoint<PNDownloadFileResult>(),
    IDownloadFile by downloadFile