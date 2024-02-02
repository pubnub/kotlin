package com.pubnub.api.v2.subscriptions

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.internal.v2.subscription.FilterImpl
import com.pubnub.internal.v2.subscription.ReceivePresenceEventsImpl

/**
 * SubscriptionOptions is a mechanism used for supplying optional modifiers for subscriptions.
 *
 * The options currently available in the PubNub client are:
 * * [Filter]
 * * [ReceivePresenceEvents]
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
     * val options = `filter { /* some expression*/ } + receivePresenceEvents()`
     */
    open operator fun plus(options: SubscriptionOptions?): SubscriptionOptions {
        val newOptions = buildSet {
            addAll(allOptions)
            options?.let {
                addAll(it.allOptions)
            }
        }
        return SubscriptionOptions(newOptions)
    }

    companion object {
        /**
         * Enable receiving presence events for a given subscription to a channel or channel group.
         */
        @JvmStatic
        fun receivePresenceEvents(): SubscriptionOptions = ReceivePresenceEventsImpl

        /**
         * Create a filter for messages delivered to [com.pubnub.api.v2.callbacks.EventListener].
         * Please see [com.pubnub.api.v2.callbacks.EventListener] for available events.
         */
        @JvmStatic
        fun filter(predicate: (PNEvent) -> Boolean): SubscriptionOptions = FilterImpl(predicate)
    }
}
