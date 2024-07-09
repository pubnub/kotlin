package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNLogVerbosity

actual fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String,
    publishKey: String,
    secretKey: String?,
    logVerbosity: Boolean
): PNConfiguration {
    return PNConfiguration.builder(userId, subscribeKey) {
        this.publishKey = publishKey
        this.secretKey = secretKey.orEmpty()
        this.logVerbosity = if (logVerbosity) {
            PNLogVerbosity.BODY
        } else {
            PNLogVerbosity.NONE
        }
    }.build()
}

actual typealias CryptoModule = CryptoModule
