package com.pubnub.api.endpoints

import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.time]
 */
expect interface Time : PNFuture<PNTimeResult>
