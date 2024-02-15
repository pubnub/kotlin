package com.pubnub.internal

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.StatusEmitter
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.callbacks.SubscribeCallback
import com.pubnub.internal.managers.ListenerManager
import com.pubnub.internal.subscribe.eventengine.configuration.EventEnginesConf
import com.pubnub.internal.v2.callbacks.EventListener
import com.pubnub.internal.v2.callbacks.StatusListener
import java.util.UUID

abstract class BasePubNub internal constructor(configuration: PNConfiguration, eventEnginesConf: EventEnginesConf) : EventEmitter, StatusEmitter {
    constructor(configuration: PNConfiguration) : this(configuration, EventEnginesConf())

    val listenerManager: ListenerManager = ListenerManager(this)
    val pubNubImpl = PubNubImpl(this, configuration, listenerManager, eventEnginesConf)

    /**
     * The current version of the Kotlin SDK.
     */
    val version: String
        get() = pubNubImpl.version

    /**
     * Unique id of this PubNub instance.
     *
     * @see [PNConfiguration.includeInstanceIdentifier]
     */
    val instanceId: String
        get() = pubNubImpl.instanceId

    companion object {
        internal const val TIMESTAMP_DIVIDER = 1000
        internal const val SDK_VERSION = "7.8.1"
        internal const val MAX_SEQUENCE = 65535

        /**
         * Generates random UUID to use. You should set a unique UUID to identify the user or the device that connects to PubNub.
         */
        fun generateUUID() = "pn-${UUID.randomUUID()}"
    }

    /**
     * Create a handle to a [Channel] that can be used to obtain a [Subscription].
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a channel is required.
     *
     * The returned [Channel] holds a reference to this [PubNub] instance internally.
     *
     * @param name the name of the channel to return. Supports wildcards by ending it with ".*". See more in the
     * [documentation](https://www.pubnub.com/docs/general/channels/overview)
     *
     * @return a [Channel] instance representing the channel with the given [name]
     */
    fun channel(name: String): Channel = pubNubImpl.channel(name)

    /**
     * Create a handle to a [ChannelGroup] that can be used to obtain a [Subscription].
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a channel group is required.
     *
     * The returned [ChannelGroup] holds a reference to this [PubNub] instance internally.
     *
     * @param name the name of the channel group to return. See more in the
     * [documentation](https://www.pubnub.com/docs/general/channels/subscribe#channel-groups)
     *
     * @return a [ChannelGroup] instance representing the channel group with the given [name]
     */
    fun channelGroup(name: String): ChannelGroup = pubNubImpl.channelGroup(name)

    /**
     * Create a handle to a [ChannelMetadata] object that can be used to obtain a [Subscription] to metadata events.
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a metadata channel is required.
     *
     * The returned [ChannelMetadata] holds a reference to this [PubNub] instance internally.
     *
     * @param id the id of the channel metadata to return. See more in the
     * [documentation](https://www.pubnub.com/docs/general/metadata/channel-metadata)
     *
     * @return a [ChannelMetadata] instance representing the channel metadata with the given [id]
     */
    fun channelMetadata(id: String): ChannelMetadata = pubNubImpl.channelMetadata(id)

    /**
     * Create a handle to a [UserMetadata] object that can be used to obtain a [Subscription] to user metadata events.
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a user metadata is required.
     *
     * The returned [UserMetadata] holds a reference to this [PubNub] instance internally.
     *
     * @param id the id of the user. See more in the
     * [documentation](https://www.pubnub.com/docs/general/metadata/users-metadata)
     *
     * @return a [UserMetadata] instance representing the channel metadata with the given [id]
     */
    fun userMetadata(id: String): UserMetadata = pubNubImpl.userMetadata(id)

    /**
     * Create a [SubscriptionSet] from the given [subscriptions].
     *
     * @param subscriptions the subscriptions that will be added to the returned [SubscriptionSet]
     * @return a [SubscriptionSet] containing all [subscriptions]
     */
    fun subscriptionSetOf(subscriptions: Set<Subscription> = emptySet()) = pubNubImpl.subscriptionSetOf(subscriptions)

    /**
     * Create a [SubscriptionSet] containing [Subscription] objects for the given sets of [channels] and
     * [channelGroups].
     *
     * Please note that the subscriptions are not active until you call [SubscriptionSet.subscribe].
     *
     * This is a convenience method, and it is equal to calling [PubNub.channel] followed by [Channel.subscription] for
     * each channel, then creating a [subscriptionSetOf] using the returned [Subscription] objects (and similarly for
     * channel groups).
     *
     * @param channels the channels to create subscriptions for
     * @param channelGroups the channel groups to create subscriptions for
     * @param options the [SubscriptionOptions] to pass for each subscription. Refer to supported options in [Channel] and
     * [ChannelGroup] documentation.
     * @return a [SubscriptionSet] containing subscriptions for the given [channels] and [channelGroups]
     */
    fun subscriptionSetOf(
        channels: Set<String> = emptySet(),
        channelGroups: Set<String> = emptySet(),
        options: SubscriptionOptions = EmptyOptions
    ): SubscriptionSet = pubNubImpl.subscriptionSetOf(channels, channelGroups, options)

    /**
     * Add a legacy listener for both client status and events.
     *
     * @param listener The listener to be added.
     */
    fun addListener(listener: SubscribeCallback) {
        listenerManager.addListener(listener)
    }

    /**
     * Add a listener for client status changes.
     *
     * @param listener The listener to be added.
     */
    override fun addListener(listener: StatusListener) {
        listenerManager.addListener(listener)
    }

    /**
     * Add a global listener for events in all active subscriptions.
     *
     * @param listener The listener to be added.
     */
    override fun addListener(listener: EventListener) {
        listenerManager.addListener(listener)
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
    fun forceDestroy() {
        pubNubImpl.forceDestroy()
    }

    /**
     * Destroy the SDK to cancel all ongoing requests and stop heartbeat timer.
     */
    fun destroy() {
        pubNubImpl.destroy()
    }
}
