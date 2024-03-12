package com.pubnub.api.v2.entities

import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

/**
 * This interface is implemented by entities that can be subscribed to, such as channels, channel groups, and user and
 * channel metadata.
 */
interface Subscribable<EvLis : BaseEventListener> {
    fun subscription(options: SubscriptionOptions = EmptyOptions): BaseSubscription<EvLis>
}
