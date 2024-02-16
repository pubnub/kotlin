package com.pubnub.internal.callbacks

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.internal.BasePubNub
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult

interface SubscribeCallback :
    Listener,
    com.pubnub.internal.v2.callbacks.StatusListener,
    com.pubnub.internal.v2.callbacks.EventListener {

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
    override fun status(pubnub: BasePubNub, status: PNStatus) {}

    /**
     * Receive messages at subscribed channels.
     *
     * @see [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param event Wrapper around the actual message content.
     */
    override fun message(pubnub: BasePubNub, event: PNMessageResult) {}

    /**
     * Receive presence events for channels subscribed to with presence enabled via `withPresence = true` in
     * [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param event Wrapper around a presence event.
     */
    override fun presence(pubnub: BasePubNub, event: PNPresenceEventResult) {}

    /**
     * Receive signals at subscribed channels.
     *
     * @see [PubNub.signal]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param event Wrapper around a signal event.
     */
    override fun signal(pubnub: BasePubNub, event: PNSignalResult) {}

    /**
     * Receive message actions for messages in subscribed channels.
     *
     * @param pubnub The client instance which has this listener attached.
     * @param event Wrapper around a message action event.
     */
    override fun messageAction(pubnub: BasePubNub, event: PNMessageActionResult) {}

    override fun objects(pubnub: BasePubNub, event: PNObjectEventResult) {}
    override fun file(pubnub: BasePubNub, event: PNFileEventResult) {}
}
