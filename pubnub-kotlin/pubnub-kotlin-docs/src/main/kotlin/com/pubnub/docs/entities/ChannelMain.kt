package com.pubnub.docs.entities
// https://www.pubnub.com/docs/sdks/kotlin/entities/channel#basic-usage

// snippet.channelMain
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.enums.PNLogVerbosity

fun main() {
    println("PubNub Channel Entity Example")
    println("============================")

    // 1. Configure PubNub
    val userId = UserId("channel-entity-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
        subscribeKey = "demo"
        logVerbosity = PNLogVerbosity.BODY // Enable debug logging of network calls
    }.build()

    // 2. Create PubNub instance
    val pubnub = PubNub.create(config)

    // 3. Create a channel entity
    val channelName = "demo-entity-channel"
    val channel = pubnub.channel(channelName)
    println("Created Channel entity for: $channelName.")

    // 4. Publish a message via the channel entity
    println("\nPublishing message via Channel entity...")
    channel.publish(
        message = mapOf(
            "text" to "Hello from Channel entity!",
            "sender" to "channel-entity-demo-user",
            "timestamp" to System.currentTimeMillis()
        )
    ).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: Message published via Channel entity")
            println("Timetoken: ${response.timetoken}")
        }.onFailure { exception ->
            println("ERROR: Failed to publish message")
            println("Error details: ${exception.message}")
        }
    }

    // Wait for the publish operation to complete
    Thread.sleep(2000)

    // Clean up
    pubnub.destroy()
    println("\nPubNub connection closed")
}
// snippet.end
