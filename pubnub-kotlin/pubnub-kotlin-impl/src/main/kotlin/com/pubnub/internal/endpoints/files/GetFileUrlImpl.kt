package com.pubnub.internal.endpoints.files

import com.pubnub.api.endpoints.files.GetFileUrl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.getFileUrl]
 */
class GetFileUrlImpl internal constructor(getFileUrl: GetFileUrlInterface) : GetFileUrlInterface by getFileUrl, GetFileUrl
