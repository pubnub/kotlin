package com.pubnub.api.endpoints.files

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.files.PNDeleteFileResult

class DeleteFileImpl(pubnub: PubNub, params: PubNub.FileInputParameters) : DeleteFile,
    EndpointImpl<PubNub.DeleteFileResponse, PNDeleteFileResult>(
        promiseFactory = { pubnub.deleteFile(params) },
        responseMapping = { result ->
            PNDeleteFileResult(result.status.toInt())
        }
    )
