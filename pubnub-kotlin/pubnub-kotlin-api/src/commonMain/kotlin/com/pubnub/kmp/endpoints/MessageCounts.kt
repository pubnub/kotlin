package com.pubnub.kmp.endpoints

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.history.PNMessageCountResult

/**
 * @see [PubNub.messageCounts]
 */
interface MessageCounts : PNFuture<PNMessageCountResult>