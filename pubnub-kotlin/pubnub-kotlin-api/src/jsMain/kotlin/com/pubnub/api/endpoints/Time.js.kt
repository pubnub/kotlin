package com.pubnub.api.endpoints

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.PNTimeResult

/**
 * @see [PubNub.time]
 */
actual interface Time : PNFuture<PNTimeResult>