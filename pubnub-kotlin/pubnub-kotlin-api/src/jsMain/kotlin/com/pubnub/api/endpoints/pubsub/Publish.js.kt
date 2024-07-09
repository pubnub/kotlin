package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.publish]
 */
actual interface Publish : PNFuture<PNPublishResult>
