package com.pubnub.api.v2

import com.pubnub.api.UserId

actual fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String
): PNConfiguration {
    return PNConfiguration.builder(userId, subscribeKey).build()
}