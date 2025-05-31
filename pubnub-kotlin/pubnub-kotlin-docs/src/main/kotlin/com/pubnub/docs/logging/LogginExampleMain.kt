package com.pubnub.docs.logging
// https://www.pubnub.com/docs/sdks/kotlin/logging#how-to-enable-logging

// snippet.loggingExampleMain
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.v2.PNConfiguration

fun main() {
    println("PubNub Logging Example")
    println("======================")

    // Configure PubNub with logging enabled
    val userId = UserId("loggingDemoUser")
    val pnConfiguration = PNConfiguration.builder(userId, "demo").apply {
        // Replace "demo" with your Subscribe Key from the PubNub Admin Portal
        publishKey = "demo" // Replace with your Publish Key from the PubNub Admin Portal

        // Set log verbosity - configure how much detail to log
        // Options:
        // - PNLogVerbosity.NONE (Turn off logging)
        // - PNLogVerbosity.BODY (Log request and response bodies - most detailed)
        logVerbosity = PNLogVerbosity.BODY
    }.build()

    println("Logging configured with level: ${pnConfiguration.logVerbosity}")

    // Initialize PubNub with the configuration
    val pubnub = PubNub.create(pnConfiguration)
    println("PubNub initialized with logging enabled")

    // Perform operations to generate logs
    demonstrateLogging(pubnub)

    // Clean up resources
    pubnub.destroy()
}

/**
 * Perform operations to generate logs
 */
fun demonstrateLogging(pubnub: PubNub) {
    println("\n# Performing operations to generate logs")

    try {
        // Time operation - simple API call that will generate logs
        println("1. Fetching PubNub time...")
        val timeResult = pubnub.time().sync()
        println(" Time result: ${timeResult.timetoken}")

        // Publish operation - will generate more detailed logs
        println("2. Publishing a message...")
        val channel = "logging-demo-channel"
        val message = mapOf(
            "text" to "Hello from PubNub Kotlin SDK!",
            "timestamp" to System.currentTimeMillis()
        )

        val publishResult = pubnub.publish(
            channel = channel,
            message = message
        ).sync()

        println("   Publish result: Timetoken=${publishResult.timetoken}")

        // Subscribe to a channel - will generate continuous logs
        println("3. Subscribing to channel...")
        pubnub.subscribe(
            channels = listOf(channel)
        )

        // Give subscription time to establish and generate logs
        println("   Subscription started - check logs for details")
        println("   Waiting 5 seconds to generate subscription logs...")
        Thread.sleep(5000)

        // Unsubscribe
        pubnub.unsubscribeAll()
        println("   Unsubscribed from all channels")
    } catch (e: Exception) {
        println("ERROR: ${e.message}")
    }

    println("\nLogging demonstration complete")
    println("Check your console for detailed logs based on the configured verbosity level")
}
// snippet.end
