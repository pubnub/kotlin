package com.pubnub.internal.v2.subscription

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.v2.callbacks.DelegatingEventListener

class SubscriptionSetImpl(
    pubnub: PubNubCore,
    initialSubscriptions: Set<SubscriptionImpl>,
) : SubscriptionSet, BaseSubscriptionSetImpl<EventListener, Subscription>(pubnub, initialSubscriptions) {
    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    override fun addListener(listener: EventListener) {
        addListener(DelegatingEventListener(listener))
    }
}
