package com.pubnub.internal.callbacks

import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult

interface Listener

abstract class SubscribeCallback<T> : Listener {

    /**
     * Receive status events like
     * [PNStatusCategory.PNAcknowledgmentCategory],
     * [PNStatusCategory.PNConnectedCategory],
     * [PNStatusCategory.PNReconnectedCategory]
     *
     * and other events related to the subscribe loop and channel mix.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param pnStatus API operation metadata.
     */
    open fun status(pubnub: T, pnStatus: PNStatus) {}

    /**
     * Receive messages at subscribed channels.
     *
     * @see [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param pnMessageResult Wrapper around the actual message content.
     */
    open fun message(pubnub: T, pnMessageResult: PNMessageResult) {}

    /**
     * Receive presence events for channels subscribed to with presence enabled via `withPresence = true` in
     * [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param pnPresenceEventResult Wrapper around a presence event.
     */
    open fun presence(pubnub: T, pnPresenceEventResult: PNPresenceEventResult) {}

    /**
     * Receive signals at subscribed channels.
     *
     * @see [PubNub.signal]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param pnSignalResult Wrapper around a signal event.
     */
    open fun signal(pubnub: T, pnSignalResult: PNSignalResult) {}

    /**
     * Receive message actions for messages in subscribed channels.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param pnMessageActionResult Wrapper around a message action event.
     */
    open fun messageAction(pubnub: T, pnMessageActionResult: PNMessageActionResult) {}

    open fun objects(pubnub: T, objectEvent: PNObjectEventResult) {}
    open fun file(pubnub: T, pnFileEventResult: PNFileEventResult) {}
}
