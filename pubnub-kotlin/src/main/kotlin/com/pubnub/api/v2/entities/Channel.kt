package com.pubnub.api.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.Subscription

/**
 * A representation of a PubNub channel identified by its [name].
 *
 * You can get a [Subscription] to this channel through [Subscribable.subscription].
 *
 * Use the [com.pubnub.api.PubNub.channel] factory method to create instances of this interface.
 */
interface Channel : BaseChannel<EventListener, Subscription>
