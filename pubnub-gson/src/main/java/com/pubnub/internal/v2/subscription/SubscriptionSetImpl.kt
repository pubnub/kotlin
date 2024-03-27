package com.pubnub.internal.v2.subscription

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.handlers.OnChannelMetadataHandler
import com.pubnub.api.v2.callbacks.handlers.OnFileHandler
import com.pubnub.api.v2.callbacks.handlers.OnMembershipHandler
import com.pubnub.api.v2.callbacks.handlers.OnMessageActionHandler
import com.pubnub.api.v2.callbacks.handlers.OnMessageHandler
import com.pubnub.api.v2.callbacks.handlers.OnPresenceHandler
import com.pubnub.api.v2.callbacks.handlers.OnSignalHandler
import com.pubnub.api.v2.callbacks.handlers.OnUuidMetadataHandler
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.callbacks.DelegatingSubscribeCallback
import com.pubnub.internal.v2.callbacks.DelegatingEventListener

class SubscriptionSetImpl
    @JvmOverloads
    constructor(
        pubnub: PubNubCore,
        initialSubscriptions: Set<SubscriptionImpl>,
        private val emitterHelper: EmitterHelper = EmitterHelper(),
    ) : SubscriptionSet, BaseSubscriptionSetImpl<EventListener, Subscription>(pubnub, initialSubscriptions) {
        init {
            emitterHelper.initialize(eventEmitter)
        }

        override fun subscribe() {
            subscribe(SubscriptionCursor(0))
        }

        /**
         * Add a listener.
         *
         * @param listener The listener to be added.
         */
        override fun addListener(listener: EventListener) {
            addListener(DelegatingEventListener(listener))
        }

        override fun removeListener(listener: Listener) {
            when (listener) {
                is SubscribeCallback -> {
                    super.removeListener(DelegatingSubscribeCallback(listener))
                }

                is EventListener -> {
                    super.removeListener(DelegatingEventListener(listener))
                }

                else -> {
                    super.removeListener(listener)
                }
            }
        }

        override fun setOnMessage(onMessageHandler: OnMessageHandler?) {
            emitterHelper.onMessage = onMessageHandler
        }

        override fun setOnSignal(onSignalHandler: OnSignalHandler?) {
            emitterHelper.onSignal = onSignalHandler
        }

        override fun setOnPresence(onPresenceHandler: OnPresenceHandler?) {
            emitterHelper.onPresence = onPresenceHandler
        }

        override fun setOnMessageAction(onMessageActionHandler: OnMessageActionHandler?) {
            emitterHelper.onMessageAction = onMessageActionHandler
        }

        override fun setOnUuidMetadata(onUuidHandler: OnUuidMetadataHandler?) {
            emitterHelper.onUuid = onUuidHandler
        }

        override fun setOnChannelMetadata(onChannelMetadataHandler: OnChannelMetadataHandler?) {
            emitterHelper.onChannel = onChannelMetadataHandler
        }

        override fun setOnMembership(onMembershipHandler: OnMembershipHandler?) {
            emitterHelper.onMembership = onMembershipHandler
        }

        override fun setOnFile(onFileHandler: OnFileHandler?) {
            emitterHelper.onFile = onFileHandler
        }
    }
