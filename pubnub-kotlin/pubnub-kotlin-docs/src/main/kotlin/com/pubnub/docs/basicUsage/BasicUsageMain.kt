package com.pubnub.docs.basicUsage
// https://www.pubnub.com/docs/sdks/kotlin#complete-example
// snippet.initializePubNubBasic

import com.google.gson.JsonObject
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

fun main() {
    // Step 1: Initialize PubNub with configuration
    val config = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
        publishKey = "demo"
    }.build()

    val pubnub = PubNub.create(config)
    // snippet.end

    // Step 2: Define a channel and prepare a message
    val myChannel = "myChannel"
    val myMessage = JsonObject().apply {
        addProperty("msg", "Hello, world")
    }

    println("Message to send: $myMessage")

    // snippet.setupEventListenersBasic
    // Step 3: Set up connection status listener
    pubnub.addListener(object : StatusListener {
        override fun status(pubnub: PubNub, status: PNStatus) {
            when (status.category) {
                PNStatusCategory.PNConnectedCategory -> println("Connected/Reconnected")
                PNStatusCategory.PNDisconnectedCategory,
                PNStatusCategory.PNUnexpectedDisconnectCategory -> println("Disconnected/Unexpectedly Disconnected")
                else -> {}
            }
        }
    })
    // snippet.end

    // snippet.createSubscriptionBasic
    // Step 4: Create a subscription
    val channel = pubnub.channel(myChannel)
    val options = SubscriptionOptions.receivePresenceEvents()
    val subscription = channel.subscription(options)

    // Step 5: Add event listeners
    subscription.addListener(object : EventListener {
        override fun message(pubnub: PubNub, result: PNMessageResult) {
            println("Received message ${result.message.asJsonObject}")
        }

        override fun presence(pubnub: PubNub, result: PNPresenceEventResult) {
            // Handle presence events
        }
    })

    // Step 6: Activate the subscription
    subscription.subscribe()
    // snippet.end

    // Wait for connection to establish before publishing
    Thread.sleep(4000)

    // snippet.publishMessagesBasic
    // Step 7: Publish a message
    channel.publish(myMessage).async { result ->
        result.onFailure { exception ->
            println("Error while publishing")
            exception.printStackTrace()
        }.onSuccess { value ->
            println("Message sent, timetoken: ${value.timetoken}")
        }
    }
    
    // Keep the program running to receive the published message
    Thread.sleep(2000)
}
// snippet.end
