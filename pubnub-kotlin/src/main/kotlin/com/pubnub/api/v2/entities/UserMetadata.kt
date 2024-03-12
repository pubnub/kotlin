package com.pubnub.api.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.Subscription

/**
 * A representation of a PubNub entity for tracking user metadata changes.
 *
 * You can get a [Subscription] to listen for metadata events through [Subscribable.subscription].
 *
 * Use the [com.pubnub.api.PubNub.userMetadata] factory method to create instances of this interface.
 */
interface UserMetadata : BaseUserMetadata<EventListener, Subscription>
