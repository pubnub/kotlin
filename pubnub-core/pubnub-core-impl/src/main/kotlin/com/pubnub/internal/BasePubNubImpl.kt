package com.pubnub.internal

import com.pubnub.api.BasePubNub
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
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
import com.pubnub.internal.subscribe.eventengine.configuration.EventEnginesConf
import java.util.UUID

abstract class BasePubNubImpl<
    EventListener : BaseEventListener,
    Subscription : BaseSubscription<EventListener>,
    Channel : BaseChannel<EventListener, Subscription>,
    ChannelGroup : BaseChannelGroup<EventListener, Subscription>,
    ChannelMetadata : BaseChannelMetadata<EventListener, Subscription>,
    UserMetadata : BaseUserMetadata<EventListener, Subscription>,
    SubscriptionSet : BaseSubscriptionSet<EventListener, Subscription>,
    StatusListener : BaseStatusListener,
    > internal constructor(
    configuration: PNConfigurationCore,
    eventEnginesConf: EventEnginesConf,
) : BasePubNub<EventListener, Subscription, Channel, ChannelGroup, ChannelMetadata, UserMetadata, SubscriptionSet, StatusListener>,
    BaseEventEmitter<EventListener>,
    BaseStatusEmitter<StatusListener> {
    constructor(configuration: PNConfigurationCore) : this(configuration, eventEnginesConf = EventEnginesConf())

    val listenerManager: ListenerManager = ListenerManager(this)
    val corePubNubClient = PubNubCore(configuration, listenerManager, eventEnginesConf)

    /**
     * The current version of the Kotlin SDK.
     */
    override val version: String
        get() = PubNubCore.SDK_VERSION

    override val timestamp: Int
        get() = corePubNubClient.timestamp()

    override val baseUrl: String
        get() = corePubNubClient.baseUrl()

    /**
     * Unique id of this PubNub instance.
     *
     * @see [PNConfigurationCore.includeInstanceIdentifier]
     */
    val instanceId: String
        get() = corePubNubClient.instanceId

    companion object {
        /**
         * Generates random UUID to use. You should set a unique UUID to identify the user or the device that connects to PubNub.
         */
        @JvmStatic
        fun generateUUID() = "pn-${UUID.randomUUID()}"
    }

    override fun subscriptionSetOf(
        channels: Set<String>,
        channelGroups: Set<String>,
        options: SubscriptionOptions,
    ): SubscriptionSet {
        val subscriptionSet = subscriptionSetOf(subscriptions = emptySet())
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
    override fun forceDestroy() {
        corePubNubClient.forceDestroy()
    }

    /**
     * Destroy the SDK to cancel all ongoing requests and stop heartbeat timer.
     */
    override fun destroy() {
        corePubNubClient.destroy()
    }

    override fun reconnect(timetoken: Long) {
        corePubNubClient.reconnect(timetoken)
    }

    override fun parseToken(token: String): PNToken {
        return corePubNubClient.parseToken(token)
    }

    override fun setToken(token: String?) {
        corePubNubClient.setToken(token)
    }

    /**
     * Cancel any subscribe and heartbeat loops or ongoing re-connections.
     *
     * Monitor the results in [SubscribeCallback.status]
     */
    override fun disconnect() {
        corePubNubClient.disconnect()
    }

    /**
     * Unsubscribe from all channels and all channel groups
     */
    override fun unsubscribeAll() {
        corePubNubClient.unsubscribeAll()
    }
}
