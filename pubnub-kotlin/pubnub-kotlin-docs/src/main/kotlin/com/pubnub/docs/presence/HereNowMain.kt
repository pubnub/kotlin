package com.pubnub.docs.presence
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence#get-a-list-of-uuids-subscribed-to-channel
// snippet.hereNowMain

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.v2.PNConfiguration

fun main() {
    println("PubNub hereNow() Example")
    println("========================")

    // Configure PubNub
    val userId = UserId("here-now-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
    }.build()

    // Create PubNub instance
    val pubnub = PubNub.create(config)

    // Example channels to check presence for
    val channels = listOf("demo-channel-1", "demo-channel-2")

    // Basic hereNow for a single channel
    singleChannelHereNow(pubnub, channels[0])

    // hereNow for multiple channels
    multipleChannelsHereNow(pubnub, channels)

    // Advanced hereNow with additional options
    advancedHereNow(pubnub, channels[0])

    // Clean up resources
    pubnub.destroy()
}

/**
 * Demonstrates basic usage of hereNow for a single channel with pagination support
 */
fun singleChannelHereNow(pubnub: PubNub, channel: String) {
    println("\n# Basic hereNow for single channel: $channel")

    pubnub.hereNow(
        channels = listOf(channel),
        limit = 100,
    ).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: Retrieved presence information")
            printChannelData(channel, response)

            // Check if more results are available
            if (response.nextOffset != null && response.nextOffset != 0) {
                println("\nMore occupants available. Fetching next page...")

                // Fetch next page using the offset from previous response
                pubnub.hereNow(
                    channels = listOf(channel),
                    limit = 100,
                    offset = response.nextOffset
                ).async { nextResult ->
                    nextResult.onSuccess { nextResponse ->
                        println("\nNext Page Results:")
                        printChannelData(channel, nextResponse)

                        // Continue pagination if needed by checking nextResponse.nextOffset
                    }.onFailure { exception ->
                        println("ERROR: Failed to get next page of presence information")
                        println("Error details: ${exception.message}")
                    }
                }
            }
        }.onFailure { exception ->
            println("ERROR: Failed to get presence information")
            println("Error details: ${exception.message}")
        }
    }

    // Wait for the operation to complete
    Thread.sleep(2000)
}

/**
 * Demonstrates using hereNow for multiple channels
 */
fun multipleChannelsHereNow(pubnub: PubNub, channels: List<String>) {
    println("\n# hereNow for multiple channels: $channels")

    pubnub.hereNow(
        channels = channels
    ).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: Retrieved presence information for multiple channels")
            println("Total Occupancy: ${response.totalOccupancy}")
            println("Total Channels: ${response.totalChannels}")

            // Iterate through all channels in the response
            response.channels.forEach { (channelName, channelData) ->
                println("\nChannel: $channelName")
                println("Occupancy: ${channelData.occupancy}")

                if (channelData.occupants.isNotEmpty()) {
                    println("UUIDs present: ${channelData.occupants.map { it.uuid }}")
                } else {
                    println("No users present")
                }
            }
        }.onFailure { exception ->
            println("ERROR: Failed to get presence information")
            println("Error details: ${exception.message}")
        }
    }

    // Wait for the operation to complete
    Thread.sleep(2000)
}

/**
 * Demonstrates advanced usage of hereNow with additional options
 */
fun advancedHereNow(pubnub: PubNub, channel: String) {
    println("\n# Advanced hereNow with additional options: $channel")

    pubnub.hereNow(
        channels = listOf(channel),
        includeUUIDs = true, // Include the UUIDs of users
        includeState = true // Include state information for users
    ).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: Retrieved detailed presence information")

            val channelData = response.channels[channel]

            if (channelData != null) {
                println("Channel: $channel")
                println("Occupancy: ${channelData.occupancy}")

                if (channelData.occupants.isNotEmpty()) {
                    println("\nPresent users:")
                    channelData.occupants.forEach { occupant ->
                        println("UUID: ${occupant.uuid}")

                        // Display state information if available
                        if (occupant.state != null) {
                            println("State: ${occupant.state}")
                        }
                    }
                } else {
                    println("No users present")
                }
            } else {
                println("No presence data for channel: $channel")
            }
        }.onFailure { exception ->
            println("ERROR: Failed to get detailed presence information")
            println("Error details: ${exception.message}")
        }
    }

    // Wait for the operation to complete
    Thread.sleep(2000)
}

/**
 * Helper function to print channel presence data
 */
private fun printChannelData(channel: String, response: PNHereNowResult) {
    val channelData = response.channels[channel]

    if (channelData != null) {
        println("Channel: $channel")
        println("Occupancy: ${channelData.occupancy}")
        println("UUIDs: ${channelData.occupants.map { it.uuid }}")

        if (response.nextOffset != null && response.nextOffset != 0) {
            println("Next Offset: ${response.nextOffset}")
        }
    } else {
        println("No presence data for channel: $channel")
    }
}
// snippet.end
