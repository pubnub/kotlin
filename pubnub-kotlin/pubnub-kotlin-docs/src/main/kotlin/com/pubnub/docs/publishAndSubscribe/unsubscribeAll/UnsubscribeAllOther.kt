package com.pubnub.docs.publishAndSubscribe.unsubscribeAll

import com.pubnub.docs.SnippetBase

class UnsubscribeAllOther : SnippetBase() {
    private fun unsubscribeAll() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#methods-8

        val pubnub = createPubNub()

        // snippet.unsubscribeAllMethod

        pubnub.unsubscribeAll()

        // snippet.end

        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-9

        // snippet.unsubscribeAllBasic

        // Subscribe to channels
        pubnub.subscribe(channels = listOf("my_channel", "other_channel"))

        // Subscribe to a channel group
        pubnub.subscribe(channelGroups = listOf("my_channel_group"))

        // Later, when you want to unsubscribe from all subscriptions
        pubnub.unsubscribeAll()

        // snippet.end
    }
}
