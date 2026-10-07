package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.v2.PNConfiguration

class DataSyncChannelsMain {
    private fun createChannelBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#create-channel

        // snippet.createChannelBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.createChannel(
            channelId = "channel-summer-sale",
            classVersion = 1,
            payload = mapOf("name" to "Summer Sale", "type" to "promotion")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Created channel ${value.data.id} with eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun getChannelBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#get-channel

        // snippet.getChannelBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.getChannel("channel-summer-sale").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Fetched channel ${value.data.id} with payload ${value.data.payload}")
            }
        }
        // snippet.end
    }

    private fun getChannelsBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#get-channels

        // snippet.getChannelsBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.getChannels(
            limit = 10
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end
    }

    private fun setChannelBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#set-channel

        // snippet.setChannelBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.setChannel(
            channelId = "channel-summer-sale",
            classVersion = 1,
            payload = mapOf("name" to "Summer Sale 2026", "type" to "promotion")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Replaced channel ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun updateChannelBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#update-channel

        // snippet.updateChannelBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.updateChannel(
            channelId = "channel-summer-sale",
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/payload/name", value = "Summer Sale 2026")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Updated channel ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun removeChannelBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#remove-channel

        // snippet.removeChannelBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.removeChannel("channel-summer-sale").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Removed channel, HTTP status ${value.status}")
            }
        }
        // snippet.end
    }
}
