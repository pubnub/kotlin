@file:Suppress("ktlint")

package com.pubnub.docs.messagePersistence.samples.fetchMessagesWithPaging

// snippet.fetchMessagesWithPaging

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.v2.PNConfiguration

fun main() {
    val timetokenOfLastMessage = 17474884096044353
    getAllMessages(listOf("kotlin-ch1", "kotlin-ch2"), start = timetokenOfLastMessage + 1) { result ->
        result.channels.forEach { (channel, messages) ->
            println("Channel $channel")
            messages.forEach { item ->
                println(item)
            }
        }
    }
}

private fun getAllMessages(
    channels: List<String>,
    start: Long,
    callback: (result: PNFetchMessagesResult) -> Unit
) {
    val userId = UserId("history-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(this, "open")
        // snippet.show
    }.build()

    // Create PubNub instance
    val pubnub = PubNub.create(config)
    // snippet.hide
    com.pubnub.docs.support.LiveClients.trackCurrent(pubnub)
    // snippet.show

    pubnub.fetchMessages(
        channels = channels,
        page = PNBoundedPage(
            limit = 25,
            start = start
        ),
    ).async { result ->
        result.onSuccess { value ->
            if (value.channels.isNotEmpty()) {
                callback.invoke(value)

                var timetoken: Long = start
                value.channels.values.forEach { it: List<PNFetchMessageItem> ->
                    it.forEach { message: PNFetchMessageItem ->
                        if (message.timetoken!! < timetoken) {
                            timetoken = message.timetoken!!
                        }
                    }
                }
                getAllMessages(channels, timetoken, callback)
            }
        }
    }
}
// snippet.end
