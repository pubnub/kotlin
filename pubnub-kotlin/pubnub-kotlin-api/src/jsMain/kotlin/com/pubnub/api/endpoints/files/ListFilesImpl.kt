package com.pubnub.api.endpoints.files

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.api.models.consumer.files.PNUploadedFile
import com.pubnub.api.models.consumer.objects.PNPage

class ListFilesImpl(pubnub: PubNub, params: PubNub.ListFilesParameters) : ListFiles,
    EndpointImpl<PubNub.ListFilesResponse, PNListFilesResult>(
        promiseFactory = { pubnub.listFiles(params) },
        responseMapping = { result ->
            PNListFilesResult(
                result.count.toInt(),
                PNPage.PNNext(result.next),
                result.status.toInt(),
                result.data.map { file ->
                    PNUploadedFile(
                        file.id,
                        file.name,
                        file.size.toInt(),
                        file.created
                    )
                }
            )
        }
    )