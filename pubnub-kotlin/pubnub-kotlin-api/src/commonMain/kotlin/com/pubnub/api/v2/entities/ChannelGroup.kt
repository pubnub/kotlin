package com.pubnub.api.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.kmp.v2.entities.ChannelGroup

/**
 * A representation of a PubNub channel group identified by its [name].
 *
 * You can get a [Subscription] to this channel group through [Subscribable.subscription].
 *
 * Use the [com.pubnub.api.PubNub.channelGroup] factory method to create instances of this interface.
 */
interface ChannelGroup : BaseChannelGroup<EventListener, Subscription>, ChannelGroup
