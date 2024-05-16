package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult

/**
 * @see [com.pubnub.api.PubNub.deleteMessages]
 */
actual interface DeleteMessages : Endpoint<PNDeleteMessagesResult>