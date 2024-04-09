package com.pubnub.internal.endpoints.files

import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.api.models.consumer.files.PNDeleteFileResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.deleteFile]
 */
class DeleteFileImpl internal constructor(deleteFile: DeleteFileInterface) :
    DeleteFileInterface by deleteFile,
    DeleteFile,
    EndpointImpl<PNDeleteFileResult?>(deleteFile)
