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

class SubscriptionSetImpl(
    pubnub: PubNubCore,
    initialSubscriptions: Set<SubscriptionImpl>,
) : SubscriptionSet, BaseSubscriptionSetImpl<EventListener, Subscription>(pubnub, initialSubscriptions) {
    // todo maybe we can add it to constructor so we can mock it?
    private val emitterHelper = EmitterHelper(eventEmitter)

    override fun subscribe() {
        super.subscribe(SubscriptionCursor(0))
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

    override fun setOnUuidMetadata(onUuidHandler: OnUuidMetadataHandler?) {
        setOnUuidHandler(onUuidHandler)
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
            { pnMessageResult -> handler.handle(pnMessageResult) }
        }
    }

    private fun setOnSignalHandler(onSignalHandler: OnSignalHandler?) {
        emitterHelper.onSignal = onSignalHandler?.let { handler ->
            { pnSignalResult -> handler.handle(pnSignalResult) }
        }
    }

    private fun setOnPresenceHandler(onPresenceHandler: OnPresenceHandler?) {
        emitterHelper.onPresence = onPresenceHandler?.let { handler ->
            { pnPresenceEventResult -> handler.handle(pnPresenceEventResult) }
        }
    }

    private fun setOnMessageActionHandler(onMessageActionHandler: OnMessageActionHandler?) {
        emitterHelper.onMessageAction = onMessageActionHandler?.let { handler ->
            { pnMessageActionResult -> handler.handle(pnMessageActionResult) }
        }
    }

    private fun setOnUuidHandler(onUuidHandler: OnUuidMetadataHandler?) {
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
