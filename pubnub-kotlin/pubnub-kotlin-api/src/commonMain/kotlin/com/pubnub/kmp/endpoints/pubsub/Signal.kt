package com.pubnub.kmp.endpoints.pubsub

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.PNPublishResult

/**
 * @see [PubNub.signal]
 */
interface Signal : PNFuture<PNPublishResult>