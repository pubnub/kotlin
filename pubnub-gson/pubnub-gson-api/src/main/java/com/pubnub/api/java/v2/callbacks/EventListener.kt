package com.pubnub.api.java.v2.callbacks

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.java.PubNubForJava
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadataResult
import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembershipResult
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadataResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult

/**
 * Implement this interface and pass it into [EventEmitter.addListener] to listen for events from the PubNub real-time
 * network.
 */
interface EventListener : Listener {
    fun message(pubnub: PubNubForJava, result: PNMessageResult) {}

    fun presence(
        pubnub: PubNubForJava,
        result: PNPresenceEventResult,
    ) {}

    fun signal(pubnub: PubNubForJava, result: PNSignalResult) {}

    fun messageAction(
        pubNub: PubNubForJava,
        result: PNMessageActionResult,
    ) {}

    fun file(
        pubnub: PubNubForJava,
        result: PNFileEventResult,
    ) {}

    fun uuid(pubnub: PubNubForJava, pnUUIDMetadataResult: PNUUIDMetadataResult) {
    }

    fun channel(pubnub: PubNubForJava, pnChannelMetadataResult: PNChannelMetadataResult) {
    }

    fun membership(pubnub: PubNubForJava, pnMembershipResult: PNMembershipResult) {
    }
}
