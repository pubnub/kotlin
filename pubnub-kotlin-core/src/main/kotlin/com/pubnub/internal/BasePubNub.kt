package com.pubnub.internal

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.v2.callbacks.BaseEventEmitter
import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.callbacks.BaseStatusListener
import com.pubnub.api.v2.callbacks.StatusEmitter
import com.pubnub.api.v2.entities.BaseChannel
import com.pubnub.api.v2.entities.BaseChannelGroup
import com.pubnub.api.v2.entities.BaseChannelMetadata
import com.pubnub.api.v2.entities.BaseUserMetadata
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.callbacks.SubscribeCallback
import com.pubnub.internal.managers.ListenerManager
import com.pubnub.internal.subscribe.eventengine.configuration.EventEnginesConf
import com.pubnub.internal.v2.subscription.BaseSubscriptionImpl
import com.pubnub.internal.v2.subscription.BaseSubscriptionSetImpl
import java.util.UUID

abstract class BasePubNub internal constructor(
    configuration: PNConfiguration,
    eventEnginesConf: EventEnginesConf,
    subscriptionFactory: SubscriptionFactory<BaseSubscriptionImpl>,
    subscriptionSetFactory: SubscriptionSetFactory<BaseSubscriptionSetImpl>
) : BaseEventEmitter, StatusEmitter {
    constructor(
        configuration: PNConfiguration,
        subscriptionFactory: SubscriptionFactory<BaseSubscriptionImpl>,
        subscriptionSetFactory: SubscriptionSetFactory<BaseSubscriptionSetImpl>
    ) : this(
        configuration, EventEnginesConf(), subscriptionFactory,
        subscriptionSetFactory
    )

    val listenerManager: ListenerManager = ListenerManager(this)

    val pubNubImpl = PubNubImpl(configuration, listenerManager, eventEnginesConf, subscriptionFactory, subscriptionSetFactory)

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
     * Create a handle to a [BaseChannel] that can be used to obtain a [BaseSubscription].
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a channel is required.
     *
     * The returned [BaseChannel] holds a reference to this [PubNub] instance internally.
     *
     * @param name the name of the channel to return. Supports wildcards by ending it with ".*". See more in the
     * [documentation](https://www.pubnub.com/docs/general/channels/overview)
     *
     * @return a [BaseChannel] instance representing the channel with the given [name]
     */
    open fun channel(name: String): BaseChannel = pubNubImpl.channel(name)

    /**
     * Create a handle to a [BaseChannelGroup] that can be used to obtain a [BaseSubscription].
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a channel group is required.
     *
     * The returned [BaseChannelGroup] holds a reference to this [PubNub] instance internally.
     *
     * @param name the name of the channel group to return. See more in the
     * [documentation](https://www.pubnub.com/docs/general/channels/subscribe#channel-groups)
     *
     * @return a [BaseChannelGroup] instance representing the channel group with the given [name]
     */
    open fun channelGroup(name: String): BaseChannelGroup = pubNubImpl.channelGroup(name)

    /**
     * Create a handle to a [BaseChannelMetadata] object that can be used to obtain a [BaseSubscription] to metadata events.
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a metadata channel is required.
     *
     * The returned [BaseChannelMetadata] holds a reference to this [PubNub] instance internally.
     *
     * @param id the id of the channel metadata to return. See more in the
     * [documentation](https://www.pubnub.com/docs/general/metadata/channel-metadata)
     *
     * @return a [BaseChannelMetadata] instance representing the channel metadata with the given [id]
     */
    open fun channelMetadata(id: String): BaseChannelMetadata = pubNubImpl.channelMetadata(id)

    /**
     * Create a handle to a [BaseUserMetadata] object that can be used to obtain a [BaseSubscription] to user metadata events.
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a user metadata is required.
     *
     * The returned [BaseUserMetadata] holds a reference to this [PubNub] instance internally.
     *
     * @param id the id of the user. See more in the
     * [documentation](https://www.pubnub.com/docs/general/metadata/users-metadata)
     *
     * @return a [BaseUserMetadata] instance representing the channel metadata with the given [id]
     */
    open fun userMetadata(id: String): BaseUserMetadata = pubNubImpl.userMetadata(id)

    /**
     * Create a [BaseSubscriptionSet] from the given [subscriptions].
     *
     * @param subscriptions the subscriptions that will be added to the returned [BaseSubscriptionSet]
     * @return a [BaseSubscriptionSet] containing all [subscriptions]
     */
    open fun subscriptionSetOf(subscriptions: Set<BaseSubscription> = emptySet()) = pubNubImpl.subscriptionSetOf(subscriptions)

    /**
     * Create a [BaseSubscriptionSet] containing [BaseSubscription] objects for the given sets of [channels] and
     * [channelGroups].
     *
     * Please note that the subscriptions are not active until you call [BaseSubscriptionSet.subscribe].
     *
     * This is a convenience method, and it is equal to calling [PubNub.channel] followed by [BaseChannel.subscription] for
     * each channel, then creating a [subscriptionSetOf] using the returned [BaseSubscription] objects (and similarly for
     * channel groups).
     *
     * @param channels the channels to create subscriptions for
     * @param channelGroups the channel groups to create subscriptions for
     * @param options the [SubscriptionOptions] to pass for each subscription. Refer to supported options in [BaseChannel] and
     * [BaseChannelGroup] documentation.
     * @return a [BaseSubscriptionSet] containing subscriptions for the given [channels] and [channelGroups]
     */
    open fun subscriptionSetOf(
        channels: Set<String> = emptySet(),
        channelGroups: Set<String> = emptySet(),
        options: SubscriptionOptions = EmptyOptions
    ): BaseSubscriptionSet = pubNubImpl.subscriptionSetOf(channels, channelGroups, options)

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
    override fun addListener(listener: BaseStatusListener) {
        listenerManager.addListener(listener)
    }

    /**
     * Add a global listener for events in all active subscriptions.
     *
     * @param listener The listener to be added.
     */
    override fun addListener(listener: BaseEventListener) {
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
