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
import com.pubnub.internal.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.v2.callbacks.EventListenerCore

class EmitterHelper(eventEmitter: BaseEventEmitter<EventListenerCore>) {
    init {
        eventEmitter.addListener(
            DelegatingEventListener(
                object : EventListener {
                    override fun message(
                        pubnub: PubNub,
                        result: PNMessageResult,
                    ) {
                        onMessage?.invoke(result)
                    }

                    override fun presence(
                        pubnub: PubNub,
                        result: PNPresenceEventResult,
                    ) {
                        onPresence?.invoke(result)
                    }

                    override fun signal(
                        pubnub: PubNub,
                        result: PNSignalResult,
                    ) {
                        onSignal?.invoke(result)
                    }

                    override fun messageAction(
                        pubnub: PubNub,
                        result: PNMessageActionResult,
                    ) {
                        onMessageAction?.invoke(result)
                    }

                    override fun uuid(
                        pubnub: PubNub,
                        result: PNUUIDMetadataResult,
                    ) {
                        onUuid?.invoke(result)
                    }

                    override fun channel(
                        pubnub: PubNub,
                        result: PNChannelMetadataResult,
                    ) {
                        onChannel?.invoke(result)
                    }

                    override fun membership(
                        pubnub: PubNub,
                        result: PNMembershipResult,
                    ) {
                        onMembership?.invoke(result)
                    }

                    override fun file(
                        pubnub: PubNub,
                        result: PNFileEventResult,
                    ) {
                        onFile?.invoke(result)
                    }
                },
            ),
        )
    }

    var onMessage: ((PNMessageResult) -> Unit)? = null
    var onPresence: ((PNPresenceEventResult) -> Unit)? = null
    var onSignal: ((PNSignalResult) -> Unit)? = null
    var onMessageAction: ((PNMessageActionResult) -> Unit)? = null
    var onUuid: ((PNUUIDMetadataResult) -> Unit)? = null
    var onChannel: ((PNChannelMetadataResult) -> Unit)? = null
    var onMembership: ((PNMembershipResult) -> Unit)? = null
    var onFile: ((PNFileEventResult) -> Unit)? = null
}
