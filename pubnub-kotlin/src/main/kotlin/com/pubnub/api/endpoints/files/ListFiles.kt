package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.files.IListFiles
import com.pubnub.internal.endpoints.files.ListFiles

/**
 * @see [PubNub.listFiles]
 */
class ListFiles internal constructor(private val listFiles: ListFiles) : DelegatingEndpoint<PNListFilesResult>(), IListFiles by listFiles {
    override fun createAction(): Endpoint<PNListFilesResult> = listFiles.mapIdentity()
}