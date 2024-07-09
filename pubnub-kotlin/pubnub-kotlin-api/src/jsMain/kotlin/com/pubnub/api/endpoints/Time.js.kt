package com.pubnub.api.endpoints

import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.time]
 */
actual interface Time : PNFuture<PNTimeResult>
