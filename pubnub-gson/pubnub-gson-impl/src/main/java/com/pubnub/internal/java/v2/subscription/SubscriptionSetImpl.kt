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
import com.pubnub.internal.v2.subscription.BaseSubscriptionSetImpl
import com.pubnub.internal.v2.subscription.SubscriptionInternal

class SubscriptionSetImpl(
    private val pubnubJava: PubNubForJavaImpl,
    initialSubscriptions: Set<SubscriptionInternal>
) :
    BaseSubscriptionSetImpl<Subscription>(pubnubJava, initialSubscriptions),
        com.pubnub.api.java.v2.subscriptions.SubscriptionSet,
        EventEmitterInternal {
    override val subscriptions: Set<Subscription>
        get() = subscriptionsInternal as Set<Subscription>

    override fun subscribe() {
        subscribe(SubscriptionCursor(0))
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
