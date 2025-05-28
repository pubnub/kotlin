package com.pubnub.docs.publishAndSubscribe.subscribe

import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.docs.SnippetBase

class SubscribeOthers : SnippetBase() {
    private fun createSubscription() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#create-a-subscription

        val pubnub = createPubNub()

        // snippet.createSubscription
        // Entity-based, local-scoped

        // Specify the channel for subscription
        val myChannel = pubnub.channel("channelName")

        // Create subscription options, if any
        val options = SubscriptionOptions.receivePresenceEvents()

        // Return a Subscription object that is used to establish the subscription
        val subscription = myChannel.subscription(options)

        // Activate the subscription to start receiving events
        subscription.subscribe()
        // snippet.end
    }

    private fun subscription() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#subscribe-1

        val pubnub = createPubNub()
        val myChannel = pubnub.channel("channelName")
        val subscription = myChannel.subscription()
        val subscriptionSet = pubnub.subscriptionSetOf(channels = setOf("channelName"))

        // snippet.subscription
        // For subscription
        subscription.subscribe()
        // For subscription set
        subscriptionSet.subscribe()

        // snippet.end
    }

    private fun subscribeBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-3

        val pubnub = createPubNub()

        // snippet.subscribeBasicUsage
        // Step 1: Create a subscription set
        val subscriptionSet = pubnub.subscriptionSetOf(
            // Specify channels with default options
            channels = setOf("my_channel", "other_channel"),
        )

        // Step 2: Subscribe using the subscription set
        subscriptionSet.subscribe()

        // snippet.end
    }

    private fun createSubscriptionSetFrom2individualSubscriptions() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#create-a-subscription-set-from-2-individual-subscriptions

        val pubnub = createPubNub()

        // snippet.createSubscriptionSetFrom2individualSubscriptions
        // Create subscriptions
        val subscription1 = pubnub.channel("channelName").subscription()
        val subscription2 = pubnub.channelGroup("channelGroup").subscription()
        val subscription3 = pubnub.channel("channelName03").subscription()

        // Combine into a subscription set
        val subscriptionSet = subscription1 + subscription2

        // Add another subscription to the set
        subscriptionSet += subscription3
        // Or
        subscriptionSet.add(subscription3)

        // Remove a subscription from the set
        subscriptionSet -= subscription3
        // Or
        subscriptionSet.remove(subscription3)

        // snippet.end
    }

    fun subscribeWithTimetoken() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#subscribe-with-timetoken
        val pubnub = createPubNub()
        val channels = setOf("my_channel", "other_channel")
        val subscriptionSet = pubnub.subscriptionSetOf(channels = channels)
        val yourTimeToken = 100000000000L // Directly using Long type

        // snippet.subscribeWithTimetoken
        subscriptionSet.subscribe(SubscriptionCursor(timetoken = yourTimeToken))
        // snippet.end
    }

    fun subscribeToSubscriptionSetUsingTimetoken() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-4

        val pubnub = createPubNub()

        // snippet.subscribeToSubscriptionSetUsingTimetoken
        // Define the channels to subscribe to
        val channels = setOf("my_channel", "other_channel")

        // Create a subscription set with specified channels and subscription options
        val options = SubscriptionOptions.receivePresenceEvents()
        val subscriptionSet = pubnub.subscriptionSetOf(channels = channels, options = options)

        // Define the timetoken for where the subscription should start
        val yourTimeToken = 100000000000L // Directly using Long type

        // Subscribe to the created SubscriptionSet with the desired timetoken
        subscriptionSet.subscribe(SubscriptionCursor(timetoken = yourTimeToken))

        // snippet.end
    }
}
