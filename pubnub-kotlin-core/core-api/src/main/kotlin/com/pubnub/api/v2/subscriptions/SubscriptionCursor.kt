package com.pubnub.api.v2.subscriptions

/**
 * A holder for a timetoken value.
 *
 * Used with [BaseSubscription.subscribe] to start listening for events newer or equal to the requested timetoken.
 */
class SubscriptionCursor(
    val timetoken: Long
)
