package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult

/**
 * @see [com.pubnub.api.PubNub.deleteMessages]
 */
expect interface DeleteMessages : Endpoint<PNDeleteMessagesResult>