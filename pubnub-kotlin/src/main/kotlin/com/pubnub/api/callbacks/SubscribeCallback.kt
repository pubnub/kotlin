package com.pubnub.api.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.internal.BasePubNub
import com.pubnub.internal.callbacks.SubscribeCallback
import com.pubnub.internal.models.consumer.objects.toApi

abstract class SubscribeCallback : SubscribeCallback {

    final override fun message(pubnub: BasePubNub, event: PNMessageResult) {
        message(pubnub as PubNub, event)
    }

    final override fun presence(pubnub: BasePubNub, event: PNPresenceEventResult) {
        presence(pubnub as PubNub, event)
    }

    final override fun signal(pubnub: BasePubNub, event: PNSignalResult) {
        signal(pubnub as PubNub, event)
    }

    final override fun messageAction(pubnub: BasePubNub, event: PNMessageActionResult) {
        messageAction(pubnub as PubNub, event)
    }

    final override fun objects(
        pubnub: BasePubNub,
        event: com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult
    ) {
        objects(pubnub as PubNub, event.toApi())
    }

    final override fun file(pubnub: BasePubNub, event: PNFileEventResult) {
        file(pubnub as PubNub, event)
    }

    final override fun status(pubnub: BasePubNub, status: PNStatus) {
        status(pubnub as PubNub, status)
    }

    /**
     * Receive status events like
     * [PNStatus.Connected],
     * [PNStatus.Disconnected],
     * [PNStatus.SubscriptionChanged]
     *
     * and other events related to the subscribe loop and channel mix.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param status API operation metadata.
     */
    open fun status(pubnub: PubNub, status: PNStatus) {}

    /**
     * Receive messages at subscribed channels.
     *
     * @see [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param event Wrapper around the actual message content.
     */
    open fun message(pubnub: PubNub, event: PNMessageResult) {}

    /**
     * Receive presence events for channels subscribed to with presence enabled via `withPresence = true` in
     * [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param event Wrapper around a presence event.
     */
    open fun presence(pubnub: PubNub, event: PNPresenceEventResult) {}

    /**
     * Receive signals at subscribed channels.
     *
     * @see [PubNub.signal]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param event Wrapper around a signal event.
     */
    open fun signal(pubnub: PubNub, event: PNSignalResult) {}

    /**
     * Receive message actions for messages in subscribed channels.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param event Wrapper around a message action event.
     */
    open fun messageAction(pubnub: PubNub, event: PNMessageActionResult) {}

    fun objects(
        pubnub: PubNub,
        objectEvent: com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult
    ) {
        objects(pubnub, objectEvent.toApi())
    }

    open fun objects(pubnub: PubNub, event: PNObjectEventResult) {}

    open fun file(pubnub: PubNub, event: PNFileEventResult) {}
}
