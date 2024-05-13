package com.pubnub.api.v2.callbacks

import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.kmp.CommonPubNub

/**
 * Implement this interface and pass it into [EventEmitter.addListener] to listen for events from the PubNub real-time
 * network.
 */
interface CommonEventListener : BaseEventListener {
    /**
     * Receive messages at subscribed channels.
     *
     * @see [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param result Wrapper around the actual message content.
     */
    fun message(
        pubnub: CommonPubNub,
        result: PNMessageResult,
    ) {}

    /**
     * Receive presence events for channels subscribed with presence enabled via
     * passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents]
     * in [com.pubnub.api.v2.entities.Subscribable.subscription].
     *
     * @param pubnub The client instance which has this listener attached.
     * @param result Wrapper around a presence event.
     */
    fun presence(
        pubnub: CommonPubNub,
        result: PNPresenceEventResult,
    ) {}

    /**
     * Receive signals at subscribed channels.
     *
     * @see [PubNub.signal]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param result Wrapper around a signal event.
     */
    fun signal(
        pubnub: CommonPubNub,
        result: PNSignalResult,
    ) {}

    /**
     * Receive message actions for messages in subscribed channels.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param result Wrapper around a message action event.
     */
    fun messageAction(
        pubnub: CommonPubNub,
        result: PNMessageActionResult,
    ) {}

    /**
     * Receive channel metadata and UUID metadata events in subscribed channels.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param result Wrapper around the object event.
     */
    fun objects(
        pubnub: CommonPubNub,
        result: PNObjectEventResult,
    ) {}

    /**
     * Receive file events in subscribed channels.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param result Wrapper around the file event.
     */
    fun file(
        pubnub: CommonPubNub,
        result: PNFileEventResult,
    ) {}
}