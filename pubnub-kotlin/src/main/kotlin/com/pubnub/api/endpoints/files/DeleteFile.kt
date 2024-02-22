package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.internal.PubNubImpl
import com.pubnub.api.models.consumer.files.PNDeleteFileResult
import com.pubnub.internal.endpoints.files.IDeleteFile

/**
 * @see [PubNubImpl.deleteFile]
 */
class DeleteFile internal constructor(deleteFile: IDeleteFile) :
    Endpoint<PNDeleteFileResult?>(),
    IDeleteFile by deleteFile
