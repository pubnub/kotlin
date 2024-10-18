package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity

private const val NO_AUTH_KEY = ""

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
    return PNConfiguration.builder(userId, subscribeKey) {
        this.publishKey = publishKey
        this.secretKey = secretKey.orEmpty()
        this.authKey = authKey.orEmpty()
        this.secretKey = secretKey.orEmpty()
        this.logVerbosity = logVerbosity
    }.build()
}

actual fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String,
    publishKey: String,
    secretKey: String?,
    logVerbosity: PNLogVerbosity
): PNConfiguration {
    return PNConfiguration.builder(userId, subscribeKey) {
        this.publishKey = publishKey
        this.secretKey = secretKey.orEmpty()
        this.authKey = NO_AUTH_KEY
        this.secretKey = secretKey.orEmpty()
        this.logVerbosity = logVerbosity
    }.build()
}
