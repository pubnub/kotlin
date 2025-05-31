package com.pubnub.docs.messagePersistence
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback#basic-usage

// snippet.fetchMessages
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.v2.PNConfiguration
import kotlin.math.min

/**
 * This example demonstrates how to fetch message history using the PubNub Kotlin SDK.
 */
fun main() {
    println("PubNub History Example")
    println("======================")

    // Configure PubNub
    val userId = UserId("history-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
        logVerbosity = PNLogVerbosity.BODY // Enable network calls logging
    }.build()

    // Create PubNub instance
    val pubnub = PubNub.create(config)

    // Channel to fetch history from
    val channelName = "demo-channel"

    // Publish 5 messages
    pubnub.channel(channelName).publish("Message 01").sync()
    pubnub.channel(channelName).publish("Message 02").sync()
    pubnub.channel(channelName).publish("Message 03").sync()
    pubnub.channel(channelName).publish("Message 04").sync()
    pubnub.channel(channelName).publish("Message 05").sync()
    pubnub.channel(channelName).publish("Message 06").sync()

    // 1. Basic history fetch - get the last 10 messages
    fetchBasicHistory(pubnub, channelName)

    // 2. Advanced history fetch with options
    fetchHistoryWithOptions(pubnub, channelName)

    // Clean up resources
    pubnub.deleteMessages(channels = listOf(channelName)).sync()
    pubnub.destroy()
}

/**
 * Basic fetch message history example
 */
fun fetchBasicHistory(pubnub: PubNub, channelName: String) {
    println("\n# Fetching basic message history from channel: $channelName")

    pubnub.fetchMessages(
        channels = listOf(channelName),
        page = PNBoundedPage(limit = 10)
    ).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: Retrieved message history")
            displayMessages(response, channelName)
        }.onFailure { exception ->
            println("ERROR: Failed to fetch history")
            println("Error details: ${exception.message}")
        }
    }

    // Wait for the operation to complete
    Thread.sleep(2000)
}

/**
 * Fetch message history with additional options
 */
fun fetchHistoryWithOptions(pubnub: PubNub, channelName: String) {
    println("\n# Fetching message history with options from channel: $channelName")

    // Get the last 25 messages, including metadata and message actions
    pubnub.fetchMessages(
        channels = listOf(channelName),
        page = PNBoundedPage(limit = 25),
        includeMessageActions = true,
        includeMessageType = true,
        includeMeta = true
    ).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: Retrieved message history with options")
            displayMessages(response, channelName)
        }.onFailure { exception ->
            println("ERROR: Failed to fetch history with options")
            println("Error details: ${exception.message}")
        }
    }

    // Wait for the operation to complete
    Thread.sleep(2000)
}

/**
 * Helper function to display message history results
 */
fun displayMessages(response: PNFetchMessagesResult, channelName: String) {
    val channelMessages = response.channels[channelName]

    if (channelMessages.isNullOrEmpty()) {
        println("No messages found for channel: $channelName")
        return
    }

    val messageCount = channelMessages.size
    println("Found $messageCount messages for channel: $channelName")

    // Display up to 5 most recent messages (or all if less than 5)
    val displayCount = min(5, messageCount)
    println("Showing the $displayCount most recent messages:")

    channelMessages.take(displayCount).forEach { message ->
        println("---------------------------------------")
        println("Timetoken: ${message.timetoken}")
        println("Message: ${message.message}")

        // If available, display metadata
        if (message.meta != null) {
            println("Metadata: ${message.meta}")
        }

        // If available, display message actions
        if (!message.actions.isNullOrEmpty()) {
            println("Message Actions: ${message.actions}")
        }
    }
    println("---------------------------------------")
}
// snippet.end
