package com.pubnub.api.endpoints

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.history.PNMessageCountResult

/**
 * @see [PubNub.messageCounts]
 */
expect interface MessageCounts : PNFuture<PNMessageCountResult>