package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.files.PNDeleteFileResult
import com.pubnub.internal.endpoints.files.IDeleteFile

/**
 * @see [PubNub.deleteFile]
 */
class DeleteFile internal constructor(deleteFile: IDeleteFile) : Endpoint<PNDeleteFileResult?>(),
    IDeleteFile by deleteFile