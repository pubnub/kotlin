package com.pubnub.internal.endpoints.files

import com.pubnub.api.endpoints.files.ListFiles
import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.listFiles]
 */
class ListFilesImpl internal constructor(listFiles: ListFilesInterface) :
    ListFilesInterface by listFiles,
    ListFiles,
    EndpointImpl<PNListFilesResult>(listFiles)
