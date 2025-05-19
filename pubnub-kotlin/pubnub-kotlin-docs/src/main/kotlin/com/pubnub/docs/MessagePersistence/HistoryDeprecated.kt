package com.pubnub.docs.MessagePersistence

import com.pubnub.docs.SnippetBase

class HistoryDeprecated : SnippetBase() {
    private fun retrieveLast100MessagesOnChannel() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback#other-examples-2

        val pubnub = createPubNub()

        // snippet.retrieveLast100MessagesOnChannel
        pubnub.history(
            channel = "history_channel", // where to fetch history from
            count = 100 // how many items to fetch
        ).async { result -> }
        // snippet.end
    }

    private fun historyThreeOldestMessages() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback#use-history-to-retrieve-the-three-oldest-messages-by-retrieving-from-the-time-line-in-reverse
        val pubnub = createPubNub()

        // snippet.historyThreeOldestMessages
        pubnub.history(
            channel = "my_channel", // where to fetch history from
            count = 3, // how many items to fetch
            reverse = true // should go in reverse?
        ).async { result ->
            result.onSuccess { res ->
                res.messages.forEach { message ->
                    message.entry // custom JSON structure for message
                }
            }
        }
    }

    private fun historyRetrieveMessagesNewerThanGivenTimetoken() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback#use-history-to-retrieve-messages-newer-than-a-given-timetoken-by-paging-from-oldest-message-to-newest-message-starting-at-a-single-point-in-time-exclusive

        val pubnub = createPubNub()

        // snippet.historyRetrieveMessagesNewerThanGivenTimetoken
        pubnub.history(
            channel = "my_channel", // where to fetch history from
            start = 13847168620721752L, // first timestamp
            reverse = true // should go in reverse?
        ).async { result ->
            result.onSuccess { res ->
                res.messages.forEach { message ->
                    message.entry // custom JSON structure for message
                }
            }.onFailure { e ->
                // handle error
                e.message
                e.statusCode
                e.pubnubError
            }
        }
    }

    private fun historyRetrieveMessagesUntilGivenTimetoken() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback#use-history-to-retrieve-messages-until-a-given-timetoken-by-paging-from-newest-message-to-oldest-message-until-a-specific-end-point-in-time-inclusive

        val pubnub = createPubNub()

        // snippet.historyRetrieveMessagesUntilGivenTimetoken
        pubnub.history(
            channel = "my_channel", // where to fetch history from
            count = 100, // how many items to fetch
            start = -1, // first timestamp
            end = 13847168819178600L, // last timestamp
            reverse = true // should go in reverse?
        ).async { result ->
            result.onSuccess { res ->
                res.messages // ["Pub3","Pub4","Pub5"]
                res.startTimetoken // 13406746780720711
                res.endTimetoken // 13406746845892666
            }.onFailure { e ->
                // handle error
                e.message
                e.statusCode
                e.pubnubError
            }
        }
    }

    private fun includeTimetoken() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback#include-timetoken-in-history-response

        val pubnub = createPubNub()

        // snippet.includeTimetoken
        pubnub.history(
            channel = "history_channel", // where to fetch history from
            count = 10, // how many items to fetch
            includeTimetoken = true // include timetoken with each entry
        ).async { result ->
            result.onSuccess { value ->
                value!!.messages.forEach {
                    it.entry // custom JSON structure for message
                    it.timetoken // requested message timetoken
                }
            }
        }
        // snippet.end
    }
}
