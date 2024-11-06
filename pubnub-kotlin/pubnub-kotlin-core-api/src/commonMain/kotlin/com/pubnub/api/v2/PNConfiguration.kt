package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity

expect interface PNConfiguration {
    val userId: UserId
    val subscribeKey: String
    val publishKey: String

    /**
     * The secretKey should only be used within a server side and never exposed to client devices.
     */
    val secretKey: String
    val logVerbosity: PNLogVerbosity

    @Deprecated(
        message = "This setting is deprecated because it relates to deprecated Access Manager (PAM V2) and will be removed in the future. " +
            "Please, migrate to new Access Manager (PAM V3) https://www.pubnub.com/docs/general/resources/migration-guides/pam-v3-migration",
        level = DeprecationLevel.WARNING
    )
    val authKey: String

    /**
     * Authentication token for the PubNub client. This token is required on the client side when Access Manager (PAM) is enabled for PubNub keys.
     * It can be generated using the [PubNub.grantToken] method, which should be executed on the server side with a PubNub instance initialized using the secret key.
     */
    val authToken: String?
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
    authToken: String? = null
): PNConfiguration
