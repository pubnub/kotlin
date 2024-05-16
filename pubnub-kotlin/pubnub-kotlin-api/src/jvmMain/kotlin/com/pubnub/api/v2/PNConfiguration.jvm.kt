package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule

actual fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String
): PNConfiguration {
    return PNConfiguration.builder(userId, subscribeKey).build()
}

actual typealias CryptoModule = CryptoModule