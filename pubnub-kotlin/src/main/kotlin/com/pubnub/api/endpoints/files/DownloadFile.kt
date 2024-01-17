package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.files.PNDownloadFileResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.files.DownloadFile
import com.pubnub.internal.endpoints.files.IDownloadFile

/**
 * @see [PubNub.downloadFile]
 */
class DownloadFile internal constructor(private val downloadFile: DownloadFile) : DelegatingEndpoint<PNDownloadFileResult>(),
    IDownloadFile by downloadFile {
    override fun createAction(): Endpoint<PNDownloadFileResult> = downloadFile.mapIdentity()
}