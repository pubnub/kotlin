package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity

private const val NO_AUTH_KEY = ""

actual interface PNConfiguration {
    actual val userId: UserId
    actual val subscribeKey: String
    actual val publishKey: String
    actual val secretKey: String
    @Deprecated(message = "To set auth token use method pubnub.setToken(token)")
    actual val authKey: String
    actual val logVerbosity: PNLogVerbosity
}

@Deprecated(
    message = "The authKey parameter is deprecated. Use createPNConfiguration without authKey instead. " +
            "To set auth token use method pubnub.setToken(token).",
    replaceWith = ReplaceWith(
        "createPNConfiguration(userId, subscribeKey, publishKey, secretKey, logVerbosity)"
    )
)
actual fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String,
    publishKey: String,
    secretKey: String?,
    authKey: String?,
    logVerbosity: PNLogVerbosity
): PNConfiguration {
    return object : PNConfiguration {
        override val userId: UserId = userId
        override val subscribeKey: String = subscribeKey
        override val publishKey: String = publishKey
        override val secretKey: String
            get() = secretKey.orEmpty()
        override val authKey: String
            get() = authKey.orEmpty()
        override val logVerbosity: PNLogVerbosity
            get() = logVerbosity
    }
}

actual fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String,
    publishKey: String,
    secretKey: String?,
    logVerbosity: PNLogVerbosity
): PNConfiguration {
    return object : PNConfiguration {
        override val userId: UserId = userId
        override val subscribeKey: String = subscribeKey
        override val publishKey: String = publishKey
        override val secretKey: String
            get() = secretKey.orEmpty()
        override val authKey: String
            get() = NO_AUTH_KEY
        override val logVerbosity: PNLogVerbosity
            get() = logVerbosity
    }
}
