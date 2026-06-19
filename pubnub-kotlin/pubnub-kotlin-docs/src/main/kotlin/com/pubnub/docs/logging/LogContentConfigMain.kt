package com.pubnub.docs.logging
// https://www.pubnub.com/docs/sdks/kotlin/logging#log-content-configuration

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.logging.LogContentConfig
import com.pubnub.api.v2.PNConfiguration

fun main() {
    // snippet.logContentConfigDefault
    val config = PNConfiguration.builder(UserId("demoUserId"), "demo") {
        publishKey = "demo"
        logContentConfig = LogContentConfig(
            loggedMessageContentMaxBytes = 1000,
            loggedHttpResponseMaxBytes = 4000,
        )
    }.build()

    val pubnub = PubNub.create(config)
    // snippet.end

    // snippet.logContentConfigSuppressMessages
    val configSuppressMessages = PNConfiguration.builder(UserId("demoUserId"), "demo") {
        publishKey = "demo"
        logContentConfig = LogContentConfig(
            loggedMessageContentMaxBytes = 0,
        )
    }.build()

    val pubnubSuppressMessages = PubNub.create(configSuppressMessages)
    // snippet.end

    // snippet.logContentConfigUnlimitedResponse
    val configUnlimitedResponse = PNConfiguration.builder(UserId("demoUserId"), "demo") {
        publishKey = "demo"
        logContentConfig = LogContentConfig(
            loggedHttpResponseMaxBytes = Int.MAX_VALUE,
        )
    }.build()

    val pubnubUnlimitedResponse = PubNub.create(configUnlimitedResponse)
    // snippet.end
}
