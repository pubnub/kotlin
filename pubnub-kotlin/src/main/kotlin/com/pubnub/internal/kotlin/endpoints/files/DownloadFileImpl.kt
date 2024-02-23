package com.pubnub.internal.kotlin.endpoints.files

import com.pubnub.api.endpoints.files.DownloadFile
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.files.IDownloadFile

/**
 * @see [PubNubImpl.downloadFile]
 */
class DownloadFileImpl internal constructor(downloadFile: IDownloadFile) :
    IDownloadFile by downloadFile,
        DownloadFile
