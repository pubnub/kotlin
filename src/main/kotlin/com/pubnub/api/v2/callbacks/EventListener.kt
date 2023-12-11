package com.pubnub.api.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult

abstract class EventListener {
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
     * Receive presence events for channels subscribed to with presence enabled via `withPresence = true` in
     * [PubNub.subscribe]
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

    open fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {}
    open fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {}
}
