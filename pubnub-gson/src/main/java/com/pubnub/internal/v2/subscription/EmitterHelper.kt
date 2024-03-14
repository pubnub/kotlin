package com.pubnub.internal.v2.subscription

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.v2.callbacks.BaseEventEmitter
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.handlers.OnChannelMetadataHandler
import com.pubnub.api.v2.callbacks.handlers.OnFileHandler
import com.pubnub.api.v2.callbacks.handlers.OnMembershipHandler
import com.pubnub.api.v2.callbacks.handlers.OnMessageActionHandler
import com.pubnub.api.v2.callbacks.handlers.OnMessageHandler
import com.pubnub.api.v2.callbacks.handlers.OnPresenceHandler
import com.pubnub.api.v2.callbacks.handlers.OnSignalHandler
import com.pubnub.api.v2.callbacks.handlers.OnUuidMetadataHandler
import com.pubnub.internal.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.v2.callbacks.EventListenerCore

class EmitterHelper {
    private val listener = object : EventListener {
        override fun message(
            pubnub: PubNub,
            result: PNMessageResult,
        ) {
            onMessage?.handle(result)
        }

        override fun presence(
            pubnub: PubNub,
            result: PNPresenceEventResult,
        ) {
            onPresence?.handle(result)
        }

        override fun signal(
            pubnub: PubNub,
            result: PNSignalResult,
        ) {
            onSignal?.handle(result)
        }

        override fun messageAction(
            pubnub: PubNub,
            result: PNMessageActionResult,
        ) {
            onMessageAction?.handle(result)
        }

        override fun uuid(
            pubnub: PubNub,
            result: PNUUIDMetadataResult,
        ) {
            onUuid?.handle(result)
        }

        override fun channel(
            pubnub: PubNub,
            result: PNChannelMetadataResult,
        ) {
            onChannel?.handle(result)
        }

        override fun membership(
            pubnub: PubNub,
            result: PNMembershipResult,
        ) {
            onMembership?.handle(result)
        }

        override fun file(
            pubnub: PubNub,
            result: PNFileEventResult,
        ) {
            onFile?.handle(result)
        }
    }

    fun initialize(eventEmitter: BaseEventEmitter<EventListenerCore>) {
        eventEmitter.addListener(DelegatingEventListener(listener))
    }

    var onMessage: OnMessageHandler? = null
    var onPresence: OnPresenceHandler? = null
    var onSignal: OnSignalHandler? = null
    var onMessageAction: OnMessageActionHandler? = null
    var onUuid: OnUuidMetadataHandler? = null
    var onChannel: OnChannelMetadataHandler? = null
    var onMembership: OnMembershipHandler? = null
    var onFile: OnFileHandler? = null
}
