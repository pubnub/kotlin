package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.signal]
 */
actual interface Signal : PNFuture<PNPublishResult>
