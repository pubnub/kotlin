package com.pubnub.api.endpoints

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.history.PNMessageCountResult

/**
 * @see [PubNub.messageCounts]
 */
actual interface MessageCounts : Endpoint<PNMessageCountResult>