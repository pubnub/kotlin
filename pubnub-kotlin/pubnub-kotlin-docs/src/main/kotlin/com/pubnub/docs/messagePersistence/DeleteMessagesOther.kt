package com.pubnub.docs.messagePersistence

import com.pubnub.api.PubNub

class DeleteMessagesOther {
    private fun deleteMessagesBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback#basic-usage-1

        // snippet.deleteMessagesBasic
        pubnub.deleteMessages(
            channels = listOf("channel_1", "channel_2"),
            start = 1460693607379L,
            end = 1460893617271L
        ).async { result ->
            // The deleteMessages() method does not return actionable data.
            // Be sure to check the status on the outcome of the operation

            if (result.isSuccess) {
                println("Successfully deleted messages.")
            } else {
                println("Error deleting messages: ${result.exceptionOrNull()?.message}")
            }
        }
        // snippet.end
    }

    private fun deleteSpecificMessageFromHistory(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback#delete-specific-message-from-history

        // snippet.deleteSpecificMessageFromHistory
        pubnub.deleteMessages(
            channels = listOf("channel_1"),
            start = 15526611838554309L,
            end = 15526611838554310L
        ).async { result ->
            // The deleteMessages() method does not return actionable data.
            // Be sure to check the status on the outcome of the operation

            if (result.isSuccess) {
                println("Successfully deleted messages.")
            } else {
                println("Error deleting messages: ${result.exceptionOrNull()?.message}")
            }
        }
        // snippet.end
    }
}
