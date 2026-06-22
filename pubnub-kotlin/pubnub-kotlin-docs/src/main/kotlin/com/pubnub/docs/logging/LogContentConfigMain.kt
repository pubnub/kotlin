package com.pubnub.docs.logging
// https://www.pubnub.com/docs/sdks/kotlin/logging#log-content-configuration

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.logging.LogContentConfig
import com.pubnub.api.v2.PNConfiguration

/**
 * Demonstrates LogContentConfig usage for controlling what gets logged in PubNub SDK.
 * Shows three different configurations: custom byte limits, suppressed message content, and unlimited HTTP responses.
 */
fun main() {
    // snippet.logContentConfigCustom
    val config = PNConfiguration.builder(UserId("demoUserId"), "demo") {
        publishKey = "demo"
        logContentConfig = LogContentConfig(
            loggedMessageContentMaxBytes = 1000,
            loggedHttpResponseMaxBytes = 4000,
        )
    }.build()

    val pubnub = PubNub.create(config)
    // snippet.end
    pubnub.close()

    // snippet.logContentConfigSuppressMessages
    val configSuppressMessages = PNConfiguration.builder(UserId("demoUserId"), "demo") {
        publishKey = "demo"
        logContentConfig = LogContentConfig(
            loggedMessageContentMaxBytes = 0,
        )
    }.build()

    val pubnubSuppressMessages = PubNub.create(configSuppressMessages)
    // snippet.end
    pubnubSuppressMessages.close()

    // snippet.logContentConfigUnlimitedResponse
    val configUnlimitedResponse = PNConfiguration.builder(UserId("demoUserId"), "demo") {
        publishKey = "demo"
        logContentConfig = LogContentConfig(
            loggedHttpResponseMaxBytes = Int.MAX_VALUE,
        )
    }.build()

    val pubnubUnlimitedResponse = PubNub.create(configUnlimitedResponse)
    // snippet.end
    pubnubUnlimitedResponse.close()
}
