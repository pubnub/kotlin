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

    abstract fun message(pubnub: PubNub, pnMessageResult: PNMessageResult)

    abstract fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult)

    abstract fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult)

    abstract fun user(pubnub: PubNub, pnUserResult: PNUserResult)

    abstract fun space(pubnub: PubNub, pnSpaceResult: PNSpaceResult)

    abstract fun membership(pubnub: PubNub, pnMembershipResult: PNMembershipResult)

    abstract fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult)
}