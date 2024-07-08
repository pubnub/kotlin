package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.signal]
 */
expect interface Signal : PNFuture<PNPublishResult>
