package com.pubnub.api.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNMembershipResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSpaceResult
import com.pubnub.api.models.consumer.pubsub.objects.PNUserResult

abstract class SubscribeCallback {
    abstract fun status(pubnub: PubNub, pnStatus: PNStatus)

    open fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {}

    open fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {}

    open fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {}

    open fun user(pubnub: PubNub, pnUserResult: PNUserResult) {}

    open fun space(pubnub: PubNub, pnSpaceResult: PNSpaceResult) {}

    open fun membership(pubnub: PubNub, pnMembershipResult: PNMembershipResult) {}

    open fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {}
}