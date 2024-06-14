package com.pubnub.api.endpoints

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.history.PNMessageCountResult

/**
 * @see [PubNub.messageCounts]
 */
actual interface MessageCounts : PNFuture<PNMessageCountResult>