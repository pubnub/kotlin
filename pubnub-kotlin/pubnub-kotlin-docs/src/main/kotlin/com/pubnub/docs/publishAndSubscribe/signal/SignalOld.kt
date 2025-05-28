package com.pubnub.docs.publishAndSubscribe.signal

import com.pubnub.docs.SnippetBase

class SignalOld : SnippetBase() {
    private fun signalMessageToChannel() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-12

        val pubnub = createPubNub()

        // snippet.signalMessageToChannel
        pubnub.signal(
            message = "hello",
            channel = "my_channel"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }
}
