package com.pubnub.api.endpoints

import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.messageCounts]
 */
expect interface MessageCounts : PNFuture<PNMessageCountResult>
