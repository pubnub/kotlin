package com.pubnub.docs.publishAndSubscribe.fire

import com.pubnub.docs.SnippetBase

class FireOld : SnippetBase() {
    private fun fireMessageToChannel() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-11

        val pubnub = createPubNub()

        // snippet.fireMessageToChannel
        pubnub.fire(
            message = listOf("hello", "there"),
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
