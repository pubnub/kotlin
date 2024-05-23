package com.pubnub.api.endpoints.files

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNFileUrlResult

/**
 * @see [PubNub.getFileUrl]
 */
actual interface GetFileUrl : Endpoint<PNFileUrlResult>