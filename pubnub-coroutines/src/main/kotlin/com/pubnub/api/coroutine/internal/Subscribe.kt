package com.pubnub.api.coroutine.internal

import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.coroutine.model.MessageEvent
import com.pubnub.api.coroutine.model.toMessageEvent
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.pubnub.api.PubNub as OldPubNub

class Subscribe(private val pubNub: OldPubNub) {

    private val listenersAndChannels: MutableMap<SubscribeCallback, Collection<String>> = mutableMapOf()

    fun messageFlow(channels: Collection<String>): Flow<MessageEvent> =
        callbackFlow {
            val callback: SubscribeCallback = object : SubscribeCallback() {

                override fun message(pubnub: OldPubNub, pnMessageResult: PNMessageResult) {
                    if (pnMessageResult.channel in channels) {
                        trySendBlocking(pnMessageResult.toMessageEvent())
                    }
                }

                override fun status(pubnub: OldPubNub, pnStatus: PNStatus) {
                }
            }
            synchronized(listenersAndChannels) {
                addChannels(callback, channels)
            }

            awaitClose {
                removeChannels(callback, channels)
            }
        }

    fun presenceFlow(channels: Collection<String>): Flow<PNPresenceEventResult> =
        callbackFlow {
            val callback: SubscribeCallback = object : SubscribeCallback() {

                override fun presence(pubnub: OldPubNub, pnPresenceEventResult: PNPresenceEventResult) {
                    if (pnPresenceEventResult.channel in channels) {
                        trySendBlocking(pnPresenceEventResult)
                    }
                }

                override fun status(pubnub: OldPubNub, pnStatus: PNStatus) {
                }
            }
            synchronized(listenersAndChannels) {
                addChannels(callback, channels)
            }

            awaitClose {
                removeChannels(callback, channels)
            }
        }

    fun messageActionFlow(channels: Collection<String>): Flow<PNMessageActionResult> =
        callbackFlow {
            val callback: SubscribeCallback = object : SubscribeCallback() {

                override fun messageAction(pubnub: OldPubNub, pnMessageActionResult: PNMessageActionResult) {
                    if (pnMessageActionResult.channel in channels) {
                        trySendBlocking(pnMessageActionResult)
                    }
                }

                override fun status(pubnub: OldPubNub, pnStatus: PNStatus) {
                }
            }
            synchronized(listenersAndChannels) {
                addChannels(callback, channels)
            }

            awaitClose {
                removeChannels(callback, channels)
            }
        }

    private fun addChannels(callback: SubscribeCallback, channels: Collection<String>) {
        listenersAndChannels[callback] = channels.toList()
        pubNub.addListener(callback)
        pubNub.subscribe(channels = channels.toList())
    }

    private fun removeChannels(callback: SubscribeCallback, channels: Collection<String>) {
        synchronized(listenersAndChannels) {
            listenersAndChannels.remove(callback)
            val channelsLeft = listenersAndChannels.values
                .flatten()
                .distinct()
                .toSet()
            val channelsToUnsubscribe = channels.filter {
                it in channelsLeft
            }

            pubNub.unsubscribe(channels = channelsToUnsubscribe)
            pubNub.removeListener(callback)
        }
    }
}
