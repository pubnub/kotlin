package com.pubnub.api.java.v2.callbacks

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.java.PubNub
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadataResult
import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembershipResult
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadataResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult

/**
 * Implement this interface and pass it into [EventEmitter.addListener] to listen for events from the PubNub real-time
 * network.
 */
interface EventListener : Listener {
    fun message(pubnub: PubNub, result: PNMessageResult) {}

    fun presence(
        pubnub: PubNub,
        result: PNPresenceEventResult,
    ) {}

    fun signal(pubnub: PubNub, result: PNSignalResult) {}

    fun messageAction(
        pubNub: PubNub,
        result: PNMessageActionResult,
    ) {}

    fun file(
        pubnub: PubNub,
        result: PNFileEventResult,
    ) {}

    fun uuid(pubnub: PubNub, pnUUIDMetadataResult: PNUUIDMetadataResult) {
    }

    fun channel(pubnub: PubNub, pnChannelMetadataResult: PNChannelMetadataResult) {
    }

    fun membership(pubnub: PubNub, pnMembershipResult: PNMembershipResult) {
    }

    /**
     * Receive DataSync realtime events on subscribed ref-channels.
     *
     * A single subscribed ref-channel delivers a mix of leaf types; branch on the sealed
     * [PNDataSyncEventResult.extractedMessage]. This exposes the Kotlin
     * [PNDataSyncEventResult] directly (a conscious cross-SDK choice — DataSync does not ship Java-specific
     * per-type result models, unlike objects).
     */
    fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
    }
}
