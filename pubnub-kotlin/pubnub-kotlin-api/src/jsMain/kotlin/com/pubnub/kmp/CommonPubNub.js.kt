package com.pubnub.kmp

import com.pubnub.api.v2.PNConfiguration

actual fun createCommonPubNub(config: PNConfiguration): CommonPubNub {
    return PubNub(config)
}