package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNListFilesResult

/**
 * @see [PubNub.listFiles]
 */
expect interface ListFiles : Endpoint<PNListFilesResult>