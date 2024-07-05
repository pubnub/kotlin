package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNFileUrlResult

/**
 * @see [PubNub.getFileUrl]
 */
interface GetFileUrl : com.pubnub.kmp.endpoints.files.GetFileUrl, Endpoint<PNFileUrlResult>
