package com.pubnub.api.endpoints.pubsub

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.PNPublishResult

/**
 * @see [PubNub.publish]
 */
actual interface Publish : PNFuture<PNPublishResult>
