package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.files.PNFileUrlResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.files.GetFileUrl
import com.pubnub.internal.endpoints.files.IGetFileUrl

/**
 * @see [PubNub.getFileUrl]
 */
class GetFileUrl internal constructor(private val getFileUrl: GetFileUrl) : DelegatingEndpoint<PNFileUrlResult>(), IGetFileUrl by getFileUrl {
    override fun createAction(): Endpoint<PNFileUrlResult> = getFileUrl.mapIdentity()
}