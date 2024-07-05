package com.pubnub.kmp.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.entities.BaseChannelMetadata
import com.pubnub.api.v2.subscriptions.Subscription

/**
 * A representation of a PubNub entity for tracking channel metadata changes.
 *
 * You can get a [Subscription] to listen for metadata events through [Subscribable.subscription].
 *
 * Use the [com.pubnub.api.PubNub.channelMetadata] factory method to create instances of this interface.
 */
interface ChannelMetadata : BaseChannelMetadata<EventListener, Subscription>
