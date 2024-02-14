package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.files.PNFileUrlResult
import com.pubnub.internal.endpoints.files.IGetFileUrl

/**
 * @see [PubNub.getFileUrl]
 */
class GetFileUrl internal constructor(getFileUrl: IGetFileUrl) : Endpoint<PNFileUrlResult>(), IGetFileUrl by getFileUrl
