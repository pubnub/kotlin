package com.pubnub.docs.publishAndSubscribe.eventListeners

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.docs.SnippetBase

class EventListenerOld : SnippetBase() {
    private fun addListener() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#add-listeners-1

        val pubnub = createPubNub()

        // snippet.addListener
        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, status: PNStatus) {
                println("Status category: ${status.category}")
                // PNConnectedCategory, PNReconnectedCategory, PNDisconnectedCategory

                println("Status error: ${status.error}")
                // true or false
            }

            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                println("Presence event: ${pnPresenceEventResult.event}")
                println("Presence channel: ${pnPresenceEventResult.channel}")
                println("Presence uuid: ${pnPresenceEventResult.uuid}")
                println("Presence timetoken: ${pnPresenceEventResult.timetoken}")
                println("Presence occupancy: ${pnPresenceEventResult.occupancy}")
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                println("Message payload: ${pnMessageResult.message}")
                println("Message channel: ${pnMessageResult.channel}")
                println("Message publisher: ${pnMessageResult.publisher}")
                println("Message timetoken: ${pnMessageResult.timetoken}")
            }

            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                println("Signal payload: ${pnSignalResult.message}")
                println("Signal channel: ${pnSignalResult.channel}")
                println("Signal publisher: ${pnSignalResult.publisher}")
                println("Signal timetoken: ${pnSignalResult.timetoken}")
            }

            override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
                with(pnMessageActionResult.messageAction) {
                    println("Message reaction type: $type")
                    println("Message reaction value: $value")
                    println("Message reaction uuid: $uuid")
                    println("Message reaction actionTimetoken: $actionTimetoken")
                    println("Message reaction messageTimetoken: $messageTimetoken")
                }

                println("Message reaction subscription: ${pnMessageActionResult.subscription}")
                println("Message reaction channel: ${pnMessageActionResult.channel}")
                println("Message reaction timetoken: ${pnMessageActionResult.timetoken}")
            }

            override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
                println("Object event channel: ${objectEvent.channel}")
                println("Object event publisher: ${objectEvent.publisher}")
                println("Object event subscription: ${objectEvent.subscription}")
                println("Object event timetoken: ${objectEvent.timetoken}")
                println("Object event userMetadata: ${objectEvent.userMetadata}")

                with(objectEvent.extractedMessage) {
                    println("Object event event: $event")
                    println("Object event source: $source")
                    println("Object event type: $type")
                    println("Object event version: $version")
                }
            }
        })
        // snippet.end
    }

    private fun removeListener() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#remove-listeners

        val pubnub = createPubNub()

        // snippet.removeListener
        val listener = object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {}
            // and other callbacks
        }

        pubnub.addListener(listener)

        // some time later
        pubnub.removeListener(listener)
        // snippet.end
    }
}
