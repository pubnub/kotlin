package com.pubnub.internal.kotlin.endpoints.files

import com.pubnub.api.endpoints.files.GetFileUrl
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.files.IGetFileUrl

/**
 * @see [PubNubImpl.getFileUrl]
 */
class GetFileUrlImpl internal constructor(getFileUrl: IGetFileUrl) : IGetFileUrl by getFileUrl, GetFileUrl
