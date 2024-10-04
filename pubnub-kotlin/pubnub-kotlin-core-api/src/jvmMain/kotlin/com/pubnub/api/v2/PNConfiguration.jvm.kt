package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity

actual fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String,
    publishKey: String,
    secretKey: String?,
    authKey: String?,
    logVerbosity: PNLogVerbosity
): PNConfiguration {
    return PNConfiguration.builder(userId, subscribeKey) {
        this.publishKey = publishKey
        this.secretKey = secretKey.orEmpty()
        this.authKey = authKey.orEmpty()
        this.secretKey = secretKey.orEmpty()
        this.logVerbosity = logVerbosity
    }.build()
}
