package com.pubnub.docs.publishAndSubscribe.unsubscribe

import com.pubnub.docs.SnippetBase

class UnsubscribeOthers : SnippetBase() {
    private fun unsubscribeMethod() {
        val pubnub = createPubNub()
        val subscription = pubnub.channel("my_channel").subscription()
        val subscriptionSet = pubnub.subscriptionSetOf(channels = setOf("my_channel", "other_channel"))

        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#methods-7
        // snippet.unsubscribeMethod

        // For subscription
        subscription.unsubscribe()
        // For subscription set
        subscriptionSet.unsubscribe()
        // snippet.end

        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-8
        // snippet.unsubscribeBasic

        // Subscribe to a channel
        subscription.subscribe()

        // Unsubscribe from that channel
        subscription.unsubscribe()

        // snippet.end
    }
}
