package com.pubnub.docs.publishAndSubscribe.publish
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage
// snippet.publishMain
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.PNConfiguration

fun main() {
    println("PubNub Publish Example")
    println("======================")

    // Configure PubNub
    val userId = UserId("publish-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
    }.build()

    // Create PubNub instance
    val pubnub = PubNub.create(config)

    // Channel to publish to
    val channelName = "demo-channel"

    // 1. Publish a simple string message
    publishSimpleMessage(pubnub, channelName)

    // 2. Publish a JSON object
    publishJsonObject(pubnub, channelName)

    // Clean up resources
    pubnub.destroy()
}

/**
 * Publishes a simple string message to a channel
 */
private fun publishSimpleMessage(pubnub: PubNub, channelName: String) {
    println("\n# Publishing simple message to channel: $channelName")

    val message = "Hello from PubNub Kotlin SDK!"

    val channel = pubnub.channel(channelName)
    channel.publish(message).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: Message published")
            println("Timetoken: ${response.timetoken}")
        }.onFailure { exception ->
            println("ERROR: Failed to publish message")
            println("Error details: ${exception.message}")
        }
    }

    // Wait for the operation to complete
    Thread.sleep(2000)
}

/**
 * Publishes a JSON object to a channel
 */
private fun publishJsonObject(pubnub: PubNub, channelName: String) {
    println("\n# Publishing JSON object to channel: $channelName")

    // Create a map to represent a JSON object
    val message = mapOf(
        "title" to "Message from Kotlin",
        "content" to "This is a JSON message",
        "timestamp" to System.currentTimeMillis(),
        "sender" to "publish-demo-user"
    )

    val channel = pubnub.channel(channelName)
    channel.publish(message).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: JSON object published")
            println("Timetoken: ${response.timetoken}")
            println("Message: $message")
        }.onFailure { exception ->
            println("ERROR: Failed to publish JSON object")
            println("Error details: ${exception.message}")
        }
    }

    // Wait for the operation to complete
    Thread.sleep(2000)
}
// snippet.end
