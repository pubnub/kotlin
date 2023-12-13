package com.pubnub.api.v2.subscriptions

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.internal.v2.subscription.Filter
import com.pubnub.internal.v2.subscription.ReceivePresenceEvents

/**
 * SubscriptionOptions is a mechanism used for supplying optional modifiers for subscriptions.
 *
 * The options currently available in the PubNub client are:
 * * [SubscriptionOptions.filter]
 * * [ChannelOptions.receivePresenceEvents]
 */
open class SubscriptionOptions internal constructor(
    optionsSet: Set<SubscriptionOptions> = emptySet()
) {
    internal val allOptions = optionsSet.toSet()
        get() = field.ifEmpty {
            setOf(this)
        }

    /**
     * Combine multiple options, for example:
     *
     * val options = `SubscriptionOptions.filter( { /* some expression*/ }) + ChannelOptions.receivePresenceEvents()`
     */
    open operator fun plus(options: SubscriptionOptions): SubscriptionOptions {
        val newOptions = buildSet {
            addAll(allOptions)
            addAll(options.allOptions)
        }
        return SubscriptionOptions(newOptions)
    }

    /**
     * Filter the events received through the subscriptions only to those that match the [predicate]
     *
     * @param predicate return `true` to deliver an event to listeners, `false` to discard it
     */
    fun filter(predicate: (PNEvent) -> Boolean): SubscriptionOptions =
        this + Filter(predicate)

    object Default : SubscriptionOptions()

    companion object {
        /**
         * Filter the events received through the subscriptions only to those that match the [predicate]
         *
         * @param predicate return `true` to deliver an event to listeners, `false` to discard it
         */
        fun filter(predicate: (PNEvent) -> Boolean): SubscriptionOptions = Default.filter(predicate)
    }
}

open class ChannelOptions internal constructor(
    optionsSet: Set<SubscriptionOptions> = emptySet()
) : SubscriptionOptions(optionsSet) {
    override operator fun plus(options: SubscriptionOptions): ChannelOptions {
        return ChannelOptions(super.plus(options).allOptions)
    }

    object Default : ChannelOptions()

    /**
     * Enable receiving presence events with this subscription.
     *
     * This is the equivalent of `pubnub.subscribe( ... , withPresence = true)` in the legacy API.
     */
    fun receivePresenceEvents(): ChannelOptions =
        this + ReceivePresenceEvents()

    companion object {
        /**
         * Enable receiving presence events with this subscription.
         *
         * This is the equivalent of `pubnub.subscribe( ... , withPresence = true)` in the legacy API.
         */
        fun receivePresenceEvents(): ChannelOptions = ReceivePresenceEvents()
    }
}
