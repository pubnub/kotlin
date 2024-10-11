package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity

expect interface PNConfiguration {
    val userId: UserId
    val subscribeKey: String
    val publishKey: String
    val secretKey: String
    @Deprecated(message = "To set auth token use method pubnub.setToken(token)")
    val authKey: String
    val logVerbosity: PNLogVerbosity
}

@Deprecated(
    message = "The authKey parameter is deprecated. Use createPNConfiguration without authKey instead. To set auth token use method pubnub.setToken(token)",
    replaceWith = ReplaceWith(
        "createPNConfiguration(userId, subscribeKey, publishKey, secretKey, logVerbosity)"
    )
)
expect fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String,
    publishKey: String,
    secretKey: String? = null,
    authKey: String? = null,
    logVerbosity: PNLogVerbosity = PNLogVerbosity.NONE,
): PNConfiguration

expect fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String,
    publishKey: String,
    secretKey: String? = null,
    logVerbosity: PNLogVerbosity = PNLogVerbosity.NONE,
): PNConfiguration
