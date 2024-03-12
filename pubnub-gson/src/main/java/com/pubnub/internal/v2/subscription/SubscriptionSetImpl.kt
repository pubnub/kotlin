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
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.callbacks.DelegatingSubscribeCallback
import com.pubnub.internal.v2.callbacks.DelegatingEventListener

class SubscriptionSetImpl(
    pubnub: PubNubCore,
    initialSubscriptions: Set<SubscriptionImpl>,
) : SubscriptionSet, BaseSubscriptionSetImpl<EventListener, Subscription>(pubnub, initialSubscriptions) {
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

    override fun setOnMessage(onMessage: OnMessageHandler?) {
        TODO("Not yet implemented")
    }

    override fun setOnSignal(onSignalHandler: OnSignalHandler?) {
        TODO("Not yet implemented")
    }

    override fun setOnPresence(onPresenceHandler: OnPresenceHandler?) {
        TODO("Not yet implemented")
    }

    override fun setOnMessageAction(onMessageActionHandler: OnMessageActionHandler?) {
        TODO("Not yet implemented")
    }

    override fun setOnUuidMetadata(onUuidHandler: OnUuidMetadataHandler?) {
        TODO("Not yet implemented")
    }

    override fun setOnChannelMetadata(onChannelMetadataHandler: OnChannelMetadataHandler?) {
        TODO("Not yet implemented")
    }

    override fun setOnMembership(onMembershipHandler: OnMembershipHandler?) {
        TODO("Not yet implemented")
    }

    override fun setOnFile(onFileHandler: OnFileHandler?) {
        TODO("Not yet implemented")
    }
}
