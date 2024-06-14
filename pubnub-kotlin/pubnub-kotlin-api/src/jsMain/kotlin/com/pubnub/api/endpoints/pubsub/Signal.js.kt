package com.pubnub.api.endpoints.pubsub

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.PNPublishResult

/**
 * @see [PubNub.signal]
 */
actual interface Signal : PNFuture<PNPublishResult>

