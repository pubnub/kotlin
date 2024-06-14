package com.pubnub.api.endpoints

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.PNTimeResult

/**
 * @see [PubNub.time]
 */
expect interface Time : PNFuture<PNTimeResult>