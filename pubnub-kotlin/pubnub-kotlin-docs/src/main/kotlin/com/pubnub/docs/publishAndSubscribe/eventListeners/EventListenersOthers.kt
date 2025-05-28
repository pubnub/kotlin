package com.pubnub.docs.publishAndSubscribe.eventListeners

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.docs.SnippetBase

class EventListenersOthers : SnippetBase() {
    fun addListenersBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-5

        val pubnub = createPubNub()

        // snippet.addListenersBasic
        // Create a subscription to a specific channel
        val subscription = pubnub.channel("my_channel").subscription()

        // Add a listener to the subscription for handling various event types
        subscription.addListener(object : EventListener {
            override fun message(pubnub: PubNub, message: PNMessageResult) {
                // Log or process message
                println("Message: ${message.message}")
            }

            override fun signal(pubnub: PubNub, signal: PNSignalResult) {
                // Handle signals
                println("Signal: ${signal.message}")
            }

            override fun messageAction(pubnub: PubNub, messageAction: PNMessageActionResult) {
                // Handle message reactions
                println("Message Reaction: ${messageAction.data}")
            }

            override fun file(pubnub: PubNub, file: PNFileEventResult) {
                // Handle file events
                println("File: ${file.file.name}")
            }

            override fun objects(pubnub: PubNub, obj: PNObjectEventResult) {
                // Handle metadata updates
                println("App Context: ${obj.extractedMessage.event}")
            }

            override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {
                // Handle presence updates
                // requires a subscription with presence
                println("Presence: ${presence.uuid} - ${presence.event}")
            }
        })

        // Activate the subscription to start receiving events
        subscription.subscribe()

        // Print a status when successfully subscribed
        println("Subscribed to channel 'my_channel'")

        // Create subscription set
        val subscriptionSet = pubnub.subscriptionSetOf(
            // Specify channels with default options
            channels = setOf("my_channel", "other_channel"),
        )

        // Add listener to the subscriptionSet
        subscriptionSet.addListener(object : EventListener {
            override fun message(pubnub: PubNub, message: PNMessageResult) {
                // Log or process message
                println("Message: ${message.message}")
            }
        })

        // Activate the subscriptionSet to start receiving events
        subscriptionSet.subscribe()

        // snippet.end
    }

    private fun createListenerForOneTypeEvent() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-6

        val pubnub = createPubNub()
        val subscription = pubnub.channel("my_channel").subscription()

        // snippet.createListenerForOneTypeEvent
        subscription.onMessage = { message ->
            // Handle message
        }

        subscription.onSignal = { signal ->
            // Handle signal
        }

        subscription.onMessageAction = { messageAction ->
            // Handle message reaction
        }

        subscription.onFile = { file ->
            // Handle file event
        }

        subscription.onObjects = { obj ->
            // Handle metadata updates
        }

        subscription.onPresence = { presence ->
            // Handle presence updates
        }

        val onMessage: (PNMessageResult) -> Unit = { /* Handle message */ }
        val onSignal: (PNSignalResult) -> Unit = { /* Handle signal */ }
        val onMessageAction: (PNMessageActionResult) -> Unit = { /* Handle message reaction */ }
        val onFile: (PNFileEventResult) -> Unit = { /* Handle file event */ }
        val onObjects: (PNObjectEventResult) -> Unit = { /* Handle metadata updates */ }
        val onPresence: (PNPresenceEventResult) -> Unit = { /* Handle presence updates */ }

        subscription.onMessage = onMessage
        subscription.onSignal = onSignal
        subscription.onMessageAction = onMessageAction
        subscription.onFile = onFile
        subscription.onObjects = onObjects
        subscription.onPresence = onPresence

        // snippet.end

        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#remove-event-listener

        // snippet.removeEventListener
        subscription.onMessage = null
        subscription.onSignal = null
        subscription.onMessageAction = null
        subscription.onFile = null
        subscription.onObjects = null
        subscription.onPresence = null

        // snippet.end
    }

    private fun addConnectionStatusListenerMethod() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#methods-6

        val pubnub = createPubNub()

        // snippet.addConnectionStatusListenerMethod
        pubnub.addListener(object : StatusListener {
            override fun status(pubnub: PubNub, status: PNStatus) {
                // Handle connection status updates
                println("Connection Status: ${status.category}")
            }
        })

        // snippet.end
    }
}
