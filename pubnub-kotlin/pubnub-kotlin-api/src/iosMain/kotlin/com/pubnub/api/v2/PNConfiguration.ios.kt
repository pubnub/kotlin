package com.pubnub.api.v2

import com.pubnub.api.UserId

actual interface PNConfiguration {
    actual val userId: UserId
    actual val subscribeKey: String
    actual val publishKey: String
    actual val secretKey: String
    actual val authKey: String
    actual val cryptoModule: CryptoModule?
}

actual interface CryptoModule

actual fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String,
    publishKey: String,
    secretKey: String?
): PNConfiguration {
    return object : PNConfiguration {
        override val userId: UserId = userId
        override val subscribeKey: String = subscribeKey
        override val publishKey: String = publishKey
        override val secretKey: String
            get() = secretKey.orEmpty()
        override val authKey: String
            get() = TODO("Not yet implemented")
        override val cryptoModule: CryptoModule?
            get() = TODO("Not yet implemented")
    }
}