package com.pubnub.internal.endpoints.files

import com.pubnub.api.endpoints.files.DownloadFile
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.downloadFile]
 */
class DownloadFileImpl internal constructor(downloadFile: IDownloadFile) :
    IDownloadFile by downloadFile,
    DownloadFile
