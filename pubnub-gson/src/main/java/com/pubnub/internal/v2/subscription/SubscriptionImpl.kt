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
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.callbacks.DelegatingSubscribeCallback
import com.pubnub.internal.managers.AnnouncementCallback
import com.pubnub.internal.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.v2.callbacks.EventEmitterImpl
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName

class SubscriptionImpl(
    val pubnub: PubNubImpl,
    channels: Set<ChannelName>,
    channelGroups: Set<ChannelGroupName>,
    options: SubscriptionOptions,
    eventEmitterFactory: (BaseSubscriptionImpl<EventListener>) -> EventEmitterImpl = { baseSubscriptionImpl ->
        EventEmitterImpl(AnnouncementCallback.Phase.SUBSCRIPTION, baseSubscriptionImpl::accepts)
    },
) : Subscription,
    BaseSubscriptionImpl<EventListener>(pubnub.pubNubCore, channels, channelGroups, options, eventEmitterFactory) {
    // todo maybe we can add it to constructor so we can mock it?
    private val emitterHelper = EmitterHelper(eventEmitter)

    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    override fun addListener(listener: EventListener) {
        addListener(DelegatingEventListener(listener))
    }

    /**
     * Create a [SubscriptionSet] that contains both subscriptions.
     *
     * @param subscription the other [Subscription] to add to the [SubscriptionSet]
     */
    override fun plus(subscription: Subscription): SubscriptionSet {
        return pubnub.subscriptionSetOf(setOf(this, subscription))
    }

    override fun subscribe() {
        subscribe(SubscriptionCursor(0))
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

    override fun setOnUuidMetadata(onUuidMetadataHandler: OnUuidMetadataHandler?) {
        emitterHelper.onUuid = onUuidMetadataHandler
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
