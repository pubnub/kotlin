package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.internal.PubNubImpl
import com.pubnub.api.models.consumer.files.PNFileUrlResult
import com.pubnub.internal.endpoints.files.IGetFileUrl

/**
 * @see [PubNubImpl.getFileUrl]
 */
class GetFileUrl internal constructor(getFileUrl: IGetFileUrl) : Endpoint<PNFileUrlResult>(), IGetFileUrl by getFileUrl
