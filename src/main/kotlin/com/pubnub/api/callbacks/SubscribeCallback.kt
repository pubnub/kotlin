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

interface Listener

interface Stoppable {
    fun stop()
}

interface CoreListener : Listener {

    fun status(pubnub: PubNub, pnStatus: PNStatus)
    fun message(pubnub: PubNub, pnMessageResult: PNMessageResult)
    fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult)
    fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult)
    fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult)
    fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult)
    fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult)
}

abstract class SubscribeCallback : CoreListener {

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

    override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {}
    override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {}
}
