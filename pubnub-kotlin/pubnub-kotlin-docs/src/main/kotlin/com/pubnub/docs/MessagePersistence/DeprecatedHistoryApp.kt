package com.pubnub.docs.MessagePersistence
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback#history-paging-example

// snippet.historyPagingExampleDeprecated
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.history.PNHistoryResult

fun main() {
    getAllMessages("my_channel", start = 0L, count = 10) { result ->
        result.messages.forEach {
            println(it.entry)
        }
    }
}

/**
 * Fetches channel history in a recursive manner, in chunks of specified size, starting from the most recent,
 * with every subset (with predefined size) sorted by the timestamp the messages were published.
 *
 * @param channel  The channel where to fetch history from
 * @param start    The timetoken which the fetching starts from
 * @param count    Chunk size
 * @param callback Callback which fires when a chunk is fetched
 */
private fun getAllMessages(
    channel: String,
    start: Long,
    count: Int,
    callback: (result: PNHistoryResult) -> Unit
) {
    val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
        publishKey = "demo"
    }
    val pubnub = PubNub.create(configBuilder.build())

    pubnub.history(
        channel = channel,
        start = start,
        count = count,
        includeTimetoken = true
    ).async { result ->
        result.onSuccess { value ->
            if (value.messages.isNotEmpty()) {
                callback.invoke(value)
                getAllMessages(channel, value.messages.first().timetoken!!, count, callback)
            }
        }
    }
}
// snippet.end
