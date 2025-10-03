package com.pubnub.docs.logging.customLogger

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.PNConfiguration

fun main() {
    // snippet.customLoggerConfiguration

    // Configure SDK with custom logger
    val config = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
        customLoggers = listOf(MonitoringLogger())
    }.build()

    val pubnub = PubNub.create(config)

    // snippet.end
}
