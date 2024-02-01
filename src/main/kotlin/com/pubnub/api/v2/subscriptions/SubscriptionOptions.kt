package com.pubnub.api.v2.subscriptions

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.internal.v2.subscription.FilterImpl
import com.pubnub.internal.v2.subscription.ReceivePresenceEventsImpl

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
    open operator fun plus(options: SubscriptionOptions?): SubscriptionOptions {
        val newOptions = buildSet {
            addAll(allOptions)
            options?.let {
                addAll(it.allOptions)
            }
        }
        return SubscriptionOptions(newOptions)
    }
}

fun ReceivePresenceEvents(): SubscriptionOptions = ReceivePresenceEventsImpl()
fun Filter(predicate: (PNEvent) -> Boolean): SubscriptionOptions = FilterImpl(predicate)

fun main() {
    println((ReceivePresenceEvents() + Filter { true }).allOptions)
}