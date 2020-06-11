package com.pubnub.api.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult

abstract class SubscribeCallback {
    abstract fun status(pubnub: PubNub, pnStatus: PNStatus)

    open fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {}

    open fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {}

    open fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {}

    open fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {}
}