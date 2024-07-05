package com.pubnub.kmp.endpoints

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.PNTimeResult

/**
 * @see [PubNub.time]
 */
interface Time : PNFuture<PNTimeResult>