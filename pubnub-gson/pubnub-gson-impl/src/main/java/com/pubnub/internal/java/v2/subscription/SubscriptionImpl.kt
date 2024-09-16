package com.pubnub.internal.java.v2.subscription

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.java.v2.callbacks.EventListener
import com.pubnub.api.java.v2.callbacks.StatusListener
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.java.PubNubForJavaImpl
import com.pubnub.internal.java.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.java.v2.callbacks.DelegatingStatusListener
import com.pubnub.internal.java.v2.callbacks.EventEmitterInternal
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName
import com.pubnub.internal.v2.subscription.SubscriptionImpl
import com.pubnub.internal.v2.subscription.SubscriptionInternal

class SubscriptionImpl(
    override val pubnub: PubNubForJavaImpl,
    channels: Set<ChannelName> = emptySet(),
    channelGroups: Set<ChannelGroupName> = emptySet(),
    options: SubscriptionOptions = EmptyOptions
) :
    SubscriptionImpl(pubnub, channels, channelGroups, options),
        com.pubnub.api.java.v2.subscriptions.Subscription,
        EventEmitterInternal {
    override fun plus(
        subscription: com.pubnub.api.java.v2.subscriptions.Subscription,
    ): com.pubnub.api.java.v2.subscriptions.SubscriptionSet {
        return SubscriptionSetImpl(pubnub, setOf(this, subscription) as Set<SubscriptionInternal>)
    }

    override fun subscribe() {
        subscribe(SubscriptionCursor(0))
    }

    override fun addListener(listener: EventListener) {
        addListener(DelegatingEventListener(listener, pubnub))
    }

    override fun removeListener(listener: Listener) {
        if (listener is EventListener) {
            super.removeListener(DelegatingEventListener(listener, pubnub))
        } // no else here to support SubscribeCallbacks which implement both interfaces
        if (listener is StatusListener) {
            super.removeListener(DelegatingStatusListener(listener, pubnub))
        }
    }

    companion object {
        fun from(subscription: SubscriptionImpl): com.pubnub.internal.java.v2.subscription.SubscriptionImpl {
            return com.pubnub.internal.java.v2.subscription.SubscriptionImpl(
                subscription.pubnub as PubNubForJavaImpl,
                subscription.channels,
                subscription.channelGroups,
                subscription.options
            )
        }
    }
}
