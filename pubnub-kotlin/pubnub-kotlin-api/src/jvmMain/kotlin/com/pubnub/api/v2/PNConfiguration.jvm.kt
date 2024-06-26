package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule

actual fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String,
    publishKey: String,
    secretKey: String?
): PNConfiguration {
    return PNConfiguration.builder(userId, subscribeKey) {
        this.publishKey = publishKey
        this.secretKey = secretKey.orEmpty()
    }.build()
}

actual typealias CryptoModule = CryptoModule