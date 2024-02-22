package com.pubnub.internal

import com.pubnub.api.BasePubNub
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.v2.callbacks.BaseEventEmitter
import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.callbacks.BaseStatusEmitter
import com.pubnub.api.v2.callbacks.BaseStatusListener
import com.pubnub.api.v2.entities.BaseChannel
import com.pubnub.api.v2.entities.BaseChannelGroup
import com.pubnub.api.v2.entities.BaseChannelMetadata
import com.pubnub.api.v2.entities.BaseUserMetadata
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.managers.ListenerManager
import java.util.UUID

abstract class BasePubNubImpl<EventListener : BaseEventListener,
        Subscription : BaseSubscription<EventListener>,
        Channel : BaseChannel<EventListener, Subscription>,
        ChannelGroup : BaseChannelGroup<EventListener, Subscription>,
        ChannelMetadata: BaseChannelMetadata<EventListener, Subscription>,
        UserMetadata: BaseUserMetadata<EventListener, Subscription>,
        SubscriptionSet: BaseSubscriptionSet<EventListener, Subscription>,
        StatusListener : BaseStatusListener>(
    configuration: PNConfiguration,
) : BasePubNub<EventListener, Subscription, Channel, ChannelGroup, ChannelMetadata, UserMetadata, SubscriptionSet, StatusListener>,
    BaseEventEmitter<EventListener>,
    BaseStatusEmitter<StatusListener> {

    val listenerManager: ListenerManager = ListenerManager(this)
    val internalPubnub = InternalPubNubClient(configuration, listenerManager)

    /**
     * The current version of the Kotlin SDK.
     */
    val version: String
        get() = internalPubnub.version

    val timestamp
        get() = internalPubnub.timestamp()

    /**
     * Unique id of this PubNub instance.
     *
     * @see [PNConfiguration.includeInstanceIdentifier]
     */
    val instanceId: String
        get() = internalPubnub.instanceId

    companion object {
        internal const val TIMESTAMP_DIVIDER = 1000
        internal const val SDK_VERSION = "7.8.1"
        internal const val MAX_SEQUENCE = 65535

        /**
         * Generates random UUID to use. You should set a unique UUID to identify the user or the device that connects to PubNub.
         */
        fun generateUUID() = "pn-${UUID.randomUUID()}"
    }

    override fun subscriptionSetOf(
        channels: Set<String>,
        channelGroups: Set<String>,
        options: SubscriptionOptions
    ): SubscriptionSet {
        val subscriptionSet = subscriptionSetOf(emptySet())
        channels.forEach {
            subscriptionSet.add(channel(it).subscription(options))
        }
        channelGroups.forEach {
            subscriptionSet.add(channelGroup(it).subscription(options))
        }
        return subscriptionSet
    }

    /**
     * Remove a listener.
     *
     * @param listener The listener to be removed.
     */
    override fun removeListener(listener: Listener) {
        listenerManager.removeListener(listener)
    }

    /**
     * Removes all status and event listeners.
     */
    override fun removeAllListeners() {
        listenerManager.removeAllListeners()
    }

    /**
     * Force destroy the SDK to evict the connection pools and close executors.
     */
    open fun forceDestroy() { // TODO open for mockito for integration tests, should not be open
        internalPubnub.forceDestroy()
    }

    /**
     * Destroy the SDK to cancel all ongoing requests and stop heartbeat timer.
     */
    fun destroy() {
        internalPubnub.destroy()
    }
}
