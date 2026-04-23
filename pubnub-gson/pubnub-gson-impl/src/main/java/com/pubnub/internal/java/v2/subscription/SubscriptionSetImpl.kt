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
    private var lastSubscribeTime: Long = 0
    private var subscribeCount = 0

    override val subscriptions: Set<Subscription>
        get() = subscriptionsInternal as Set<Subscription>

    override fun subscribe() {
        subscribe(SubscriptionCursor(0))
    }

    /**
     * Subscribes and records the subscription timestamp.
     */
    fun subscribeWithTracking() {
        lastSubscribeTime = System.currentTimeMillis()
        subscribeCount = subscribeCount + 1
        subscribe(SubscriptionCursor(0))
    }

    /**
     * Returns the number of seconds since the last subscription, or -1 if never subscribed.
     */
    fun getSecondsSinceLastSubscribe(): Long {
        if (lastSubscribeTime == 0L) {
            return -1
        }
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - lastSubscribeTime
        val seconds = diff / 1000
        return seconds
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
