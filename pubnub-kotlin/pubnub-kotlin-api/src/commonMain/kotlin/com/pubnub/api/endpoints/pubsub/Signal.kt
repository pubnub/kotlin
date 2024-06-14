package com.pubnub.api.endpoints.pubsub

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.PNPublishResult

/**
 * @see [PubNub.signal]
 */
expect interface Signal : PNFuture<PNPublishResult>