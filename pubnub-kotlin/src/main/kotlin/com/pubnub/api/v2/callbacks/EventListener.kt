package com.pubnub.api.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.internal.BasePubNub
import com.pubnub.internal.models.consumer.objects.toApi
import com.pubnub.internal.v2.callbacks.EventListener

abstract class EventListener : EventListener {

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

    /**
     * Receive messages at subscribed channels.
     *
     * @see [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param result Wrapper around the actual message content.
     */
    open fun message(pubnub: PubNub, result: PNMessageResult) {}

    /**
     * Receive presence events for channels subscribed with presence enabled via
     * passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents]
     * in [com.pubnub.api.v2.entities.BaseChannel.subscription].
     *
     * @param pubnub The client instance which has this listener attached.
     * @param result Wrapper around a presence event.
     */
    open fun presence(pubnub: PubNub, result: PNPresenceEventResult) {}

    /**
     * Receive signals at subscribed channels.
     *
     * @see [PubNub.signal]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param result Wrapper around a signal event.
     */
    open fun signal(pubnub: PubNub, result: PNSignalResult) {}

    /**
     * Receive message actions for messages in subscribed channels.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param result Wrapper around a message action event.
     */
    open fun messageAction(pubnub: PubNub, result: PNMessageActionResult) {}

    /**
     * Receive channel metadata and UUID metadata events in subscribed channels.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param result Wrapper around the object event.
     */
    open fun objects(pubnub: PubNub, result: PNObjectEventResult) {}

    /**
     * Receive file events in subscribed channels.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param result Wrapper around the file event.
     */
    open fun file(pubnub: PubNub, result: PNFileEventResult) {}
}
