package com.pubnub.kmp.endpoints.pubsub

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.PNPublishResult

/**
 * @see [PubNub.publish]
 */
interface Publish : PNFuture<PNPublishResult>