package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNFileUrlResult

/**
 * @see [PubNub.getFileUrl]
 */
expect interface GetFileUrl : Endpoint<PNFileUrlResult>