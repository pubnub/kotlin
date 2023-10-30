package com.pubnub.internal.callbacks

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.internal.BasePubNub
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult


interface Listener

interface SubscribeCallback : Listener {

    /**
     * Receive status events like
     * [PNStatus.Connected],
     * [PNStatus.Disconnected],
     * [PNStatus.SubscriptionChanged]
     *
     * and other events related to the subscribe loop and channel mix.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param status subscription status update.
     */
    fun status(pubnub: BasePubNub, status: PNStatus) {}

    /**
     * Receive messages at subscribed channels.
     *
     * @see [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param message Wrapper around the actual message content.
     */
    fun message(pubnub: BasePubNub, message: PNMessageResult) {}

    /**
     * Receive presence events for channels subscribed to with presence enabled via `withPresence = true` in
     * [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param presenceEvent Wrapper around a presence event.
     */
    fun presence(pubnub: BasePubNub, presenceEvent: PNPresenceEventResult) {}

    /**
     * Receive signals at subscribed channels.
     *
     * @see [PubNub.signal]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param signal Wrapper around a signal event.
     */
    fun signal(pubnub: BasePubNub, signal: PNSignalResult) {}

    /**
     * Receive message actions for messages in subscribed channels.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param messageAction Wrapper around a message action event.
     */
    fun messageAction(pubnub: BasePubNub, messageAction: PNMessageActionResult) {}

    fun objects(pubnub: BasePubNub, objectEvent: PNObjectEventResult) {}
    fun file(pubnub: BasePubNub, fileEvent: PNFileEventResult) {}
}
