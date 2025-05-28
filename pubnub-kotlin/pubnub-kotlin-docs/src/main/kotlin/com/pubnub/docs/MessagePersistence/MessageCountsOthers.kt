package com.pubnub.docs.MessagePersistence

import com.pubnub.docs.SnippetBase
import java.util.concurrent.TimeUnit

class MessageCountsOthers : SnippetBase() {
    private fun messageCountsBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback#basic-usage-2

        val pubnub = createPubNub()

        // snippet.messageCountsBasic
        val lastHourTimetoken = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)

        pubnub.messageCounts(
            channels = listOf("news"),
            channelsTimetoken = listOf(lastHourTimetoken * 10_000L)
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }

    private fun messageCountsDifferentTimetokenForEachChannel() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback#other-examples-2

        val pubnub = createPubNub()

        // snippet.messageCountsDifferentTimetokenForEachChannel
        val lastHourTimetoken = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)
        val lastDayTimetoken = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)

        pubnub.messageCounts(
            channels = listOf("news", "info"),
            channelsTimetoken = listOf(lastHourTimetoken, lastDayTimetoken).map { it * 10_000L }
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { pmMessageCountResult ->
                pmMessageCountResult.channels.forEach { (channel, count) ->
                    println("$count new messages on $channel")
                }
            }
        }
        // snippet.end
    }
}
