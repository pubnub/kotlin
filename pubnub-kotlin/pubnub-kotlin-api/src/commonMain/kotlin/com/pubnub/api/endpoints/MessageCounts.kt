package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.history.PNMessageCountResult

/**
 * @see [PubNub.messageCounts]
 */
expect interface MessageCounts : Endpoint<PNMessageCountResult>