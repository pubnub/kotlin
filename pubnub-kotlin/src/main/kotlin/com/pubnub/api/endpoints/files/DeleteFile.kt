package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.files.PNDeleteFileResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.files.DeleteFile
import com.pubnub.internal.endpoints.files.IDeleteFile

/**
 * @see [PubNub.deleteFile]
 */
class DeleteFile internal constructor(private val deleteFile: DeleteFile) :
    DelegatingEndpoint<PNDeleteFileResult?>(),
    IDeleteFile by deleteFile {
    override fun createAction(): Endpoint<PNDeleteFileResult?> = deleteFile.mapIdentity()
}
