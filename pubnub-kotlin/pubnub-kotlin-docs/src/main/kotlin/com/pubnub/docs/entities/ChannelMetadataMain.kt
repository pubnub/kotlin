package com.pubnub.docs.entities
// https://www.pubnub.com/docs/sdks/kotlin/entities/channel-metadata#basic-usage

// snippet.channelMetadata
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.entities.ChannelMetadata

fun main() {
    println("PubNub ChannelMetadata Entity Example")
    println("===================================")

    // 1. Configure PubNub
    val userId = UserId("channel-metadata-entity-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
        subscribeKey = "demo"
        logVerbosity = PNLogVerbosity.BODY // Enable debug logging of network calls
    }.build()

    // 2. Create PubNub instance
    val pubnub = PubNub.create(config)

    // 3. Create a Channel Metadata entity
    val channelMetadataName = "team-chat"
    val channelMetadata: ChannelMetadata = pubnub.channelMetadata(channelMetadataName)
    println("Created ChannelMetadata entity for: $channelMetadataName")
    channelMetadata.id

    // 4. Set channel metadata
    println("\nSetting channel metadata...")
    pubnub.setChannelMetadata(
        channel = channelMetadataName,
        name = "Team Chat",
        description = "Internal team communication channel",
        includeCustom = true,
        custom = mapOf(
            "type" to "private",
            "category" to "internal",
            "maxUsers" to 50
        )
    ).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: Channel metadata set")
            println("Channel ID: ${response.data?.id}")
            println("Name: ${response.data?.name}")
            println("Description: ${response.data?.description}")
            println("Custom: ${response.data?.custom}")
        }.onFailure { exception ->
            println("ERROR: Failed to set channel metadata")
            println("Error details: ${exception.message}")
        }
    }

    // 5. Define a subscription to the channel metadata
    val channelMetadataSubscription = channelMetadata.subscription()
    channelMetadataSubscription.onMessage = { result ->
        println("\nReceived message on channel metadata subscription: ${result.message.asString}")
    }

    channelMetadataSubscription.subscribe()
    Thread.sleep(2000)

    // 6. Publish a message to the channel
    pubnub.publish(channelMetadataName, "Some message").sync()


    // 7. Wait for operation to complete
    Thread.sleep(2000)


    // 8. Clean up
    pubnub.destroy()
    println("\nPubNub connection closed")
}
// snippet.end
