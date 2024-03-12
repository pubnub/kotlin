package com.pubnub.internal.v2.subscription

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
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
import com.pubnub.internal.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName

class SubscriptionImpl(
    val pubnub: PubNubImpl,
    channels: Set<ChannelName>,
    channelGroups: Set<ChannelGroupName>,
    options: SubscriptionOptions,
) : Subscription, BaseSubscriptionImpl<EventListener>(pubnub.pubNubCore, channels, channelGroups, options) {
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
        super.subscribe(SubscriptionCursor(0))
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
        setOnMessageHandler(onMessageHandler)
    }

    override fun setOnSignal(onSignalHandler: OnSignalHandler?) {
        setOnSignalHandler(onSignalHandler)
    }

    override fun setOnPresence(onPresenceHandler: OnPresenceHandler?) {
        setOnPresenceHandler(onPresenceHandler)
    }

    override fun setOnMessageAction(onMessageActionHandler: OnMessageActionHandler?) {
        setOnMessageActionHandler(onMessageActionHandler)
    }

    override fun setOnUuidMetadata(onUuidMetadataHandler: OnUuidMetadataHandler?) {
        setOnUuidMetadataHandler(onUuidMetadataHandler)
    }

    override fun setOnChannelMetadata(onChannelMetadataHandler: OnChannelMetadataHandler?) {
        setOnChannelMetadataHandler(onChannelMetadataHandler)
    }

    override fun setOnMembership(onMembershipHandler: OnMembershipHandler?) {
        setOnMembershipHandler(onMembershipHandler)
    }

    override fun setOnFile(onFileHandler: OnFileHandler?) {
        setOnFileHandler(onFileHandler)
    }

    private fun setOnMessageHandler(onMessageHandler: OnMessageHandler?) {
        emitterHelper.onMessage = onMessageHandler?.let { handler ->
            { pnMessageResult: PNMessageResult -> handler.handle(pnMessageResult) }
        }
    }

    private fun setOnPresenceHandler(onPresenceHandler: OnPresenceHandler?) {
        emitterHelper.onPresence = onPresenceHandler?.let { handler ->
            { pnPresenceEventResult -> handler.handle(pnPresenceEventResult) }
        }
    }

    private fun setOnSignalHandler(onSignalHandler: OnSignalHandler?) {
        emitterHelper.onSignal = onSignalHandler?.let { handler ->
            { pnSignalResult: PNSignalResult -> handler.handle(pnSignalResult) }
        }
    }

    private fun setOnMessageActionHandler(onMessageActionHandler: OnMessageActionHandler?) {
        emitterHelper.onMessageAction =
            onMessageActionHandler?.let { handler ->
                { pnMessageActionResult -> handler.handle(pnMessageActionResult) }
            }
    }

    private fun setOnUuidMetadataHandler(onUuidHandler: OnUuidMetadataHandler?) {
        emitterHelper.onUuid = onUuidHandler?.let { handler ->
            { pnUUIDMetadataResult -> handler.handle(pnUUIDMetadataResult) }
        }
    }

    private fun setOnChannelMetadataHandler(onChannelMetadataHandler: OnChannelMetadataHandler?) {
        emitterHelper.onChannel = onChannelMetadataHandler?.let { handler ->
            { pnChannelMetadataResult -> handler.handle(pnChannelMetadataResult) }
        }
    }

    private fun setOnMembershipHandler(onMembershipHandler: OnMembershipHandler?) {
        emitterHelper.onMembership = onMembershipHandler?.let { handler ->
            { pnMembershipResult -> handler.handle(pnMembershipResult) }
        }
    }

    private fun setOnFileHandler(onFileHandler: OnFileHandler?) {
        emitterHelper.onFile = onFileHandler?.let { handler ->
            { pnFileEventResult -> handler.handle(pnFileEventResult) }
        }
    }
}
