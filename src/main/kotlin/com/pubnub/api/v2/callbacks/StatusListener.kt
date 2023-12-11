package com.pubnub.api.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult

abstract class StatusListener : SubscribeCallback() {
    abstract override fun status(pubnub: PubNub, status: PNStatus)

    override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {}

    override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {}

    override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {}

    override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {}

    override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {}

    override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {}
}
