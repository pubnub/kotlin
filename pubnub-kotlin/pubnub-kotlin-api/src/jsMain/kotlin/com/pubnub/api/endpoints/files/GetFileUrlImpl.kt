package com.pubnub.api.endpoints.files

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.files.PNFileUrlResult
import kotlin.js.Promise

class GetFileUrlImpl(pubnub: PubNub, params: PubNub.FileInputParameters) : GetFileUrl,
    EndpointImpl<String, PNFileUrlResult>(
        promiseFactory = {
            Promise { resolve, reject ->
                try {
                    resolve(pubnub.getFileUrl(params))
                } catch (e: Exception) {
                    reject(e)
                }
            }
        },
        responseMapping = { result ->
            PNFileUrlResult(result)
        }
    )