package com.pubnub.internal.java.v2.subscription

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.java.v2.callbacks.EventListener
import com.pubnub.api.java.v2.callbacks.StatusListener
import com.pubnub.api.java.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.internal.java.PubNubForJavaImpl
import com.pubnub.internal.java.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.java.v2.callbacks.DelegatingStatusListener
import com.pubnub.internal.java.v2.callbacks.EventEmitterInternal
import com.pubnub.internal.v2.subscription.SubscriptionInternal
import com.pubnub.internal.v2.subscription.SubscriptionSetImpl

class SubscriptionSetImpl(
    private val pubnubJava: PubNubForJavaImpl,
    initialSubscriptions: Set<SubscriptionInternal>
) :
    SubscriptionSetImpl(pubnubJava, initialSubscriptions),
        com.pubnub.api.java.v2.subscriptions.SubscriptionSet,
        EventEmitterInternal {
    override val subscriptions: Set<Subscription>
        get() = super.subscriptions.map { it as Subscription }.toSet()

    override fun plusAssign(subscription: Subscription) {
        super.addInternal(subscription as SubscriptionInternal)
    }

    override fun minusAssign(subscription: Subscription) {
        super.removeInternal(subscription as SubscriptionInternal)
    }

    /**
     * Start receiving events from the subscriptions (or subscriptions) represented by this object.
     *
     * The PubNub client will start a network connection to the server if it doesn't have one already,
     * or will alter the existing connection to add channels and groups requested by this subscriptions if needed.
     */
    override fun subscribe() {
        subscribe(SubscriptionCursor(0))
    }

    override fun add(subscription: Subscription) {
        plusAssign(subscription)
    }

    override fun remove(subscription: Subscription) {
        minusAssign(subscription)
    }

    override fun addListener(listener: EventListener) {
        addListener(DelegatingEventListener(listener, pubnubJava))
    }

    override fun removeListener(listener: Listener) {
        if (listener is EventListener) {
            super.removeListener(DelegatingEventListener(listener, pubnubJava))
        } // no else here to support SubscribeCallbacks which implement both interfaces
        if (listener is StatusListener) {
            super.removeListener(DelegatingStatusListener(listener, pubnubJava))
        }
    }
}
