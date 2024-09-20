package com.pubnub.api.endpoints.files

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.files.PNDownloadFileResult
import com.pubnub.kmp.DownloadableImpl

class DownloadFileImpl(pubnub: PubNub, params: PubNub.DownloadFileParameters) : DownloadFile, EndpointImpl<Any, PNDownloadFileResult>(
    promiseFactory = { pubnub.downloadFile(params) },
    responseMapping = {
        PNDownloadFileResult(
            params.name,
            DownloadableImpl(it)
        )
    }
)
