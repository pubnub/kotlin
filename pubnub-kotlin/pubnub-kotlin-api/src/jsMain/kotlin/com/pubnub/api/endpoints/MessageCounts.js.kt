package com.pubnub.api.endpoints

import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.messageCounts]
 */
actual interface MessageCounts : PNFuture<PNMessageCountResult>
