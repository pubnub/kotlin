package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity

private const val NO_AUTH_KEY = ""

actual interface PNConfiguration {
    actual val userId: UserId
    actual val subscribeKey: String
    actual val publishKey: String
    actual val secretKey: String
    actual val logVerbosity: PNLogVerbosity
    val enableEventEngine: Boolean

    @Deprecated(
        message = "This setting is deprecated because it relates to deprecated Access Manager (PAM V2) and will be removed in the future. " +
            "Please, migrate to new Access Manager (PAM V3) https://www.pubnub.com/docs/general/resources/migration-guides/pam-v3-migration ",
        level = DeprecationLevel.WARNING
    )
    actual val authKey: String
}

@Deprecated(
    message = "The authKey parameter is deprecated because it relates to deprecated Access Manager (PAM V2) and will be removed in the future." +
        "Please, use createPNConfiguration without authKey instead and migrate to new Access Manager " +
        "(PAM V3) https://www.pubnub.com/docs/general/resources/migration-guides/pam-v3-migration ",
    level = DeprecationLevel.WARNING,
    replaceWith = ReplaceWith(
        "createPNConfiguration(userId, subscribeKey, publishKey, secretKey, logVerbosity)"
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
        override val enableEventEngine: Boolean
            get() = false
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
        override val enableEventEngine: Boolean
            get() = true
        override val logVerbosity: PNLogVerbosity
            get() = logVerbosity
    }
}
