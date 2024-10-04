package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity

expect interface PNConfiguration {
    val userId: UserId
    val subscribeKey: String
    val publishKey: String
    val secretKey: String
    val authKey: String
    val logVerbosity: PNLogVerbosity
}

expect fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String,
    publishKey: String,
    secretKey: String? = null,
    authKey: String? = null,
    logVerbosity: PNLogVerbosity = PNLogVerbosity.NONE,
): PNConfiguration
