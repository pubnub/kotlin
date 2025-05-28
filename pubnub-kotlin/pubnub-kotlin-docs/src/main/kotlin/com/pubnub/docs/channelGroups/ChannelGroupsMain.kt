package com.pubnub.docs.channelGroups
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/channel-groups#basic-usage

// snippet.addChannelsToChannelGroupMain
import com.pubnub.api.PubNub
import com.pubnub.api.UserId

fun main() {
    // Configure PubNub with demo keys
    val config = com.pubnub.api.v2.PNConfiguration.builder(UserId("add-channels-demo"), "demo").apply {
        publishKey = "demo"
    }

    val pubnub = PubNub.create(config.build())

    // Set up channel names and channel group name
    val channelA = "demo-channel-a"
    val channelB = "demo-channel-b"
    val channelC = "demo-channel-c"
    val myChannelGroup = "demo-channel-group"

    // Add channels to a channel group
    println("Adding channels to channel group '$myChannelGroup'...")

    // The addChannelsToChannelGroup method adds channels to an existing or new channel group
    pubnub.addChannelsToChannelGroup(
        // The name of the channel group to add channels to
        channelGroup = myChannelGroup,
        // A list of channel names to add to the channel group
        channels = listOf(channelA, channelB, channelC)
    ).async { result ->
        result.onFailure { exception ->
            println("Failed to add channels to channel group: ${exception.message}")
            exception.printStackTrace()
        }.onSuccess {
            println("Successfully added channels to channel group '$myChannelGroup'")

            // Verify by listing the channels in the group
            pubnub.listChannelsForChannelGroup(channelGroup = myChannelGroup).async { listResult ->
                listResult.onFailure { listException ->
                    println("Failed to list channels: ${listException.message}")
                }.onSuccess { response ->
                    println("Channels in group '$myChannelGroup': ${response.channels.joinToString(", ")}")

                    // Clean up
                    cleanup(pubnub, myChannelGroup)
                }
            }
        }
    }

    // Keep the program running until the operations complete
    Thread.sleep(5000)
}

/**
 * Optional cleanup to remove the channel group after demonstration
 */
fun cleanup(pubnub: PubNub, channelGroup: String) {
    println("Cleaning up - Deleting channel group '$channelGroup'")
    pubnub.deleteChannelGroup(channelGroup = channelGroup).async { result ->
        result.onFailure { exception ->
            println("Failed to delete channel group: ${exception.message}")
        }.onSuccess {
            println("Successfully deleted channel group '$channelGroup'")
            pubnub.destroy()
        }
    }
}
// snippet.end
