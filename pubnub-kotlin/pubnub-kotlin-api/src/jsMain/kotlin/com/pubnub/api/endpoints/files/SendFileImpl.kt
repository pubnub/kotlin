package com.pubnub.api.endpoints.files

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.files.PNBaseFile
import com.pubnub.api.models.consumer.files.PNFileUploadResult

class SendFileImpl(pubnub: PubNub, params: PubNub.SendFileParameters) : SendFile, EndpointImpl<PubNub.SendFileResponse, PNFileUploadResult>(
    promiseFactory = { pubnub.sendFile(params) },
    responseMapping = {
        PNFileUploadResult(
            it.timetoken.toLong(),
            200, // TODO anything else possible here?
            PNBaseFile(
                it.id,
                it.name
            )
        )
    }
)
