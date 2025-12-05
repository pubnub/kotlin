package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.enums.LogLevel
import com.pubnub.api.enums.PNLogVerbosity

private const val NO_AUTH_KEY = ""
private const val NO_AUTH_TOKEN = ""

actual interface PNConfiguration {
    actual val userId: UserId
    actual val subscribeKey: String
    actual val publishKey: String
    actual val secretKey: String
    val logLevel: LogLevel
    val enableEventEngine: Boolean

    @Deprecated(
        message = "This setting is deprecated because it relates to deprecated Access Manager (PAM V2) and will be removed in the future. " +
            "Please, migrate to new Access Manager (PAM V3) https://www.pubnub.com/docs/general/resources/migration-guides/pam-v3-migration ",
        level = DeprecationLevel.WARNING
    )
    actual val authKey: String

    /**
     * Authentication token for the PubNub client. This token is required on the client side when Access Manager (PAM) is enabled for PubNub keys.
     * It can be generated using the [PubNub.grantToken] method, which should be executed on the server side with a PubNub instance initialized using the secret key.
     */
    actual val authToken: String?
}

@Deprecated(
    message = "The authKey parameter is deprecated because it relates to deprecated Access Manager (PAM V2) and will be removed in the future." +
        "Please, use createPNConfiguration without authKey instead and migrate to new Access Manager " +
        "(PAM V3) https://www.pubnub.com/docs/general/resources/migration-guides/pam-v3-migration ",
    level = DeprecationLevel.WARNING,
    replaceWith = ReplaceWith(
        "createPNConfiguration(userId, subscribeKey, publishKey, secretKey, logLevel, authToken)"
    ),
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
        override val userId: UserId
            get() = userId
        override val subscribeKey: String
            get() = subscribeKey
        override val publishKey: String
            get() = publishKey
        override val secretKey: String
            get() = secretKey.orEmpty()
        override val authKey: String
            get() = authKey.orEmpty()
        override val authToken: String?
            get() = null
        override val enableEventEngine: Boolean
            get() = true
        override val logLevel: LogLevel
            get() = LogLevel.NONE
    }
}

actual fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String,
    publishKey: String,
    secretKey: String?,
    logLevel: LogLevel,
    authToken: String?
): PNConfiguration {
    return object : PNConfiguration {
        override val userId: UserId
            get() = userId
        override val subscribeKey: String
            get() = subscribeKey
        override val publishKey: String
            get() = publishKey
        override val secretKey: String
            get() = secretKey.orEmpty()
        override val authKey: String
            get() = NO_AUTH_KEY
        override val authToken: String?
            get() = authToken
        override val enableEventEngine: Boolean
            get() = true
        override val logLevel: LogLevel
            get() = logLevel
    }
}
