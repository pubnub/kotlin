package com.pubnub.docs.entities
// https://www.pubnub.com/docs/sdks/kotlin/entities/channel-group#basic-usage

// snippet.channelGroup
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.entities.ChannelGroup

fun main() {
    println("PubNub ChannelGroup Entity Example")
    println("=================================")

    // 1. Configure PubNub
    val userId = UserId("channel-group-entity-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
        subscribeKey = "demo"
        logVerbosity = PNLogVerbosity.BODY // Enable debug logging
    }.build()

    // 2. Create PubNub instance
    val pubnub = PubNub.create(config)

    // 3. Create a ChannelGroup entity
    val groupName = "demo-entity-group"
    val channelGroup: ChannelGroup = pubnub.channelGroup(groupName)
    println("Created ChannelGroup entity for: ${channelGroup.name}")

    // Define channels to add to the group
    val channelMarketing = "chat-marketing"
    val channels = listOf(
        channelMarketing,
        "chat-engineering",
        "chat-support"
    )

    // 4. Add channels to the group
    println("\nAdding channels to group...")
    pubnub.addChannelsToChannelGroup(
        channelGroup = groupName,
        channels = channels
    ).async { result ->
        result.onSuccess {
            println("SUCCESS: Added channels to group '$groupName':")
            channels.forEach { println("- $it") }
        }.onFailure { exception ->
            println("ERROR: Failed to add channels to group")
            println("Error details: ${exception.message}")
        }
    }

    // 5. Define a subscription to the channel group
    val channelGroupSubscription = channelGroup.subscription()

    channelGroupSubscription.onMessage = { result ->
        println("Received message: ${result.message.asString}")
    }

    channelGroupSubscription.subscribe()
    Thread.sleep(2000)

    // 6. Publish a message to a channel in the group
    pubnub.publish(channelMarketing, "Important message").sync()


    // Wait for operation to complete
    Thread.sleep(2000)

    // Clean up
    pubnub.destroy()
    println("\nPubNub connection closed")
}
// snippet.end
