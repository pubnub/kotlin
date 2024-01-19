package com.pubnub.api.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.internal.callbacks.SubscribeCallback
import com.pubnub.internal.models.consumer.objects.toApi

abstract class SubscribeCallback : SubscribeCallback<PubNub>() {

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
    abstract override fun status(pubnub: PubNub, pnStatus: PNStatus)

    /**
     * Receive messages at subscribed channels.
     *
     * @see [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param pnMessageResult Wrapper around the actual message content.
     */
    override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {}

    /**
     * Receive presence events for channels subscribed to with presence enabled via `withPresence = true` in
     * [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param pnPresenceEventResult Wrapper around a presence event.
     */
    override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {}

    /**
     * Receive signals at subscribed channels.
     *
     * @see [PubNub.signal]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param pnSignalResult Wrapper around a signal event.
     */
    override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {}

    /**
     * Receive message actions for messages in subscribed channels.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param pnMessageActionResult Wrapper around a message action event.
     */
    override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {}

    final override fun objects(
        pubnub: PubNub,
        objectEvent: com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult
    ) {
        objects(pubnub, objectEvent.toApi())
    }

    open fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {}

    override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {}
}
